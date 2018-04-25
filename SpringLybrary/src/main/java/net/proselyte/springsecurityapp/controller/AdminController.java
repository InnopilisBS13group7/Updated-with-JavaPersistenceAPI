package net.proselyte.springsecurityapp.controller;

import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.Order;
import net.proselyte.springsecurityapp.model.User;
import net.proselyte.springsecurityapp.service.LogServiceC;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.print.Doc;
import javax.servlet.http.Cookie;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AdminController extends Controller {

    @Autowired
    protected LogServiceC logServiceC;

    /**
     * adding book
     * @param title
     * @param author
     * @param publisher
     * @param description
     * @param year
     * @param status
     * @param edition
     * @return "true" string
     */
    @RequestMapping(value = "/addDocument", method = RequestMethod.POST)
    public String addDocument(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                              @RequestParam(value = "title", required = false, defaultValue = "Not found") String title,
                              @RequestParam(value = "author", required = false, defaultValue = "Not found") String author,
                              @RequestParam(value = "publisher", required = false, defaultValue = "Not found") String publisher,
                              @RequestParam(value = "note", required = false, defaultValue = "Not found") String description,
                              @RequestParam(value = "year", required = false, defaultValue = "0") String year,
                              @RequestParam(value = "status", required = false, defaultValue = "Not found") String status,
                              @RequestParam(value = "number", required = false, defaultValue = "0") String number,
                              @RequestParam(value = "edition", required = false, defaultValue = "Not found") String edition){
        Document d = new Document(title,author,status, Integer.parseInt(number),description,"#","book",Integer.parseInt(year),publisher,edition,100);
        User u=userService.getByCookie(cookieUserCode.getValue());
        if(u.getStatus().equals("admin")||u.getStatus().equals("lib2")||u.getStatus().equals("lib3")){
            documentService.save(d);
            logServiceC.save(u,"added document "+" "+title+" "+author+ " (" + d.getId()+")");
            return "true";
        }
        return "false";
    }

    /**
     * adding Auidio/video document
     * @param title
     * @param author
     * @return "true" string
     */
    @RequestMapping(value = "/addAV", method = RequestMethod.POST)
    public String addDocument(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                              @RequestParam(value = "title", required = false, defaultValue = "Not found") String title,
                              @RequestParam(value = "number", required = false, defaultValue = "0") String number,
                              @RequestParam(value = "author", required = false, defaultValue = "Not found") String author) {
        User u=userService.getByCookie(cookieUserCode.getValue());
        Document d = new Document(title,author,"AV",Integer.parseInt(number),"none","#","av",0,"none","none",100);
        if(u.getStatus().equals("admin")||u.getStatus().equals("lib2")||u.getStatus().equals("lib3")) {
            logServiceC.save(u, "added AV document " + " " + title + " " + author + " (" + d.getId()+")");
            documentService.save(d);
            return "true";
        }
        return "false";
    }

    /**
     * setting new parameters to document
     * @param id
     * @param title
     * @param author
     * @param publisher
     * @param description
     * @param edition
     * @param year
     * @return "false" if does not exist such book, otherwise "true"
     */
    @RequestMapping(value = "/modifyDocument", method = RequestMethod.POST)
    public String modifyDocument(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                                 @RequestParam(value = "documentId") String id,
                                 @RequestParam(value = "title") String title,
                                 @RequestParam(value = "author") String author,
                                 @RequestParam(value = "publisher") String publisher,
                                 @RequestParam(value = "note", required = false, defaultValue = "Not_found") String description,
                                 @RequestParam(value = "edition", required = false, defaultValue = "Not_found") String edition,
                                 @RequestParam(value = "year", required = false, defaultValue = "Not_found") String year){
        User u=userService.getByCookie(cookieUserCode.getValue());
        Document d = documentService.get(Integer.parseInt(id));
        if (d ==null) return "false";//does not exist such book
        logServiceC.save(u, "modified document " + d.getTitle() + " " + d.getAuthor() + " (" + d.getId()+")");
        d.setTitle(title);
        d.setAuthor(author);
        d.setPublisher(publisher);
        d.setDescription(description);
        d.setEdition(edition);
        d.setYear(Integer.parseInt(year));

        documentService.save(d);
        return "true";
    }

    /**
     * add new user to the system
     * @param password
     * @param name
     * @param email
     * @param surname
     * @param status
     * @return if already exists - "false", otherwise "true"
     */
    @RequestMapping(value = "/addNewUser", method = RequestMethod.POST)
    public String addNewuser(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                             @RequestParam(value = "password", required = false, defaultValue = "No password") String password,
                             @RequestParam(value = "name", required = false, defaultValue = "No name") String name,
                             @RequestParam(value = "email", required = false, defaultValue = "No email") String email,
                             @RequestParam(value = "surname", required = false, defaultValue = "No surname") String surname,
                             @RequestParam(value = "status", required = false, defaultValue = "No status") String status) {
        User u=userService.getByCookie(cookieUserCode.getValue());
        boolean check = addNewUserToTheSystem(name,surname,email,password,status);
        if(check) logServiceC.save(u,"added user "+" "+name+" "+surname);
        return check?"true":"false";
    }

    /**
     *setting new parameters to user
     * @param id
     * @param name
     * @param address
     * @param phone
     * @param status
     * @return "false" if does not exist such user, otherwise "true"
     */
    @RequestMapping(value = "/modifyUser", method = RequestMethod.POST)
    public String modifyUser(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                             @RequestParam(value = "id", required = false, defaultValue = "No id") String id,
                             @RequestParam(value = "name", required = false, defaultValue = "No name") String name,
                             @RequestParam(value = "address", required = false, defaultValue = "No address") String address,
                             @RequestParam(value = "phone", required = false, defaultValue = "No phone") String phone,
                             @RequestParam(value = "type", required = false, defaultValue = "No type") String status){
        User host=userService.getByCookie(cookieUserCode.getValue());
        String surname = name.substring(name.indexOf(' ')+1, name.length());
        name = name.substring(0,name.indexOf(' '));
        User u = userService.get(Integer.parseInt(id));
        if (u == null) return "false";//does not exist such user
        u.setName(name);
        u.setSurname(surname);
        u.setAddress(address);
        u.setPhone(phone);
        u.setStatus(status);
        logServiceC.save(host,"modified user "+" "+name+" "+surname);
        userService.save(u);


        return "true";
    }

    /**
     * deleting user from the system
     * @param id
     * @return "false" if does not exist such user, otherwise "true"
     */
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUser(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                             @RequestParam(value = "id", required = false, defaultValue = "0") String id) {
        User host=userService.getByCookie(cookieUserCode.getValue());
        User u = userService.get(Integer.parseInt(id));
        if (u == null) return "false";//does not exist such user
        userService.delete(u);
        logServiceC.save(host,"deleted user "+" "+u.getName()+" "+u.getSurname());
        return "true";
    }

    /**
     * deleting document from the system
     * @param id
     * @return "false" if does not exist such document, otherwise "true"
     */
    @RequestMapping(value = "/deleteItem", method = RequestMethod.POST)
    public String deleteDocumentById(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                                     @RequestParam(value = "documentId", required = false, defaultValue = "0") String id){
        User host=userService.getByCookie(cookieUserCode.getValue());
        Document d = documentService.get(Integer.parseInt(id));
        if (d==null) return "false";//does not exist such book
        logServiceC.save(host,"deleted document "+" "+d.getId());
        documentService.delete(d);
        return "true";
    }

    /**
     * setting status "closed" to the order, returning book to the system
     * @param orderId
     * @return "false" if does not exist such order, otherwise "true"
     */
    @RequestMapping(value = "/closeOrder", method = RequestMethod.POST)
    public String closeOrder(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                            @RequestParam(value = "orderId", required = false, defaultValue = "Not found") String orderId){
        User host=userService.getByCookie(cookieUserCode.getValue());
        Order or = orderService.get(Integer.parseInt(orderId));
        Document document = documentService.get(or.getItemId());
        if (!or.getStatus().equals("queue")) document.setAmount(document.getAmount()+1);
        if (or == null) return "false";//does not exist such order
        logServiceC.save(host,"closed order "+" "+or.getId());
        or.setStatus("closed");
        orderService.save(or);
        documentService.save(document);
        return "true";
    }

    /**
     * setting status "waitForAccept" , starting time and finish time, saving to orders
     * @param orderId
     * @return "true" string
     */
    @RequestMapping(value = "/queueRequest", method = RequestMethod.POST)
    public String queueRequest(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                               @RequestParam(value = "id", required = false, defaultValue = "Not found") String orderId){
        User host=userService.getByCookie(cookieUserCode.getValue());
        Order or = orderService.get(Integer.parseInt(orderId));
        Document d = documentService.get(or.getItemId());
        logServiceC.save(host,"requested a queue "+" "+or.getId());
        return documentService.queueRequest(userService.getByCookie(cookieUserCode.getValue()),d);
    }

    /**
     * searching users
     * @param cookieUserCode
     * @param searchKey
     * @return
     */
    @RequestMapping(value = "/searchUsers", method = RequestMethod.POST)
    public String searchUsers(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                                @RequestParam(value = "name") String searchKey){
        User u = getClientUserObject(getIdFromCookie(cookieUserCode.getValue()));
        List<User> list = getAllUsers();

        list = list.stream().filter(d -> d.isAppropriateForSearch(searchKey)).collect(Collectors.toList());

        return createListOfUsersBlock(getAllUsers(),u);
    }

}
