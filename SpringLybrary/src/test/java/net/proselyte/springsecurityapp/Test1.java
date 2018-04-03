package net.proselyte.springsecurityapp;

import net.proselyte.springsecurityapp.config.jp;
import net.proselyte.springsecurityapp.controller.BookingController;
import net.proselyte.springsecurityapp.controller.Controller;
import net.proselyte.springsecurityapp.dao.DocumentRepository;
import net.proselyte.springsecurityapp.dao.UserRepository;
import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.User;
import net.proselyte.springsecurityapp.service.DocumentServiceC;
import net.proselyte.springsecurityapp.service.UserService;
import net.proselyte.springsecurityapp.service.UserServiceC;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {jp.class,UserServiceC.class,DocumentServiceC.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
@WebAppConfiguration
public class Test1 {

    @Autowired
    protected UserServiceC userService;
    @Autowired
    protected DocumentServiceC documentService;
    @Resource
    private UserRepository userRepository;

    @Before
    public void before(){
        User p1 = new User("email1","pass","Sergey","Afonso", "cookieId1",
                "faculty",0,"Via Margutta, 3","30001");
        userService.save(p1);
        User p2 = new User("email2","pass","Nadia", "Teixeira","cookieId2",
                "faculty",0,"Via Sacra, 13","30002");
        userService.save(p2);
        User p3=new User("email3","pass","Elvira", "Espindola","cookieId3",
                "faculty",0,"Via del Corso, 22","30003");
        userService.save(p3);
        User s=new User("emails","pass","Andrey", "Velo","cookieIds",
                "student",0,"Avenida Mazatlan 250","30004");
        userService.save(s);
        User v=new User("emailv","pass","Veronika" ,"Rama","cookieIdv",
                "visiting professor",0,"Stret Atocha, 27","30005");
        userService.save(v);

        Document d1=new Document("Introduction to Algorithms",
                "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest and Clifford Stein",
                "NA",3,"5000","","book",2009,"MIT Press","Third edition");

        documentService.save(d1);

        Document d2=new Document("Design Patterns: Elements of Reusable Object-Oriented Software",
                "Erich Gamma, Ralph Johnson, John Vlissides, Richard Helm",
                "bestseller",3,"1700","","book",2003,
                "Addison-Wesley Professional","First edition");
        documentService.save(d2);

        Document d3=new Document("Null References: The Billion Dollar Mistake",
                "Tony Hoare","",2,"700","","",0,"","");
        documentService.save(d3);
        System.out.println(documentService.getAllDocuments());

    }

    @org.junit.Test
    public void t1() {

    }

    @org.junit.Test
    public void t2(){

    }



   /* @org.springframework.context.annotation.Configuration
    public static class ContextConfiguration {
    }
    */
}