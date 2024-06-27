package com.blibli.inventory.serviceInterface;

import com.blibli.inventory.Dto.*;
import com.blibli.inventory.Model.*;

import java.util.*;

public interface ProductServiceInterface {

    public String addProduct(Product product);

    public List<ProductDto> getProducts();

    public String updateProduct(Product product);

    public String deleteProduct(long id);

    public Product getproductbyid(long id);

    public ProductDto getProductById(long id);

}
