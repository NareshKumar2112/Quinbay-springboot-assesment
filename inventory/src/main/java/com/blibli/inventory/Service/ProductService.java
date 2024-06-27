package com.blibli.inventory.Service;

import com.blibli.inventory.Dto.*;
import com.blibli.inventory.Model.*;
import com.blibli.inventory.Repository.*;
import com.blibli.inventory.serviceInterface.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.kafka.annotation.*;
import org.springframework.kafka.core.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements ProductServiceInterface {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;
    @Autowired
    private ProductRepository product_repository;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private CategoryService categoryService;
    private static final String TOPIC = "mytopic";

    public String addProduct(Product product)
    {
        Category category=categoryService.getCategoryById(product.getCategory().getCategory_id()).get();
        Seller seller=sellerService.getSellerById(product.getSeller().getSeller_id());
        product.setCategory(category);
        product.setSeller(seller);
        if(seller!=null && product.getName()!=null && product.getName().length()>2 && product.getCost()>0 && product.getQuantity()>0) {
            Product product_added = product_repository.save(product);
            if (product_added == null) {
                return "product is not added";
            }
            return "added successfully";
        }
        else
        {
            return "invalid inputs";
        }
    }
    public List<ProductDto> getProducts()
    {
        List<Product> all_products =product_repository.findAll();
        List<ProductDto> productDtoList=new ArrayList<>();
        for(int i=0;i<all_products.size();i++)
        {
            Product product=all_products.get(i);
            ProductDto productDto=new ProductDto();
            productDto.setName(product.getName());
            productDto.setCost(product.getCost());
            productDto.setQuantity(product.getQuantity());
            productDto.setCategory_id(product.getCategory().getCategory_id());
            productDto.setSeller_id(product.getSeller().getSeller_id());
            productDtoList.add(productDto);
        }
        return productDtoList;
    }
    public String updateProduct(Product product)
    {
        if(getProductById(product.getId())!=null) {
            if (product.getName() != null && product.getName().length() > 2 && product.getCost() > 0 && product.getQuantity() > 0) {
                ProductDto newProduct=getProductById(product.getId());
                LocalDate currentDate = LocalDate.now();
                if(!newProduct.getName().equals(product.getName()))
                {
                    ProductHistory productHistory=new ProductHistory();
                    productHistory.setProductId(product.getId());
                    productHistory.setColumnName("product name");
                    productHistory.setOldValue(newProduct.getName());
                    productHistory.setNewValue(product.getName());
                    productHistory.setDate(currentDate+"");
                    productHistoryRepository.save(productHistory);
                }
                if(newProduct.getQuantity()!=product.getQuantity())
                {
                    ProductHistory productHistory=new ProductHistory();
                    productHistory.setProductId(product.getId());
                    productHistory.setColumnName("product quantity");
                    productHistory.setOldValue(newProduct.getQuantity()+"");
                    productHistory.setNewValue(product.getQuantity()+"");
                    productHistory.setDate(currentDate+"");
                    productHistoryRepository.save(productHistory);
                }
                if(newProduct.getCost()!=product.getCost())
                {
                    ProductHistory productHistory=new ProductHistory();
                    productHistory.setProductId(product.getId());
                    productHistory.setColumnName("product cost");
                    productHistory.setOldValue(newProduct.getCost()+"");
                    productHistory.setNewValue(product.getCost()+"");
                    productHistory.setDate(currentDate+"");
                    productHistoryRepository.save(productHistory);
                }
                System.out.println(product.getId());
                Product product1=getproductbyid(product.getId());
                product.setSeller(sellerService.getSellerById(product1.getSeller().getSeller_id()));
                product.setCategory(categoryService.getCategoryById(product1.getCategory().getCategory_id()).get());
                Product update_product = product_repository.saveAndFlush(product);
                if (update_product == null) {
                    return "update failed";
                }
                return "updated successfully";
            } else {
                return "invalid input";
            }
        }
        else {
            return "product is not available";
        }
    }
    public String deleteProduct(long id)
    {
        try {
            Optional<Product> product=product_repository.findById(id);
            if(product.isEmpty())
            {
                return "product is not available";
            }
            product_repository.deleteById(id);
        }catch(Exception e)
        {
            return "deletion failed";
        }
        return "deleted successfully";
    }

    @KafkaListener(topics = "ordermessage",groupId = "inventory-group")
    public void handlekafkaOrder(String message)
    {
        System.out.println(message);
    }
    public Product getproductbyid(long id)
    {
        return product_repository.findById(id).get();
    }
    public ProductDto getProductById(long id) {

        List<Product> all_products =product_repository.findAll();
        for(int i=0;i<all_products.size();i++)
        {
            ProductDto product=new ProductDto();
            long pid=all_products.get(i).getId();
            if(id==pid)
            {
                product.setName(all_products.get(i).getName());
                product.setCost(all_products.get(i).getCost());
                product.setQuantity(all_products.get(i).getQuantity());
                product.setCategory_id(all_products.get(i).getCategory().getCategory_id());
                product.setSeller_id(all_products.get(i).getSeller().getSeller_id());
                return product;
            }
        }
        return null;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC,  message);
    }

    @Cacheable(value = "springboot",key = "#key")
    public String rediscache(String key,String value)
    {
        System.out.println(key);
        return value;
    }
}