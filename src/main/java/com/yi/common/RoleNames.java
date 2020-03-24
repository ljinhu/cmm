package com.yi.common;

/**
 * @Author: ruihui.li
 * @Date: 2020/3/24 09:04
 * @Description:
 */
public enum RoleNames {

    TEACHER("teacher"),MANAGER("manager"),PARENT("parent");
    private String name;

    private RoleNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }}