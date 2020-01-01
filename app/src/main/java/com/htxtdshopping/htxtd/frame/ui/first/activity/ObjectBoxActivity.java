package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.base.AppContext;
import com.htxtdshopping.htxtd.frame.databinding.ActivityObjectBoxBinding;
import com.htxtdshopping.htxtd.frame.db.Customer;
import com.htxtdshopping.htxtd.frame.db.Order;
import com.htxtdshopping.htxtd.frame.db.Order_;
import com.htxtdshopping.htxtd.frame.db.Person;
import com.htxtdshopping.htxtd.frame.db.Student;
import com.htxtdshopping.htxtd.frame.db.Student_;
import com.htxtdshopping.htxtd.frame.db.Teacher;
import com.htxtdshopping.htxtd.frame.db.Teacher_;
import com.htxtdshopping.htxtd.frame.db.User;

import java.util.List;

import androidx.lifecycle.Observer;
import io.objectbox.Box;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.android.ObjectBoxLiveData;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscriptionList;
import io.objectbox.reactive.ErrorObserver;
import io.objectbox.relation.ToMany;
import io.objectbox.rx.RxBoxStore;
import io.objectbox.rx.RxQuery;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ObjectBoxActivity extends BaseFitsWindowActivity<ActivityObjectBoxBinding, BaseViewModel> {

    private Box<Person> personBox;
    private Box<User> userBox;

    private DataSubscriptionList mList = new DataSubscriptionList();

    @Override
    public int getLayoutId() {
        return R.layout.activity_object_box;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        personBox = AppContext.boxFor(Person.class);
        userBox = AppContext.boxFor(User.class);

        changeListener();
    }

    @Override
    public void initEvent() {
        
    }

    @Override
    public void initData() {

    }

    private void changeListener() {
        //数据库数据更新回调
        getNoteLiveData().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> personList) {
                Log.e("aaa", personList.size() + "   " + Thread.currentThread().getName());
            }
        });

        userBox.query().build().subscribe(mList)
                .on(AndroidScheduler.mainThread())
                .onError(new ErrorObserver() {
                    @Override
                    public void onError(Throwable th) {
                        Log.e("error", th.getMessage());
                    }
                })
                .observer(new DataObserver<List<User>>() {
                    @Override
                    public void onData(List<User> data) {
                        Log.e("data", data.size() + "  " + Thread.currentThread().getName());
                    }
                });
        AppContext.getInstance().getBoxStore()
                .subscribe(User.class).dataSubscriptionList(mList)
                .on(AndroidScheduler.mainThread())
                .observer(new DataObserver<Class<User>>() {
                    @Override
                    public void onData(Class<User> data) {
                        Log.e("onData", Thread.currentThread().getName());
                    }
                });
    }

    private ObjectBoxLiveData<Person> noteLiveData;

    public ObjectBoxLiveData<Person> getNoteLiveData() {
        if (noteLiveData == null) {
            // query all notes, sorted a-z by their text
            noteLiveData = new ObjectBoxLiveData<>(personBox.query().build());
        }
        return noteLiveData;
    }

    public void rxjava(View view) {
        RxBoxStore.<User>observable(AppContext.getInstance().getBoxStore())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(new Consumer<Class>() {
                    @Override
                    public void accept(Class aClass) throws Exception {
                        Log.e("aaa", "change");
                    }
                });

        Query<User> query = userBox.query().build();
        RxQuery.observable(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        Log.e("aaa", "change");
                    }
                });
    }

    public void first(View view) {
        personBox.removeAll();
        userBox.removeAll();

        final User user1 = new User();
        user1.setName("user1");
        user1.setDescription("description1");
        final Person person1 = new Person();
        person1.setName("person1");
        person1.getUser().setTarget(user1);
        personBox.put(person1);

        User user2 = new User();
        user2.setName("user2");
        user2.setDescription("description2");
        Person person2 = new Person();
        person2.setName("person2");
        user2.getPerson().setTarget(person2);
        userBox.put(user2);

        List<Person> personList = personBox.getAll();
        List<User> userList = userBox.getAll();
        for (int i = 0; i < personList.size(); i++) {
            Log.e("aaa", personList.get(i).getName());
        }
        for (int i = 0; i < userList.size(); i++) {
            Log.e("bbb", userList.get(i).getName());
        }
    }

    public void second(View view) {
        Box<Customer> customerBox = AppContext.boxFor(Customer.class);
        Box<Order> orderBox = AppContext.boxFor(Order.class);

        // Remove all previous object to have clear start for simplicity's sake
        customerBox.removeAll();
        orderBox.removeAll();

        logTitle("Add a customer with an order (using to-one)");
        Customer customer = new Customer();
        Order order1 = new Order();
        order1.customer.setTarget(customer);
        orderBox.put(order1);
        logOrders(orderBox, customer);
        logCustomer();

        logTitle("Add two orders to the customer (from the other side of the relation using the to-many backlink)");
        customer.orders.reset(); // just to be on the safe side before adding
        customer.orders.add(new Order());
        customer.orders.add(new Order());
        customerBox.put(customer);
        logOrders(orderBox, customer);
        logCustomer();

        logTitle("Remove (delete) the first order object");
        orderBox.remove(order1);
        logOrders(orderBox, customer);
        logCustomer();

        logTitle("Remove an order from the to-many relation (does not delete the order object)");
        customer.orders.reset();
        customer.orders.remove(0);
        customerBox.put(customer);
        logOrders(orderBox, customer);
        logCustomer();
    }

    private void logOrders(Box<Order> orderBox, Customer customer) {
        List<Order> ordersQueried = orderBox.query().equal(Order_.customerId, customer.id).build().find();
        log("Customer " + customer.id + " has " + ordersQueried.size() + " orders");
        for (Order order : ordersQueried) {
            log("Order " + order.id + " related to customer " + order.customer.getTargetId());
        }
        log("");
    }

    private void logCustomer(){
        List<Customer> customers = AppContext.boxFor(Customer.class).getAll();
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            customer.orders.reset();
            for (int j = 0; j < customer.orders.size(); j++) {
                Order order = customer.orders.get(j);
                Log.e("aaa","order:"+order.id+"");
            }
            Log.e("aaa","customer:"+customer.id+"");
        }
    }

    public void third(View view) {
        Box<Student> studentBox = AppContext.boxFor(Student.class);
        Box<Teacher> teacherBox = AppContext.boxFor(Teacher.class);

        // Remove all previous object to have clear start for simplicity's sake
        studentBox.removeAll();
        teacherBox.removeAll();

        logTitle("Add two students and two teachers; first student has two teachers, second student has one teacher");
        Teacher obiWan = new Teacher("Obi-Wan Kenobi");
        Teacher yoda = new Teacher("Yoda");
        Student luke = new Student("Luke Skywalker");
        luke.teachers.add(obiWan);
        luke.teachers.add(yoda);
        Student anakin = new Student("Anakin Skywalker");
        anakin.teachers.add(obiWan);
        studentBox.put(luke, anakin);
        logTeachers(studentBox, teacherBox);

        // https://docs.objectbox.io/queries
        logTitle("Query for all students named \"Skywalker\" taught by \"Yoda\"");
        QueryBuilder<Student> builder = studentBox.query().contains(Student_.name, "Skywalker");
        builder.link(Student_.teachers).equal(Teacher_.name, yoda.name);
        List<Student> studentsTaughtByYoda = builder.build().find();
        log("There is " + studentsTaughtByYoda.size() + " student taught by Yoda: "
                + studentsTaughtByYoda.get(0).name);
        log("");

        logTitle("Remove first teacher from first student");
        luke.teachers.remove(obiWan);
        luke.teachers.applyChangesToDb(); // more efficient than studentBox.put(student1);
        logTeachers(studentBox, teacherBox);

        logTitle("Remove student of second teacher using backlink");
        yoda.students.clear();
        yoda.students.applyChangesToDb();
        logTeachers(studentBox, teacherBox);
    }

    private void logTeachers(Box<Student> studentBox, Box<Teacher> teacherBox) {
        log("There are " + teacherBox.count() + " teachers");
        List<Teacher> students = teacherBox.getAll();
        for (Teacher teacher : students) {
            ToMany<Student> teachersToMany = teacher.students;
            for (Student student : teachersToMany) {
                log("Student " + student.id + " is taught by teacher " + teacher.id);
            }
        }
        log("");
    }

    private void log(String message) {
        mBinding.textViewLog.append(message + "\n");
    }

    private void logTitle(String message) {
        Spannable spannableString = new SpannableString(message.concat("\n"));
        StyleSpan span = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(span, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.textViewLog.append(spannableString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mList.cancel();
        getNoteLiveData().removeObservers(this);
    }
}
