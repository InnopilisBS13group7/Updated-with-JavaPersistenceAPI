package net.proselyte.springsecurityapp.service;

import net.proselyte.springsecurityapp.model.Log;
import net.proselyte.springsecurityapp.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LogService {
    public List<Log> getAllLogs();
    public void save(User user, String s);
    public void save(String s);

}
