package net.proselyte.springsecurityapp.service;

import net.proselyte.springsecurityapp.dao.DocumentRepository;
import net.proselyte.springsecurityapp.dao.OrderRepository;
import net.proselyte.springsecurityapp.dao.UserRepository;
import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.Order;
import net.proselyte.springsecurityapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceC implements UserService {


    private UserRepository userRepository;
    private DocumentRepository documentRepository;
    private OrderRepository orderRepository;

    @Autowired
    public UserServiceC(UserRepository userRepository, DocumentRepository documentRepository,OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.orderRepository = orderRepository;
    }


    @Override
    public List<User> getAllusers() {
        return userRepository.findAll();
    }

    @Override
    public User get(int id) {
        return userRepository.getOne(id);
    }

    @Override
    public User get(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public User getByCookie(String cookie) {
        return userRepository.getByCookieId(cookie);
    }

    @Override
    public void add(User user) {
        userRepository.save(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        userRepository.delete(id);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public String checkoutDocument(int documentId, int userId) {
        Date date = new Date();
        Document d = documentRepository.findOne(documentId);
        User u = userRepository.findOne(userId);
        List<Order> orders = orderRepository.findByUserId(u.getId());

        boolean check = false;
        for (Order or : orders) {
            if (or.getItemId() == documentId && (or.getStatus().equals("open") || or.getStatus().equals("queue"))) {
                check = true;
            }
        }
        if (check) return "false";
        if (d == null) return "false";
        int currentAmount = d.getAmount();
        if (d.getStatus().equals("reference")) return "false";
        //-- some conditions
        long keepingTime = 0;
        String userStatus = u.getStatus();
        String documentStatus = d.getStatus();
        if (d.getType().equals("av")) keepingTime = 2*604800000L;
        else if (documentStatus.equals("bestseller")) keepingTime = 1209600000;
        else {
            if (userStatus.equals("disabled") || userStatus.equals("student")) keepingTime = 3*604800000L;
            else keepingTime = 2 * 1728000000L;

        }
        if (userStatus.equals("visitingProfessor")) keepingTime = 604800000L;
        if (userStatus.equals("ta") || userStatus.equals("instructor") || userStatus.equals("professor")) keepingTime = 4*604800000L;
        //------
        if (currentAmount == 0) {
            orderRepository.save(new Order(u.getId(), documentId, date.getTime(), date.getTime()+keepingTime, "queue"));
            return "You are in queue";
        }
        //------
        d.setAmount(d.getAmount() - 1);
        documentRepository.save(d);
        orderRepository.save(new Order(u.getId(), documentId, date.getTime(), (date.getTime() + keepingTime), "open"));
        return "true";
    }

    @Override
    public String renewDocument(int orderId) {
        Order or = orderRepository.findOne(orderId);
        Document d = documentRepository.findOne(or.getItemId());

        if (or.getStatus().equals("renewed")) return "false";
        if (d == null) return "false";

        or.setStatus("renewed");
        Date date = new Date();
        long start = date.getTime();
        or.setFinishTime(start+(or.getFinishTime()-or.getStartTime()));
        or.setStartTime(start);
        orderRepository.save(or);
        return "true";
    }


}
