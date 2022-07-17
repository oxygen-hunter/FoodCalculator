package com.example.foodcalculator.service;

import com.example.foodcalculator.model.Food;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.Set;

public interface FoodService {

    Iterable<Food> getFoods();

    Set<Map.Entry<Food, Integer>> getCart();

    void addProductById(String id, int quantity);

    void removeProductById(String id);

    void removeAll();

    void fillDataFromXlsx(File file);

    void uploadDataFromXlsx(MultipartFile file);

    Double getCarbo();

    Double getProtein();

    Double getFat();

    Double getHeat();
}
