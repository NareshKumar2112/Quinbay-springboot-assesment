package com.blibli.inventory.Dto;

import com.blibli.inventory.Model.*;
import lombok.*;

import java.util.*;

@Data
public class CategoryDto {

    private String name;
    private List<Product> productList;

}
