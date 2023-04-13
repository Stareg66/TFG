package com.example.tfg.food;

public class Food {
    private final Integer id;
    private final String foodName;
    private final Integer kcal;

    public Food(Integer id, String foodName, Integer kcal){
        this.id = id;
        this.foodName = foodName;
        this.kcal = kcal;
    }

    public Integer getId() {
        return id;
    }

    public String getFoodName() {
        return foodName;
    }

    public Integer getKcal() {
        return kcal;
    }
}
