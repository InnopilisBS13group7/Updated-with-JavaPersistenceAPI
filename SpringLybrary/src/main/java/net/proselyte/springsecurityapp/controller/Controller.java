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
        User u = userService.get(userId);
        if (u == null) return "error";

        String page = "<div id=\"usercard\">" +
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
                createUserHistoryBlock(u) +
                "</div> " +
                "</div>";
        return page;
    }

    public String createUserHistoryBlock(User u) {
        Date date = new Date();
        if (u == null) return "error";
        String items = "<div class=line>";
        long keepingTime;
        int i = 0;
        int margin = -1;
        long wholeFine = 0;
        long fine = 0;
        Document d;
        for (Order or : orderService.getOrdersByUserId(u.getId())) {
            d = documentService.get(or.getItemId());
            if (or.getStatus().equals("open") || or.getStatus().equals("queue") || or.getStatus().equals("renewed")
                    || or.getStatus().equals("waitForAccept")) {
                i++;
                keepingTime = or.getFinishTime();
                if (keepingTime> date.getTime()) fine = (keepingTime-date.getTime())/1000/3600/24;
                items = items + "<div class=\"books\" style=\"margin-left:" + margin + "px\"> " +
                        "<div class=books_inside>" +
                            ((or.getStatus().equals("queue"))? "# in queue"+u.getPositionInQueue(getQueueForDocument(documentService.get(or.getItemId()))):
                                    ((or.getStatus().equals("waitForAccept"))?"Accept the book":getDate(keepingTime))) +
                        "<p class=inside_text id=queue>In queue</p>" +
                        "<p class=inside_text id=fine>Fine: 228$</p>" +
                        "<div class=accept_book id=" +or.getId() + ">Accept the book</div>" +
                        ((!or.getStatus().equals("renewed"))?"<div class=renew_book id=" +or.getId() + ">Renew the book</div>":"") +
                        "<div class=return_book id=" +or.getId() + ">Return the book</div>" +
                        "</div>" +
                        "<img src=\"/resources/img/books/1.jpg\" width=\"190px\" height=\"289px\" /> " +
                        "<p class=\"bookname\">" + documentService.get(or.getItemId()).getTitle() + "</p> " +
                        "</div>";
                margin += 198;
                if (i % 4 == 0) {
                    items +="</div><div class=line>";
                    margin = -5;
                }
            }
        }
        items += "</div>";
        return items;
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
        div += "<div id=search_panel>" +
                "<div class=search_buttons id=search_all>All</div>" +
                "<div class=search_buttons id=search_closed>Closed</div>" +
                "<div class=search_buttons id=search_open>Open</div>" +
                "<div class=search_buttons id=search_finished>Finished</div>" +
                "<div class=search_buttons id=search_queue>Queue</div>" +
                "<div class=search_buttons id=search_renewed>Renewed</div>" +
                "<input type=text id=search_user_id placeholder=\"Users id\" />" +
                "<div class=search_buttons id=search_user>By user</div>" +
                "<div id=search_button>Search</div>" +
                "</div>" +
                "<div id=list_box>";
        for (Order or : orders) {
            d = documentService.get(or.getItemId());
            u = userService.get(or.getUserId());
            div += "<div class=settings_list_orders>" +
                    "<img src=/resources/img/books/1.jpg width=82px height=82px class=settings_orders_list_avatar />" +
                    "<div class=settings_orders_list_specs_box>" +
                    "<b style=\"text-decoration:underline;\"> (" + or.getId() + ")" + d.getTitle()+ "   :" + u.getName() + " " + u.getSurname()  + "</b></br>" +
                    "<b>Status: </b>"+or.getStatus()+"</br>" +
                    "<b>Fine: </b>" + or.getStatus() + "</br>" +
                    "<b>Return date:</b>" + getDate(or.getFinishTime()) +
                    "</div>" +
                    (or.getStatus().equals("closed") ? "":"<div class=settings_orders_list_modify id="+or.getId()+">Close</div>") +
                    "</div>";
        }
        return div + "</div></div>";
    }

    protected String createListOfOrdersBlock(List<Order> orders, String config, int userId){
        String div = "";
        Document d;
        User u;
        config = config.toLowerCase();

        for (Order or : orders) {
            if (config.contains(or.getStatus()) && !config.contains("byuserid") ||
                    config.contains("byuserid") && config.contains(or.getStatus()) && or.getUserId() == userId) {
                d = documentService.get(or.getItemId());
                u = userService.get(or.getUserId());
                div += "<div class=settings_list_orders>" +
                        "<img src=/resources/img/books/1.jpg width=82px height=82px class=settings_orders_list_avatar />" +
                        "<div class=settings_orders_list_specs_box>" +
                        "<b style=\"text-decoration:underline;\"> (" + or.getId() + ")" + d.getTitle() + "   :" + u.getName() + " " + u.getSurname() + "</b></br>" +
                        "<b>Status: </b>" + or.getStatus() + "</br>" +
                        "<b>Fine: </b>" + or.getStatus() + "</br>" +
                        "<b>Return date:</b>" + getDate(or.getFinishTime()) +
                        "</div>" +
                        (or.getStatus().equals("closed") ? "" : "<div class=settings_orders_list_modify id=" + or.getId() + ">Close</div>") +
                        "</div>";
            }
        }
        return div;
    }

    protected List<User> getQueueForDocument(Document document){
        List<Order> queue = orderService.getQueue(document.getId());
        List<User> users = new LinkedList<>();
        for (Order or:queue){
            users.add(userService.get(or.getUserId()));
        }
        List<User> students = new LinkedList<>();
        List<User> instructors= new LinkedList<>();
        List<User> tas= new LinkedList<>();
        List<User> visitingProfessors= new LinkedList<>();
        List<User> professors= new LinkedList<>();
        for(User u:users){
            if (u.getStatus().equals("student")){
                students.add(u);
                continue;
            }
            if (u.getStatus().equals("instructor")){
                instructors.add(u);
                continue;
            }
            if (u.getStatus().equals("ta")){
                tas.add(u);
                continue;
            }if (u.getStatus().equals("professor")){
                professors.add(u);
                continue;
            }
            if (u.getStatus().equals("visitingProfessor"))
                visitingProfessors.add(u);

        }
        students.addAll(instructors);
        students.addAll(tas);
        students.addAll(visitingProfessors);
        students.addAll(professors);
        return students;

    }
}
