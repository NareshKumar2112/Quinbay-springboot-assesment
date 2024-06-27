package com.blibli.inventory.Service;

import com.blibli.inventory.Model.*;
import com.blibli.inventory.Repository.*;
import com.blibli.inventory.serviceInterface.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;
@Service
public class ProductHistoryService implements ProductHistoryServiceInterface {

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    public List<ProductHistory> getProductHistory() {
        return productHistoryRepository.findAll();
    }
}
