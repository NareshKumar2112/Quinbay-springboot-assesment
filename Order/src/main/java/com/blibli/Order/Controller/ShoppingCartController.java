package com.blibli.Order.Controller;

import com.blibli.Order.Model.ShoppingCart;
import com.blibli.Order.Service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    @PostMapping("/add")
    public String addToCart(@RequestBody ShoppingCart shoppingCart)
    {
        return cartService.addToCart(shoppingCart);
    }
    @DeleteMapping("/delete")
    public String deleteCart(String userName)
    {
        return cartService.deleteCart(userName);
    }
    @GetMapping("/get/{userName}")
    public ResponseEntity<?> getShppingCart(@PathVariable String userName)
    {
        return cartService.getShoppingCartById(userName);
    }
    @PutMapping("/update")
    public String updateCart(@RequestBody ShoppingCart shoppingCart)
    {
        return cartService.updateCart(shoppingCart);
    }
}
