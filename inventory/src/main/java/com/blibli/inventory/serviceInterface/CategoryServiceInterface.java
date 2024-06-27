package com.blibli.inventory.serviceInterface;

import com.blibli.inventory.Dto.*;
import com.blibli.inventory.Model.*;

import java.util.*;

public interface CategoryServiceInterface {

    public String addCategory(Category category);

    public List<CategoryDto> getAllCategory();

    public Optional<Category> getCategoryById(long id);

}
