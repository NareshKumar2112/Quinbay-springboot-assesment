package com.blibli.inventory.Controller;

import com.blibli.inventory.Dto.*;
import com.blibli.inventory.Model.Product;
import com.blibli.inventory.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService product_service;

    @GetMapping("/get")
    public List<ProductDto> getProductName()
    {
        List<ProductDto>productList=product_service.getProducts();
        return productList;
    }
    @PostMapping("/post")
    public String postProduct(@RequestBody Product product)
    {
        System.out.println("hke");
        return product_service.addProduct(product);
    }
    @PutMapping("/update")
    public String updateProduct(@RequestBody Product product)
    {
        return product_service.updateProduct(product);

    }
    @GetMapping("/kget")
    public String getKafkaData(@RequestParam String message) {
        product_service.sendMessage(message);
        return "Message sent to Kafka successfully!";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable long id)
    {
            return product_service.deleteProduct(id);
    }

    @PostMapping("/redis")
    public String getredis(@RequestParam String key,@RequestParam String value)
    {
        System.out.println("ffe");
        return  product_service.rediscache(key,value);
    }

    @GetMapping("/get/{id}")
    public ProductDto getProductById(@PathVariable long id)
    {
        ProductDto optionalProduct=product_service.getProductById(id);
        if(optionalProduct==null)
        {
            return null;
        }
        return optionalProduct;
    }
}