package com.example.foodcounter.controller;

import com.example.foodcounter.service.FoodService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("/fillData")
    public String fillDataFromXlsx() {
        foodService.fillDataFromXlsx();
        return "redirect:/";
    }
}
