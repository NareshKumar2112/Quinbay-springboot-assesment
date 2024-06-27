package com.blibli.inventory.Controller;

import com.blibli.inventory.Dto.*;
import com.blibli.inventory.Model.*;
import com.blibli.inventory.Service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public String addCategory(@RequestBody Category category)
    {
        return categoryService.addCategory(category);
    }
    @GetMapping("/get")
    public List<CategoryDto> getAllCategory()
    {
        return categoryService.getAllCategory();
    }
}
