package com.example.tfg.food;

public class Food {
    private final Integer food_id;
    private final String nombre_es;
    private final Long group_id;
    private final Micronutrients micronutrientes;

    public Food(Integer food_id, String nombre_es, Long group_id, Micronutrients micronutrientes){
        this.food_id = food_id;
        this.nombre_es = nombre_es;
        this.group_id = group_id;
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

    public Long getGroup_id() {
        return group_id;
    }
}
