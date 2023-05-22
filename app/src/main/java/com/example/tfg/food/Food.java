package com.example.tfg.food;

public class Food {
    private final Integer food_id;
    private final String nombre_es;
    private final Micronutrients micronutrientes;

    public Food(Integer food_id, String nombre_es, Micronutrients micronutrientes){
        this.food_id = food_id;
        this.nombre_es = nombre_es;
        this.micronutrientes = micronutrientes;
    }

    public Integer getId() {
        return food_id;
    }

    public String getFoodName() {
        return nombre_es;
    }

    public Micronutrients getMicronutrientes(){
        return micronutrientes;
    }
}
