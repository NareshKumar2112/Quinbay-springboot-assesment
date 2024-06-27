package com.blibli.Order.serviceInterface;

import com.blibli.Order.Model.*;
import org.springframework.http.*;

public interface ShoppingCartServiceInterface {

    public String addToCart(ShoppingCart shoppingCart);

    public String deleteCart(String userName);

    public ResponseEntity<?> getShoppingCartById(String userName);

    public String updateCart(ShoppingCart shoppingCart);

}
