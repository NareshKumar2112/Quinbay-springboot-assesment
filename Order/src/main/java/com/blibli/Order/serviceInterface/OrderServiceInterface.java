package com.blibli.Order.serviceInterface;

import com.blibli.Order.Dto.*;
import com.blibli.Order.Exceptions.*;
import com.blibli.Order.Model.*;

import java.util.*;

public interface OrderServiceInterface {

    public String addOrders(List<Cart> ordered_list, int id);

    public List<OrdersDto> getOrders();

    public List<Product> getAllProducts()throws ProductNotFoundException;

    public Product getProductById(int id);

    public String confirmOrder(String userName);
}
