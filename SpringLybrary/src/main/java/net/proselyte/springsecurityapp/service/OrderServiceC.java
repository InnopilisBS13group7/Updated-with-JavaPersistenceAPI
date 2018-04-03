package net.proselyte.springsecurityapp.service;


import net.proselyte.springsecurityapp.dao.DocumentRepository;
import net.proselyte.springsecurityapp.dao.OrderRepository;
import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.Order;
import net.proselyte.springsecurityapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceC implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DocumentRepository documentRepository;

    public List<Order> getAllOrders() {
        return (List<Order>)orderRepository.findAll();
    }

    @Override
    public List<Order> getAllFinishedOrders() {
        return orderRepository.findByStatus("finished");
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByUserIdAndItemId(int userId, int itemid) {
        return orderRepository.findByUserIdAndItemId(userId,itemid);
    }

    @Override
    public List<Order> getOrdersByUserAndStatus(User u, String status) {
        return orderRepository.findByUserIdAndStatus(u.getId(),status);
    }

    @Override
    public List<Order> getOpenOrdersByUserId(int userId) {
        return orderRepository.findByUserIdAndStatus(userId,"open");
    }

    @Override
    public List<Order> getQueue(int documentId) {
        return orderRepository.findAllByItemIdAndStatus(documentId,"queue");
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void delete(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public Order get(int id) {
        return orderRepository.getOne(id);
    }

}
