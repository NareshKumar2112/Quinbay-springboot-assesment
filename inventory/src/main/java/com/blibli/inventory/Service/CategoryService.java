package com.blibli.inventory.Service;

import com.blibli.inventory.Dto.*;
import com.blibli.inventory.Model.*;
import com.blibli.inventory.Repository.*;
import com.blibli.inventory.serviceInterface.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class CategoryService implements CategoryServiceInterface {

    @Autowired
    private CategoryRepository categoryRepository;

    public String addCategory(Category category) {
        if(categoryRepository.save(category)==null)
        {
            return "not added";
        }
        return "added successfully";
    }
    public List<CategoryDto> getAllCategory()
    {
        List<Category> categoryList=categoryRepository.findAll();
        List<CategoryDto> categoryDtosList=new ArrayList<>();
        for(int i=0;i<categoryList.size();i++)
        {
            CategoryDto categoryDto=new CategoryDto();
            categoryDto.setName(categoryList.get(i).getName());
            categoryDto.setProductList(categoryList.get(i).getProductList());
            categoryDtosList.add(categoryDto);
        }
        return categoryDtosList;
    }
    public Optional<Category> getCategoryById(long id)
    {
        return categoryRepository.findById(id);
    }
}
