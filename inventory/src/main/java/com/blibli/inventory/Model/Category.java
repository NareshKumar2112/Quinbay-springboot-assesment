package com.blibli.inventory.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long category_id;
    private String name;
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonManagedReference("category")
    private List<Product> productList=new ArrayList<>();
}
