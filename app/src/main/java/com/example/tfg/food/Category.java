package com.example.tfg.food;

public class Category {
    Integer category_id;
    String category_name;

    public Category(Integer category_id, String category_name){
        this.category_id = category_id;
        this.category_name = category_name;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public String getCategory_name() {
        return category_name;
    }
}
