package com.teamtreehouse.giflib.web.controller;

import com.teamtreehouse.giflib.model.Category;
import com.teamtreehouse.giflib.service.CategoryService;
import com.teamtreehouse.giflib.web.Color;
import com.teamtreehouse.giflib.web.FlashMessage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // Index of all categories
    @RequestMapping("/categories")
    @SuppressWarnings("unchecked")
    public String listCategories(Model model) {
        // TODO: Get all categories
        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories",categories);
        return "category/index";
    }

    // Single category page
    @RequestMapping("/categories/{categoryId}")
    public String category(@PathVariable Long categoryId, Model model) {
        // TODO: Get the category given by categoryId
        Category category = categoryService.findById(categoryId);

        model.addAttribute("category", category);
        return "category/details";
    }

    // Form for adding a new category
    @RequestMapping("categories/add")
    public String formNewCategory(Model model) {
        // TODO: Add model attributes needed for new form
        if (!model.containsAttribute("category")){
            model.addAttribute("category", new Category());
        }
        model.addAttribute("colors", Color.values());

        //form attributes
        model.addAttribute("action", String.format("/categories"));
        model.addAttribute("heading", "New Category");
        model.addAttribute("submit", "Add");

        return "category/form";
    }

    // Form for editing an existing category
    @RequestMapping("categories/{categoryId}/edit")
    public String formEditCategory(@PathVariable Long categoryId, Model model) {
        // TODO: Add model attributes needed for edit form
        if (!model.containsAttribute("category")){
            model.addAttribute("category", categoryService.findById(categoryId));
        }
        model.addAttribute("colors", Color.values());

        //form attributes
        model.addAttribute("action", String.format("/categories/%s", categoryId));
        model.addAttribute("heading", "Edit Category");
        model.addAttribute("submit", "Update");

        return "category/form";
    }

    // Update an existing category
    @RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.POST)
    public String updateCategory(@Valid Category category, BindingResult result, RedirectAttributes redirectAttributes) {
        // TODO: Add category if valid data was received
        if (result.hasErrors()){
            //include validation errors on redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);

            //update category if invalid was received
            redirectAttributes.addFlashAttribute("category", category);

            //redirect back to form
            return String.format("redirect:/categories/%s/edit", category.getId());
        }
        categoryService.save(category);

        //flash message for success
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Category updated!", FlashMessage.Status.SUCCESS));

        // TODO: Redirect browser to /categories
        return "redirect:/categories";
    }

    // Add a category
    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public String addCategory(@Valid Category category, BindingResult result, RedirectAttributes redirectAttributes) {
        // TODO: Add category if valid data was received
        if (result.hasErrors()){
            //include validation errors on redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);

            //add category if invalid was received
            redirectAttributes.addFlashAttribute("category", category);

            //redirect back to form
            return "redirect:/categories/add";
        }
        categoryService.save(category);

        //flash message for success
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Category added!", FlashMessage.Status.SUCCESS));

        // TODO: Redirect browser to /categories
        return "redirect:/categories";
    }

    // Delete an existing category
    @RequestMapping(value = "/categories/{categoryId}/delete", method = RequestMethod.POST)
    public String deleteCategory(@PathVariable Long categoryId, RedirectAttributes redirectAttributes) {
        Category cat = categoryService.findById(categoryId);

        // TODO: Delete category if it contains no GIFs
        if (cat.getGifs().size() > 0){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Cannot delete category containing gifs.", FlashMessage.Status.FAILURE));
            return String.format("redirect:/categories/%s/edit", categoryId);
        }
        categoryService.delete(cat);

        // TODO: Redirect browser to /categories
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Category deleted!", FlashMessage.Status.SUCCESS));
        return "redirect:/categories";
    }
}
