package com.example.foodcalculator.service;

import com.example.foodcalculator.model.Food;
import com.example.foodcalculator.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
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
        fillDataFromXlsx();
//        Food[] foods = {
//                new Food("-1", "牛肉盖浇饭", 10.0, 20.0, 30.0, 80.0),
//                new Food("-2", "明治醇壹450ml", 22.5, 15, 15, 220)
//        };
//        repository.saveAll(Arrays.asList(foods));
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
    public void removeAll() {
        cart.clear();
    }

    @Override
    public void fillDataFromXlsx() {
        FileInputStream fileInputStream = null;
        try {
//            URL resource = this.getClass().getResource("/data/foods.xlsx");
//            assert resource != null;
//            File file = new File(resource.toURI());
            File file = new File("D:/Documents/Exercise-Docs/foods.xlsx");
            fileInputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = 0;
            for (Row row : sheet) {
                if (rowNum == 0) {
                    rowNum++;
                    continue;
                }
                String id = row.getCell(0).getCellType() == CellType.STRING ?
                        row.getCell(0).getStringCellValue() : String.valueOf(row.getCell(0).getNumericCellValue());
                String name = row.getCell(1).getStringCellValue();
                double carbo = row.getCell(2).getNumericCellValue();
                double protein = row.getCell(3).getNumericCellValue();
                double fat = row.getCell(4).getNumericCellValue();
                double heat = row.getCell(5).getNumericCellValue();
                Food food = new Food(id, name, carbo, protein, fat, heat);
                repository.save(food);
                //System.out.println(food);
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (Exception e) {
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
