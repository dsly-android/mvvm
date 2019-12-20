package com.htxtdshopping.htxtd.frame.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Student {

    @Id
    public long id;

    public String name;

    public ToMany<Teacher> teachers;

    // used by ObjectBox to init relations
    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }
}
