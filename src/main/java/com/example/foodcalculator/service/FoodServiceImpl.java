package com.example.foodcalculator.service;

import com.example.foodcalculator.model.Food;
import com.example.foodcalculator.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository repository;

    private final Map<Food, Integer> cart = new HashMap<>();

    @Autowired
    public FoodServiceImpl(FoodRepository repository) throws URISyntaxException {
        this.repository = repository;
        fillDefaultData();
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

    private void fillDefaultData() throws URISyntaxException {
        URL resource = this.getClass().getResource("/data/foods.xlsx");
        assert resource != null;
        File file = new File(resource.toURI());
//      File file = new File("D:/Documents/Exercise-Docs/foods.xlsx");
        fillDataFromXlsx(file);
    }

    @Override
    public void fillDataFromXlsx(File file) {
        FileInputStream fileInputStream = null;
        try {
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
    public void uploadDataFromXlsx(MultipartFile file) {
        // normalize the file path
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // save the file on the local file system
        try {
            String UPLOAD_DIR = "uploads/";
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            fillDataFromXlsx(path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<Food> searchFoodByName(String keyword) {
        // 一定要加 % xx % 以符合 jpa 标准
        return repository.findFoodsByNameLike("%"+keyword+"%");
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
