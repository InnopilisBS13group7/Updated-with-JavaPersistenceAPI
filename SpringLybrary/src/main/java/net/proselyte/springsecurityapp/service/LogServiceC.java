package net.proselyte.springsecurityapp.service;

import net.proselyte.springsecurityapp.dao.LogRepository;
import net.proselyte.springsecurityapp.model.Log;
import net.proselyte.springsecurityapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class LogServiceC implements LogService{

    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat formatForDate = new SimpleDateFormat("dd.MM.yyyy");
        return formatForDate.format(date);
    }

    @Autowired
    private LogRepository logRepository;

    @Override
    public List<Log> getAllLogs(){
        return logRepository.findAll();
    }

    @Override
    public void save(User user,String s){
        s=user.getStatus()+" "+" "+user.getName()+" "+user.getSurname()+" ("+user.getId()+ ") "+s;
        Log log=new Log(getDate(),s);
        logRepository.save(log);
    }

    @Override
    public void save(String s){
        Log log=new Log(getDate(),s);
        logRepository.save(log);
    }
}
