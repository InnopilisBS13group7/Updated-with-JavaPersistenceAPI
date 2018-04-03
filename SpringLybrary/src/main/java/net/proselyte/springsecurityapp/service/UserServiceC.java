package net.proselyte.springsecurityapp.service;

import net.proselyte.springsecurityapp.dao.UserRepository;
import net.proselyte.springsecurityapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceC implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceC(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
