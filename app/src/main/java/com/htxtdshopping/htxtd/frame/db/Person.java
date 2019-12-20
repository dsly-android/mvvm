package com.htxtdshopping.htxtd.frame.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * @author 陈志鹏
 * @date 2019-08-23
 */
@Entity
public class Person {
    @Id
    private long id;

    private String name;

    private ToOne<User> user;

    public Person() {
    }

    //添加这个可以提升性能
    public Person(long id, String name, long userId) {
        this.id = id;
        this.name = name;
        user.setTargetId(userId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ToOne<User> getUser() {
        return user;
    }
}
