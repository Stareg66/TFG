package com.example.tfg.food;

public class Micronutrients {
    private Double proteina_total;
    private Double carbohidratos;
    private Double fibra_total;
    private Double azucares_totales;
    private Double grasa_total;
    private Double ag_saturados_total;
    private Double ag_poliinsaturados_total;
    private Double ag_monoinsaturados_total;
    private Double ag_trans_total;
    private Double colesterol;
    private Double sodio;
    private Double potasio;
    private Double vitamina_a;
    private Double vitamina_c;
    private Double calcio;
    private Double hierro_total;

    public Micronutrients(Double proteina_total, Double carbohidratos, Double fibra_total, Double azucares_totales, Double grasa_total, Double ag_saturados_total, Double ag_poliinsaturados_total,
                          Double ag_monoinsaturados_total, Double ag_trans_total, Double colesterol, Double sodio, Double potasio, Double vitamina_a, Double vitamina_c, Double calcio, Double hierro_total){
        this.proteina_total = proteina_total;
        this.carbohidratos = carbohidratos;
        this.fibra_total = fibra_total;
        this.azucares_totales = azucares_totales;
        this.grasa_total = grasa_total;
        this.ag_saturados_total = ag_saturados_total;
        this.ag_poliinsaturados_total = ag_poliinsaturados_total;
        this.ag_monoinsaturados_total = ag_monoinsaturados_total;
        this.ag_trans_total = ag_trans_total;
        this.colesterol = colesterol;
        this.sodio = sodio;
        this.potasio = potasio;
        this.vitamina_a = vitamina_a;
        this.vitamina_c = vitamina_c;
        this.calcio = calcio;
        this.hierro_total = hierro_total;
    }

    public Double getProteina_total() {
        return proteina_total;
    }

    public Double getCarbohidratos() {
        return carbohidratos;
    }

    public Double getFibra_total() {
        return fibra_total;
    }

    public Double getAzucares_totales() {
        return azucares_totales;
    }

    public Double getGrasa_total() {
        return grasa_total;
    }

    public Double getAg_saturados_total() {
        return ag_saturados_total;
    }

    public Double getAg_poliinsaturados_total() {
        return ag_poliinsaturados_total;
    }

    public Double getAg_monoinsaturados_total() {
        return ag_monoinsaturados_total;
    }

    public Double getAg_trans_total() {
        return ag_trans_total;
    }

    public Double getColesterol() {
        return colesterol;
    }

    public Double getSodio() {
        return sodio;
    }

    public Double getPotasio() {
        return potasio;
    }

    public Double getVitamina_a() {
        return vitamina_a;
    }

    public Double getVitamina_c() {
        return vitamina_c;
    }

    public Double getCalcio() {
        return calcio;
    }

    public Double getHierro_total() {
        return hierro_total;
    }

}
