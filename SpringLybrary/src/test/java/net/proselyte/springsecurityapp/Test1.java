package net.proselyte.springsecurityapp;

import net.proselyte.springsecurityapp.config.jp;
import net.proselyte.springsecurityapp.controller.BookingController;
import net.proselyte.springsecurityapp.dao.DocumentRepository;
import net.proselyte.springsecurityapp.dao.UserRepository;
import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.User;
import net.proselyte.springsecurityapp.service.DocumentServiceC;
import net.proselyte.springsecurityapp.service.UserServiceC;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import javax.annotation.Resource;
import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {jp.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class Test1 {

    @Resource
    private UserServiceC userServiceC;
    private DocumentServiceC documentServiceC;

    @Before
    public void before(){
        User p1 = new User("email1","pass","Sergey","Afonso", "cookieId1",
                "faculty",0,"Via Margutta, 3","30001");
        userServiceC.save(p1);
        User p2 = new User("email2","pass","Nadia", "Teixeira","cookieId2",
                "faculty",0,"Via Sacra, 13","30002");
        userServiceC.save(p2);
        User p3=new User("email3","pass","Elvira", "Espindola","cookieId3",
                "faculty",0,"Via del Corso, 22","30003");
        userServiceC.save(p3);
        User s=new User("emails","pass","Andrey", "Velo","cookieIds",
                "student",0,"Avenida Mazatlan 250","30004");
        userServiceC.save(s);
        User v=new User("emailv","pass","Veronika" ,"Rama","cookieIdv",
                "visiting professor",0,"Stret Atocha, 27","30005");
        userServiceC.save(v);

        Document d1=new Document("Introduction to Algorithms",
                "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest and Clifford Stein",
                "NA",3,"5000","","book",2009,"MIT Press","Third edition");

        documentServiceC.save(d1);

        Document d2=new Document("Design Patterns: Elements of Reusable Object-Oriented Software",
                "Erich Gamma, Ralph Johnson, John Vlissides, Richard Helm",
                "bestseller",3,"1700","","book",2003,
                "Addison-Wesley Professional","First edition");
        documentServiceC.save(d2);

        Document d3=new Document("Null References: The Billion Dollar Mistake",
                "Tony Hoare","",2,"700","","",0,"","");
        documentServiceC.save(d3);

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