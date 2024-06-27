package com.blibli.Order.Controller;

import com.blibli.Order.Dto.*;
import com.blibli.Order.Exceptions.*;
import com.blibli.Order.Model.Cart;
import com.blibli.Order.Model.Order;
import com.blibli.Order.Model.Orders;
import com.blibli.Order.Model.Product;
import com.blibli.Order.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService order_service;

    @PostMapping("/add")
    public String addOrders(@RequestBody List<Cart> ordered_list) {
        return order_service.addOrders(ordered_list,1);
    }
    @GetMapping("/get")
    public ResponseEntity<?> getOrders() {
        List<OrdersDto> allOrders=order_service.getOrders();
        if(allOrders.isEmpty())
        {
            return new ResponseEntity<>("no orders",HttpStatus.OK);
        }
        return new ResponseEntity<>(allOrders,HttpStatus.OK);
    }

    @GetMapping("/get/products")
    public List<Product> getAllProducts() throws ProductNotFoundException
    {
        List<Product> allProduct=order_service.getAllProducts();
        return allProduct;
    }
    @PostMapping("/confirm/{userName}")
    public String confirmOrder(@PathVariable String userName)
    {
        return order_service.confirmOrder(userName);
    }
}