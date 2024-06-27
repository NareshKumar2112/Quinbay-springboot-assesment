package com.blibli.Order.Service;

import com.blibli.Order.Dto.*;
import com.blibli.Order.Exceptions.*;
import com.blibli.Order.Model.*;
import com.blibli.Order.orderRepository.CartRepository;
import com.blibli.Order.orderRepository.OrderRepository;
import com.blibli.Order.orderRepository.OrdersRepository;
import com.blibli.Order.serviceInterface.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OrderService implements OrderServiceInterface {


    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository order_repository;
    @Autowired
    private OrdersRepository ordersRepository;
    private static final String TOPIC = "ordermessage";

    public String addOrders(List<Cart> ordered_list,int id) {
        List<Cart> confirm_list=new ArrayList<>();
        Order order=new Order();
        double total_amount=0;
        HttpHeaders headers=new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String>entity=new HttpEntity<String>(headers);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("productId",1);
        for(int i=0;i<ordered_list.size();i++)
        {
            long productId=ordered_list.get(i).getOrderedProductId();
            int quantity=ordered_list.get(i).getOrderedProductQuantity();
            Product product=restTemplate.exchange("http://localhost:8080/product/get/"+productId, HttpMethod.GET, entity, Product.class,hashMap).getBody();
            if(product!=null)
            {
                if(quantity>=0 ) {
                    if(quantity<=product.getQuantity()) {
                        total_amount = total_amount + (quantity * product.getCost());
                        ordered_list.get(i).setOrderedProductName(product.getName());
                        ordered_list.get(i).setCostPerProduct(product.getCost());
                        ordered_list.get(i).setTotalProductCost(product.getCost()*quantity);
                        confirm_list.add(ordered_list.get(i));
                        product.setId(product.getId());
                        product.setQuantity(product.getQuantity() - quantity);
                        restTemplate.put("http://localhost:8080/product/update",product, new HashMap<>());
                    }
                    else {
                        return "out of stock";
                    }
                }
                else {
                    return "invalid input";
                }
            }
            else
            {
                return "product is not available";
            }
        }
        order.setOrderedList(confirm_list);
        order.setTotalAmount(total_amount);
        order_repository.save(order);
        return "placed successfully";
    }
    public List<OrdersDto> getOrders()
    {
        List<Orders> ordersList=ordersRepository.findAll();
        List<OrdersDto> ordersDtoList=new ArrayList<>();
        for(int i=0;i<ordersList.size();i++)
        {
            OrdersDto ordersDto=new OrdersDto();
            ordersDto.setShoppingCart(ordersList.get(i).getShoppingCart());
            ordersDto.setTotalAmount(ordersList.get(i).getTotalAmount());
            ordersDtoList.add(ordersDto);
        }
        return ordersDtoList;
    }
    public List<Product> getAllProducts() throws ProductNotFoundException
    {
        Product[] productList=restTemplate.getForObject("http://localhost:8080/product/get",Product[].class);
        if(productList.length==0)
        {
            throw new ProductNotFoundException("No products available");
        }
        return Arrays.asList(productList);
    }
    public Product getProductById(int id)
    {
        Product product=restTemplate.getForObject("http://localhost:8080/product/get/"+id,Product.class);
        if(product!=null)
        {
            return product;
        }
        return null;
    }

    public String confirmOrder(String userName) {
        Optional<ShoppingCart> usershoppingCart=cartRepository.findById(userName);
        if(usershoppingCart.isEmpty())
        {
            return "the cart is empty";
        }
        ShoppingCart shoppingCart=usershoppingCart.get();
        Orders orders=new Orders();
        double totalAmount=0;
        HttpHeaders headers=new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String>entity=new HttpEntity<String>(headers);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("id",1);
        List<Product> orderedList=shoppingCart.getProductList();
        for(int i=0;i<orderedList.size();i++)
        {
            System.out.println("fvfvf");
            long productId=orderedList.get(i).getId();
            int quantity=orderedList.get(i).getQuantity();
            Product product=restTemplate.exchange("http://localhost:8080/product/get/"+productId, HttpMethod.GET, entity, Product.class,hashMap).getBody();
            if(product!=null)
            {
                if(quantity>=0 ) {
                    if(quantity<=product.getQuantity()) {
                        totalAmount = totalAmount + (quantity * product.getCost());
                        product.setQuantity(product.getQuantity()-quantity);
                        product.setId(productId);

                        System.out.println(product.getId()+product.getName()+" "+product.getQuantity()+" "+product.getSeller_id()+product.getCategory_id());
                        restTemplate.put("http://localhost:8080/product/update",product, new HashMap<>());
                        System.out.println("fee");
                    }
                    else {
                        return product.getName()+" is out of stock";
                    }
                }
                else {
                    return "invalid input";
                }
            }
            else
            {
                return "product is not available";
            }

        }
        orders.setTotalAmount(totalAmount);
        orders.setShoppingCart(shoppingCart);
        ordersRepository.save(orders);
        kafkaTemplate.send(TOPIC,"order palced successfully");
//        cartRepository.deleteById(userName);
        return"order is placed successfully";
    }
}
