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
    public boolean add(User librarian, User NewuUser);
    public void add(User NewUser);
    public void save(User NewuUser);
    public boolean save(User librarian, User NewUser);
    public void delete(int id);
    public boolean delete(User librarian,int id);
    public void delete(User NewUser);
    public boolean delete(User librarian, User NewUser);

    public String checkoutDocument(int documentId, int userId);

    public String renewDocument(int orderId);



}
