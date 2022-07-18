package com.example.foodcalculator.repository;

import com.example.foodcalculator.model.Food;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends PagingAndSortingRepository<Food, String> {

    Optional<Food> findById(String id);

    Iterable<Food> findFoodsByNameLike(String name);

    Iterable<Food> findAll();
}
