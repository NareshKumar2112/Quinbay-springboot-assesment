package com.blibli.Order.Dto;

import com.blibli.Order.Model.*;
import lombok.*;

@Data
public class OrdersDto {

    private ShoppingCart shoppingCart;
    private double totalAmount;

}
