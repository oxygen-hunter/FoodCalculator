package com.example.foodcounter.repository;

import com.example.foodcounter.model.Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends PagingAndSortingRepository<Food, String> {

    Optional<Food> findById(String id);

    Iterable<Food> findAll();
}
