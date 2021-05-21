package com.hyphenate.liaoxin.common.net.bean;

public class MyContacts {
    public String name;
    public String phone;
    public String note;
    public String group;

    @Override
    public String toString() {
        return "MyContacts{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", note='" + note + '\'' +
                ", group='" + group + '\'' +
                '}';
    }


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
