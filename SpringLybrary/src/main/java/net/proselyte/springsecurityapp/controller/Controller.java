package net.proselyte.springsecurityapp.controller;

import net.proselyte.springsecurityapp.dao.DocumentRepository;
import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.Order;
import net.proselyte.springsecurityapp.model.User;
import net.proselyte.springsecurityapp.service.DocumentService;
import net.proselyte.springsecurityapp.service.OrderService;
import net.proselyte.springsecurityapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Encoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    protected UserService userService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected DocumentService documentService;

    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat formatForDate = new SimpleDateFormat("dd.MM.yyyy");
        return formatForDate.format(date);
    }

    public static String getDate(long currentTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(cal.getTime());
    }

    public static int getIdFromCookie(String cookieUserCode) {
        return Integer.parseInt(cookieUserCode.substring(6, cookieUserCode.length() - 6));
    }

    public boolean createNewCookieForUser(String email, HttpServletResponse response) {

        User u = userService.get(email);
        if (u == null) return false;

        Random random = new Random();
        String cookieId = (random.nextInt(700000) + 100000) + "" + u.getId() + "" + (random.nextInt(700000) + 100000);
        u.setCookieId(cookieId);
        userService.save(u);
        Cookie cookie = new Cookie("user_code", cookieId);
        cookie.setMaxAge(3600 * 12);
        response.addCookie(cookie);
        return true;
    }

    public String createUserCardPage(int userId) {
        String page = "";
        User u = userService.get(userId);
        if (u == null) return "error";

        String title, time, items = "";
        long keepingTime;
        int i = 0;
        int margin = -5;
        for (Order or : orderService.getOpenOrdersByUserId(u.getId())) {
            i++;
            keepingTime = or.getFinishTime();
            items = items + "<div class=\"books\" style=\"margin-left:" + margin + "px\"> " +
                    "<div class=books_inside>" + getDate(keepingTime) +
                    "<div class=return_book id=" + or.getId() + ">Return the book</div></div>" +
                    "<img src=\"/resources/img/books/1.jpg\" width=\"190px\" height=\"289px\" /> " +
                    "<p class=\"bookname\">" + "3 PIGS</p> " +
                    "</div>";
            margin += 198;
            if (i % 4 == 0) margin = -5;

        }

        page = "<div id=\"usercard\">" +
                "<div id=\"usercard_avatar\" class=\"blocks\"></div>" +
                "<div class=\"blocks\" id=\"usercard_info\">" +
                "<p id=\"name\">" + u.getName() + " " + u.getSurname() + "</p> " +
                "<p id=\"settings_bottom\">Settings</p> " +
                "<p class=\"usercard_info_text1\" style=\"margin-top:-8px\">Status:</p> " +
                "<p class=\"usercard_info_text1\" style=\"margin-top:22px\">fine:</p> " +
                "<p class=\"usercard_info_text1\" style=\"margin-top:52px\">Address:</p> " +
                "<p class=\"usercard_info_text1\" style=\"margin-top:82px\">Phone:</p> " +
                "<p class=\"usercard_info_text1\" style=\"margin-top:112px\">Card Id:</p> " +
                "<p class=\"usercard_info_text2\" style=\"margin-top:-8px\">" + u.getStatus() + "</p> " +
                "<p class=\"usercard_info_text2\" style=\"margin-top:22px\">" + u.getFine() + "</p> " +
                "<p class=\"usercard_info_text2\" style=\"margin-top:52px\">" + u.getAddress() + "</p> " +
                "<p class=\"usercard_info_text2\" style=\"margin-top:82px\">" + u.getPhone() + "</p> " +
                "<p class=\"usercard_info_text2\" style=\"margin-top:112px\">" + userId + "</p> " +
                "</div> " +
                "<div class=\"blocks\" id=\"history\"> " +
                "<div class=\"line\"> " +
                items +
                "</div> " +
                "</div> " +
                "</div>";
        return page;
    }

    public static String getClientIpAddress(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    protected boolean isCookieWrong(Cookie cookieUserCode) {
        if (cookieUserCode == null) return true;
        return userService.getByCookie(cookieUserCode.getValue()) == null;
    }

    protected boolean addNewUserToTheSystem(String name, String surname, String email, String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (userService.get(email) != null) return false;
        String encodedPassword = encoder.encode(password);
        userService.add(new User(email, encodedPassword, name, surname));
        return true;
    }

    protected boolean addNewUserToTheSystem(String name, String surname, String email, String password, String status) {
        if (userService.get(email) != null) return false;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        userService.add(new User(email, encodedPassword, name, surname, status));
        return true;
    }

    protected List<User> getAllUsers() {
        return userService.getAllusers();
    }

    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    protected List<Order> getAllFinishedOrders() {
        return orderService.getAllFinishedOrders();
    }

    protected List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }


    protected User getClientUserObject(String id) {
        return userService.get(Integer.parseInt(id));
    }

    protected User getClientUserObject(int id) {
        return userService.get(id);
    }

    protected String createListOfUsersBlock(List<User> users) {
        String div = "<div class=settings_type_box id=settings_users>" +
                "<div id=new_user><p id=new_user_bottom>+ Add a new user</p>" +
                "<input type=text class=new_user_inputs id=new_user_inputs_name placeholder=\"Name\" />" +
                "<input type=text class=new_user_inputs id=new_user_inputs_surname placeholder=\"Surname\" />" +
                "<input type=text class=new_user_inputs id=new_user_inputs_email placeholder=\"email\" />" +
                "<input type=password class=new_user_inputs id=new_user_inputs_password placeholder=\"Password\" />" +
                "<input type=text class=new_user_inputs id=new_user_inputs_type placeholder=\"Type\" />" +
                "<div id=new_user_save>Save</div>" +
                "<div id=new_user_alert>New user was saved</div>" +
                "</div>";
        for (User u : users) {
            div += "<div class=settings_list_users>" +
                    "<img src=/resources/img/avatars/1.jpg width=106px height=106px class=settings_users_list_avatar />" +
                    "<div class=settings_users_list_specs_box>" +
                    "<b>Name: </b><input type=text class=settings_inputs_users_name placeholder=\"Name\" value=\""+ u.getName() + " " + u.getSurname() +"\" /></br>" +
                    "<b>Adress: </b><input type=text class=settings_inputs_users_adress placeholder=\"Adress\" value=\""+u.getAddress()+"\" /></br>" +
                    "<b>Phone number: </b><input type=text class=settings_inputs_users_phone placeholder=\"Phone number\" value=\""+u.getPhone()+"\" /></br>" +
                    "<b>id: </b>"+u.getId()+"</br>" +
                    "<b>Type: </b><input type=text class=settings_inputs_users_type placeholder=\"Type\" value=\"" + u.getStatus() + "\" /></br>" +
                    "</div>" +
                    "<div class=settings_users_list_modify id=" + u.getId() + ">Save</div>" +
                    "</div>";
        }
        return div + "</div>";
    }

    protected String createListOfOrdersBlock(List<Order> orders){
        String div = "<div class=settings_type_box id=settings_orders>";
        Document d;
        User u;
        for (Order or : orders) {
            d = documentService.get(or.getItemId());
            u = userService.get(or.getUserId());
            div += "<div class=settings_list_orders>" +
                    "<img src=/resources/img/books/1.jpg width=62px height=62px class=settings_orders_list_avatar />" +
                    "<div class=settings_orders_list_specs_box>" +
                    "<b style=\"text-decoration:underline;\"> (" + or.getId() + ")" + d.getTitle()+ "   :" + u.getName() + " " + u.getSurname()  + "</b></br>" +
                    "<b>Status: </b>"+or.getStatus()+"</br>" +
                    "<b>Return date:</b>" + getDate(or.getFinishTime()) +
                    "</div>" +
                    (or.getStatus().equals("closed") ? "":"<div class=settings_orders_list_modify id="+or.getId()+">Close</div>") +
                    "</div>";
        }
        return div + "</div>";
    }
}
