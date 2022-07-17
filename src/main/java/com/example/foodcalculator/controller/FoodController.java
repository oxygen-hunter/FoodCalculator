package com.example.foodcalculator.controller;

import com.example.foodcalculator.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class FoodController {

    private FoodService foodService;

    @Autowired
    public void setFoodService(FoodService foodService) {
        this.foodService = foodService;
    }

    @ModelAttribute
    public void fillData(Model model) {
        model.addAttribute("foods", foodService.getFoods());
        model.addAttribute("cart", foodService.getCart());
        model.addAttribute("carbo", foodService.getCarbo());
        model.addAttribute("protein", foodService.getProtein());
        model.addAttribute("fat", foodService.getFat());
        model.addAttribute("heat", foodService.getHeat());
    }

    @GetMapping("/")
    public String counter(Model model, HttpServletRequest request) {
        request.getSession();
        return "index";
    }

    @GetMapping("/add")
    public String addById(@RequestParam(name = "id") String id) {
        try {
            foodService.addProductById(id, 1);
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/sub")
    public String subById(@RequestParam(name = "id") String id) {
        try {
            foodService.addProductById(id, -1);
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/remove")
    public String removeById(@RequestParam(name = "id") String id) {
        try {
            foodService.removeProductById(id);
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/removeAll")
    public String removeAll() {
        foodService.removeAll();
        return "redirect:/";
    }

    @GetMapping("/fillData")
    public String fillDataFromXlsx() throws URISyntaxException {
        URL resource = this.getClass().getResource("/data/foods.xlsx");
        assert resource != null;
        File file = new File(resource.toURI());
        foodService.fillDataFromXlsx(file);
        return "redirect:/";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }
        // handle file
        foodService.uploadDataFromXlsx(file);
        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded " + file.getName() + '!');
        return "redirect:/";
    }

}
