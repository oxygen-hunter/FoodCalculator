package com.example.foodcalculator.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "foods")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Food {

    @Id
    @Getter
    @Setter
    @Column(name = "id")
    private String id;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @Column(name = "carbo")
    private double carbo;

    @Getter
    @Setter
    @Column(name = "protein")
    private double protein;

    @Getter
    @Setter
    @Column(name = "fat")
    private double fat;

    @Getter
    @Setter
    @Column(name = "heat")
    private double heat;

    public String getImage() {
        return "https://images-na.ssl-images-amazon.com/images/I/31nTxlNh1OL.jpg";
    }

//    public Food(String id, String name, double carbo, double protein, double fat, double heat) {
//        this.name = name;
//        this.carbo = carbo;
//        this.protein = protein;
//        this.fat = fat;
//        this.heat = heat;
//    }
}
