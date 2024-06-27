package com.blibli.Order.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "shoppingCart")
public class ShoppingCart {

    @Id
    private String userName;
    private List<Product> productList;
}
