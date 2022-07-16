package com.example.foodcounter.service;

import com.example.foodcounter.model.Food;
import com.example.foodcounter.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.io.File;
import java.io.FileInputStream;
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
    public void removeProductById(String id) {
        Food food = checkArgument(id);
        cart.remove(food);
    }

    @Override
    public void fillDataFromXlsx() {
        try {
            URL resource = this.getClass().getResource("/data/foods.xlsx");
            assert resource != null;
            File file = new File(resource.toURI());
            FileInputStream excelFile = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            for (Row row : datatypeSheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        System.out.print(cell.getStringCellValue() + "--");
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        System.out.print(cell.getNumericCellValue() + "--");
                    }
                }
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
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
        double sum = 0;
        for (var item : cart.entrySet()) {
            sum += item.getValue() * item.getKey().getCarbo();
        }
        return sum;
    }

    @Override
    public Double getProtein() {
        double sum = 0;
        for (var item : cart.entrySet()) {
            sum += item.getValue() * item.getKey().getProtein();
        }
        return sum;
    }

    @Override
    public Double getFat() {
        double sum = 0;
        for (var item : cart.entrySet()) {
            sum += item.getValue() * item.getKey().getFat();
        }
        return sum;
    }

    @Override
    public Double getHeat() {
        double sum = 0;
        for (var item : cart.entrySet()) {
            sum += item.getValue() * item.getKey().getHeat();
        }
        return sum;
    }
}
