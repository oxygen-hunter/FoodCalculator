package com.example.foodcounter.service;

import com.example.foodcounter.model.Food;

import java.util.Map;
import java.util.Set;

public interface FoodService {

    Iterable<Food> getFoods();

    Set<Map.Entry<Food, Integer>> getCart();

    void addProductById(String id, int quantity);

    Double getCarbo();

    Double getProtein();

    Double getFat();

    Double getHeat();
}
