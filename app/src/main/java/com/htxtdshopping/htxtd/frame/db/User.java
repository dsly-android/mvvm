package com.htxtdshopping.htxtd.frame.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Unique;
import io.objectbox.relation.ToOne;

/**
 * @author 陈志鹏
 * @date 2019-08-22
 */
@Entity
public class User {
    @Id(assignable = false)
    private long id;

    //index能增加查询的速度。String为HASH，其他为VALUE。
    // 哈希适用于等式检查，但不适用于“starts with”开头的类型条件。 如果经常使用它们，则应使用基于值的索引。
    @Index
    private String name;

    //unique在数据库中保存值是唯一值，
    //如果违反了唯一约束，put（）操作将中止并抛出UniqueViolationException：
    @Unique
    private String description;

    private ToOne<Person> person;

    public User() {
    }

    public User(long id, String name, String description, long personId) {
        this.id = id;
        this.name = name;
        this.description = description;
        person.setTargetId(personId);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ToOne<Person> getPerson() {
        return person;
    }
}
