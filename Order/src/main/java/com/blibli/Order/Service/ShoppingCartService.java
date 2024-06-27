package com.blibli.Order.Service;

import com.blibli.Order.Model.Product;
import com.blibli.Order.Model.ShoppingCart;
import com.blibli.Order.orderRepository.CartRepository;
import com.blibli.Order.serviceInterface.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService implements ShoppingCartServiceInterface {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderService orderService;

    public String addToCart(ShoppingCart shoppingCart) {
        for(int i=0;i<shoppingCart.getProductList().size();i++)
        {
            int id= (int) shoppingCart.getProductList().get(i).getId();
            Product product=orderService.getProductById(id);
            if(product==null)
            {
                return "product id "+id+" is not available";
            }
            shoppingCart.getProductList().get(i).setCost(product.getCost());
            shoppingCart.getProductList().get(i).setName(product.getName());
        }
        Optional<ShoppingCart> userCart=cartRepository.findById(shoppingCart.getUserName());
        if(userCart.isEmpty()) {
            ShoppingCart shoppingCart1 = cartRepository.save(shoppingCart);
            if (shoppingCart1 == null) {
                return "add to cart is failed";
            }
            return "successfully added";
        }
        else {
            ShoppingCart oldCartList=userCart.get();
            for(int i=0;i<shoppingCart.getProductList().size();i++)
            {
                int newId= (int) shoppingCart.getProductList().get(i).getId();
                boolean flag=true;
                for(int j=0;j<oldCartList.getProductList().size();j++)
                {
                    int oldId=(int) oldCartList.getProductList().get(j).getId();
                    int newQuantity=oldCartList.getProductList().get(j).getQuantity();
                    if(newId==oldId)
                    {
                        int oldQuantity=oldCartList.getProductList().get(j).getQuantity();
                        oldCartList.getProductList().get(j).setQuantity(newQuantity+oldQuantity);
                        flag=false;
                    }
                }
                if(flag) {
                    oldCartList.getProductList().add(shoppingCart.getProductList().get(i));
                }
            }
                ShoppingCart shoppingCart1 = cartRepository.save(oldCartList);
                if (shoppingCart1 == null) {
                    return "add to cart is failed";
                }
                return "successfully added";
        }
    }
    public String deleteCart(String userName) {
        try
        {
            cartRepository.deleteById(userName);
        }
        catch(Exception exception)
        {
            return "deletion failed";
        }
        return "deleted successfully";
    }
    public ResponseEntity<?> getShoppingCartById(String userName)
    {
        Optional<ShoppingCart> shoppingCart=cartRepository.findById(userName);
        if(shoppingCart.isEmpty())
        {
            return new ResponseEntity<>("The cart is empty",HttpStatus.OK);
        }
        return new ResponseEntity<>(shoppingCart,HttpStatus.OK);
    }
    public String updateCart(ShoppingCart shoppingCart)
    {

        for(int i=0;i<shoppingCart.getProductList().size();i++) {

            int id= (int) shoppingCart.getProductList().get(i).getId();
            Product product=orderService.getProductById(id);
            if(product==null)
            {
                return "product id "+id+" is not available";
            }
            shoppingCart.getProductList().get(i).setCost(product.getCost());
            shoppingCart.getProductList().get(i).setName(product.getName());
        }
        ShoppingCart shoppingCartResult=cartRepository.save(shoppingCart);
        if(shoppingCartResult==null)
        {
            return "update failed";
        }
        return "update successfully";
    }
}
