package com.blibli.Order.orderRepository;

import com.blibli.Order.Model.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<ShoppingCart,String> {

}
