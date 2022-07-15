package com.example.foodcounter.service;

import com.example.foodcounter.model.Food;
import com.example.foodcounter.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository repository;

    private final Map<Food, Integer> cart = new HashMap<>();

    @Autowired
    public FoodServiceImpl(FoodRepository repository) {
        this.repository = repository;
        Food[] foods = {
                new Food("1", "牛肉盖浇饭", 10.0, 20.0, 30.0, 80.0),
                new Food("2", "明治醇壹450ml", 22.5, 15, 15, 220)
        };
        repository.saveAll(Arrays.asList(foods));
    }

    private Food checkArgument(String id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("food id does not exist"));
    }

    @Override
    public void addProductById(String id, int quantity) {
        Food food = checkArgument(id);
        cart.merge(food, quantity, Integer::sum);
        if (cart.get(food) <= 0) {
            cart.remove(food);
        }
    }

    @Override
    public Iterable<Food> getFoods() {
        return repository.findAll();
    }

    @Override
    public Set<Map.Entry<Food, Integer>> getCart() {
        return cart.entrySet();
    }

    @Override
    public Double getCarbo() {
        return 100.0;
    }

    @Override
    public Double getProtein() {
        return 100.0;
    }

    @Override
    public Double getFat() {
        return 100.0;
    }

    @Override
    public Double getHeat() {
        return 100.0;
    }
}
