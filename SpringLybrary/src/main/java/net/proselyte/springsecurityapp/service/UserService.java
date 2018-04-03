package net.proselyte.springsecurityapp.service;


import net.proselyte.springsecurityapp.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface UserService {
    public List<User> getAllusers();
    public User get(int id);

    public User get(String email);
    public User getByCookie(String cookie);
    public void add(User user);
    public void save(User user);
    public void delete(int id);
    public void delete(User user);

    public String checkoutDocument(int documentId, int userId);

    public String renewDocument(int orderId);



}
