package com.blibli.Order.orderRepository;

import com.blibli.Order.Model.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrdersRepository extends MongoRepository<Orders,String> {
}
