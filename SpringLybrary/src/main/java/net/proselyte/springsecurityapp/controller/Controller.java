package net.proselyte.springsecurityapp.controller;

import net.proselyte.springsecurityapp.dao.DocumentRepository;
import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.Log;
import net.proselyte.springsecurityapp.model.Order;
import net.proselyte.springsecurityapp.model.User;
import net.proselyte.springsecurityapp.service.DocumentService;
import net.proselyte.springsecurityapp.service.LogServiceC;
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
    protected LogServiceC logServiceC;

    @Autowired
    protected UserService userService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected DocumentService documentService;

    /**
     * @return date in string format
     */
    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat formatForDate = new SimpleDateFormat("dd.MM.yyyy");
        return formatForDate.format(date);
    }

    /**
     * sets current time in miliseconds
     * @param currentTime
     * @return  time in miliseconds in string format
     */
    public static String getDate(long currentTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(cal.getTime());
    }

    /**
     *
     * @param or object
     * @param d object
     * @return fine by current order
     */
    public int getFine(Order or, Document d){
        return orderService.getFine(or,d);
    }

    /**
     * @param cookieUserCode
     * @return id from cookie
     */
    public static int getIdFromCookie(String cookieUserCode) {
        return Integer.parseInt(cookieUserCode.substring(6, cookieUserCode.length() - 6));
    }

    /**
     * create new cookie for user
     * @param email
     * @param response
     * @return cookie
     */
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

    /**
     * create user card page
     * @param userId
     * @return user card page
     */
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

    /**
     * create a part of user page about previous orders
     * @param u
     * @return block with previous orders
     */
    public String createUserHistoryBlock(User u) {
        Date date = new Date();
        if (u == null) return "error";
        String items = "<div id=history_alert></div>" +
                "<div class=line>";
        long keepingTime;
        int i = 0;
        int margin = -1;
        long wholeFine = 0;
        long fine = 0;
        Document d;
        boolean shouldNotify = false;
        for (Order or : orderService.getOrdersByUserId(u.getId())) {
            d = documentService.get(or.getItemId());
            if (or.getStatus().equals("waitForAccept")) shouldNotify = true;
            if (or.getStatus().equals("open") || or.getStatus().equals("queue") || or.getStatus().equals("renewed")
                    || or.getStatus().equals("waitForAccept")) {
                i++;
                keepingTime = or.getFinishTime();
                if (keepingTime < date.getTime()) fine = getFine(or,d);
                items = items + "<div class=\"books\" style=\"margin-left:" + margin + "px\"> " +
                        "<div class=books_inside>" +
                        ((or.getStatus().equals("waitForAccept"))?("Document is Ready!"):getDate(keepingTime)) +
                        (or.getStatus().equals("queue")? "<p class=inside_text id=queue># in queue "+ (1+u.getPositionInQueue(getQueueForDocument(documentService.get(or.getItemId())))) +"</p>" : "") +
                        "<p class=inside_text id=fine>Fine: "+fine+"</p>" +
                        "<p class=inside_text >Status: "+or.getStatus() +"</p>" +
                        ((or.getStatus().equals("waitForAccept"))?("<div class=accept_book id=" +or.getId() + ">Accept the book</div>"):"") +
                        ((or.getStatus().equals("open"))?"<div class=renew_book id=" +or.getId() + ">Renew the book</div>":"") +
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
        if (shouldNotify) items += "<script>$(\"#alert_message\").text(\"Document is available!\");"+
                "$(\"#alert_back\").slideDown(0).animate({\"opacity\":\"1\"}, 200);</script>";
        return items;
    }

    /**
     * @param request
     * @return Client Ip Address
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    /**
     * checks the correctness of cookie
     * @param cookieUserCode
     * @return true/false
     */
    protected boolean isCookieWrong(Cookie cookieUserCode) {
        if (cookieUserCode == null) return true;
        return userService.getByCookie(cookieUserCode.getValue()) == null;
    }

    /**
     * adds new user to the system
     * @param name
     * @param surname
     * @param email
     * @param password
     * @return true/false
     */
    protected boolean addNewUserToTheSystem(String name, String surname, String email, String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (userService.get(email) != null) return false;
        String encodedPassword = encoder.encode(password);
        userService.add(new User(email, encodedPassword, name, surname));
        return true;
    }

    /**
     * adds new user to the system
     * @param name
     * @param surname
     * @param email
     * @param password
     * @return true/false
     */
    protected boolean addNewUserToTheSystem(String name, String surname, String email, String password, String status) {
        if (userService.get(email) != null) return false;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        userService.add(new User(email, encodedPassword, name, surname, status));
        return true;
    }

    /**
     *
     * @return list of all users in the system
     */
    protected List<User> getAllUsers() {
        return userService.getAllusers();
    }

    /**
     *
     * @return list of all orders in the system
     */
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     *
     * @return list of all finished orders in the system
     */
    protected List<Order> getAllFinishedOrders() {
        return orderService.getAllFinishedOrders();
    }

    /**
     *
     * @return list of all documents in the system
     */
    protected List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    protected User getClientUserObject(String id) {
        return userService.get(Integer.parseInt(id));
    }

    /**
     *
     * @param id
     * @return user by id
     */
    protected User getClientUserObject(int id) {
        return userService.get(id);
    }

    /**
     * @param users
     * @return block with all users
     */
    protected String createListOfUsersBlock(List<User> users, User host) {
        String div = "<div class=settings_type_box id=settings_users>" +
                ((isLibrarian(host.getStatus()) && !host.getStatus().equals("lib1"))?"<div id=new_user><p id=new_user_bottom>+ Add a new user</p>" +
                "<input type=text class=new_user_inputs id=new_user_inputs_name placeholder=\"Name\" />" +
                "<input type=text class=new_user_inputs id=new_user_inputs_surname placeholder=\"Surname\" />" +
                "<input type=text class=new_user_inputs id=new_user_inputs_email placeholder=\"email\" />" +
                "<input type=password class=new_user_inputs id=new_user_inputs_password placeholder=\"Password\" />" +
                "<input type=text class=new_user_inputs id=new_user_inputs_type placeholder=\"Type\" />" +
                "<div id=new_user_save>Save</div>" +
                "<div id=new_user_alert>New user was saved</div>" +
                "</div>":"<div id=users_plug></div>") +
                "<input id=search_users placeholder=\"Search\" />" +
                "<select id=settings_search_select>" +
                "<option>By all</option>" +
                "<option>By name</option>" +
                "<option>By address</option>" +
                "<option>By phone</option>" +
                "<option>By id</option>" +
                "<option>By type</option>" +
                "</select>";
        for (User u : users) {
            div += "<div class=settings_list_users>" +
                    "<img src=/resources/img/avatars/1.jpg width=106px height=106px class=settings_users_list_avatar />" +
                    "<div class=settings_users_list_specs_box>" +
                    "<b>Name: </b><input type=text class=settings_inputs_users_name placeholder=\"Name\" value=\""+ u.getName() + " " + u.getSurname() +"\" /></br>" +
                    "<b>Address: </b><input type=text class=settings_inputs_users_adress placeholder=\"Address\" value=\""+u.getAddress()+"\" /></br>" +
                    "<b>Phone number: </b><input type=text class=settings_inputs_users_phone placeholder=\"Phone number\" value=\""+u.getPhone()+"\" /></br>" +
                    "<b>id: </b>"+u.getId()+"</br>" +
                    "<b>Type: </b><select class=settings_inputs_users_type >\" +\n" +
                    "<option>" + u.getStatus() + "</option>" +"\n" +
                    "\"<option>lib1</option>\" +\n" +
                    "\"<option>lib2</option>\" +\n" +
                    "\"<option>lib3</option>\" +\n" +
                    "\"<option>student</option>\" +\n" +
                    "\"<option>visitingProfessor</option>\" +\n" +
                    "\"<option>instructor</option>\" +\n" +
                    "\"<option>ta</option>\" +\n" +
                    "\"<option>professor</option>\" +\n" +
                    "\"</select></br>" +
                    "</div>" +
                    ((host.getStatus().equals("admin") ||host.getStatus().equals("lib3"))?"<div class=settings_users_list_delete id=" + u.getId() + ">Delete</div>" : "") +
                    (isLibrarian(host.getStatus())?"<div class=settings_users_list_modify id=" + u.getId() + ">Save</div>":"") +
                    "</div>";
        }
        return div + "</div>";
    }

    protected String usersListForSettings(List<User> users, User host){
        String div = "";
        for (User u : users) {
            div += "<div class=settings_list_users>" +
                    "<img src=/resources/img/avatars/1.jpg width=106px height=106px class=settings_users_list_avatar />" +
                    "<div class=settings_users_list_specs_box>" +
                    "<b>Name: </b><input type=text class=settings_inputs_users_name placeholder=\"Name\" value=\""+ u.getName() + " " + u.getSurname() +"\" /></br>" +
                    "<b>Address: </b><input type=text class=settings_inputs_users_adress placeholder=\"Address\" value=\""+u.getAddress()+"\" /></br>" +
                    "<b>Phone number: </b><input type=text class=settings_inputs_users_phone placeholder=\"Phone number\" value=\""+u.getPhone()+"\" /></br>" +
                    "<b>id: </b>"+u.getId()+"</br>" +
                    "<b>Type: </b><select class=settings_inputs_users_type >\" +\n" +
                    "<option>" + u.getStatus() + "</option>" +"\n" +
                    "\"<option>lib1</option>\" +\n" +
                    "\"<option>lib2</option>\" +\n" +
                    "\"<option>lib3</option>\" +\n" +
                    "\"<option>student</option>\" +\n" +
                    "\"<option>visitingProfessor</option>\" +\n" +
                    "\"<option>instructor</option>\" +\n" +
                    "\"<option>ta</option>\" +\n" +
                    "\"<option>professor</option>\" +\n" +
                    "\"</select></br>" +
                    "</div>" +
                    ((host.getStatus().equals("admin") ||host.getStatus().equals("lib3"))?"<div class=settings_users_list_delete id=" + u.getId() + ">Delete</div>" : "") +
                    (isLibrarian(host.getStatus())?"<div class=settings_users_list_modify id=" + u.getId() + ">Save</div>":"") +
                    "</div>";
        }
        return div;
    }

    boolean isLibrarian(String s){
        return s.equals("admin") || s.equals("lib1") || s.equals("lib2")|| s.equals("lib3");
    }

    String createListOfHistoryBlock(String history){
        return history;
    }

    String getAllHistory(){
        String div="";
        List<Log> logs = logServiceC.getAllLogs();
        div = "<div class=settings_type_box id=settings_history>";
        for (Log l:logs) {
            div += "<div class=settings_history_list_box>\n" +
                    "  <p class=settings_history_list_text>"+l.getInfo() +"</p>\n"  +
                    "  <p class=settings_history_list_date>"+l.getDate()+"</p>\n" +
                    "</div>";
        }

        return div + "</div>";
    }

    /**
     * @param orders
     * @return block with all orders
     */
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
                    "<b>Fine: </b>" + getFine(or,d) + "</br>" +
                    "<b>Return date:</b>" + getDate(or.getFinishTime()) +
                    "</div>" +
                    (or.getStatus().equals("closed") ? "":"<div class=settings_orders_list_modify id="+or.getId()+">Close</div>") +
                    "</div>";
        }
        return div + "</div></div>";
    }

    /**
     * @param orders
     * @param config
     * @param userId
     * @return block with all orders
     */
    protected String createListOfOrdersBlock(List<Order> orders, String config, int userId){
        String div = "";
        Document d;
        User u;
        config = config.toLowerCase();
        for (Order or : orders) {
            if (config.contains(or.getStatus().toLowerCase()) && !config.contains("by user") ||
                    config.contains("by user") && config.contains(or.getStatus().toLowerCase()) && or.getUserId() == userId) {
                d = documentService.get(or.getItemId());
                u = userService.get(or.getUserId());
                div += "<div class=settings_list_orders>" +
                        "<img src=/resources/img/books/1.jpg width=82px height=82px class=settings_orders_list_avatar />" +
                        "<div class=settings_orders_list_specs_box>" +
                        "<b style=\"text-decoration:underline;\"> (" + or.getId() + ")" + d.getTitle() + "   :" + u.getName() + " " + u.getSurname() + "</b></br>" +
                        "<b>Status: </b>" + or.getStatus() + "</br>" +
                        "<b>Fine: </b>" + getFine(or,d) + "</br>" +
                        "<b>Return date:</b>" + getDate(or.getFinishTime()) +
                        "</div>" +
                        (or.getStatus().equals("closed") ? "" : "<div class=settings_orders_list_modify id=" + or.getId() + ">Close</div>") +
                        "</div>";
            }
        }
        return div;
    }

    /**
     * @param document
     * @return queue for document
     */
    protected List<User> getQueueForDocument(Document document){
        return documentService.getQueueForDocument(document);

    }

    protected String getListOfDocuments(List<Document> list, User u){
        String divList = "";
        for (Document d : list) {
            divList = divList + "<div class=books_box><img src=/resources/img/books/" + (d.getType().equals("book") ? "1.jpg" : "2.jpg") + " class=cover width=94px height=145px />" +
                    "<p class=books_text>" +
                    "Title:&nbsp;<input class=books_inputs_title placeholder=\"Title\" value=\"" + d.getTitle() + "\" /></br>" +
                    "Author:&nbsp;<input class=books_inputs_author placeholder=\"Author\" value=\"" + d.getAuthor() + "\" /></br>" +

                    (d.getType().equals("book") ? "Publisher:&nbsp;<input class=books_inputs_publisher placeholder=\"Publisher\" value=\"" + d.getPublisher() + "\" /></br>" +
                            "Year:&nbsp;<input class=books_inputs_year placeholder=\"Year\" value=\"" + d.getYear() + "\" /></br>" +
                            "Type:&nbsp;<input class=books_inputs_type placeholder=\"Type\" value=\"" + d.getEdition() + "\" /></br>" +
                            "Edition:&nbsp;<input class=books_inputs_edition placeholder=\"Edition\" value=\"" + d.getEdition() + "\" /></br>" +
                            "Note:&nbsp;<input class=books_inputs_note placeholder=\"Note\" value=\"" + d.getDescription() + "\" /></br>" : "") +
                    "</p>" +
                    ((u.getStatus().equals("admin") || u.getStatus().equals("lib3"))?"<div class=deleteit id=" + d.getId() + ">Delete</div>":"") +
                    "<div class=bookit id=" + d.getId() + ">Book</div>" +
                    ((u.getStatus().equals("admin") || isLibrarian(u.getStatus()))?(
                            "<div class=modifyit id=" + d.getId() + ">Modify</div>" +
                                    "<div class=queue id=" + d.getId() + ">Queue</div>"):"") +
                    "<div class=queue_box></div>" +
                    "</div>";
        }
        return divList;
    }

    public static int editdist(String S1, String S2) {
        int m = S1.length(), n = S2.length();
        int[] D1;
        int[] D2 = new int[n + 1];

        for(int i = 0; i <= n; i ++)
            D2[i] = i;

        for(int i = 1; i <= m; i ++) {
            D1 = D2;
            D2 = new int[n + 1];
            for(int j = 0; j <= n; j ++) {
                if(j == 0) D2[j] = i;
                else {
                    int cost = (S1.charAt(i - 1) != S2.charAt(j - 1)) ? 1 : 0;
                    if(D2[j - 1] < D1[j] && D2[j - 1] < D1[j - 1] + cost)
                        D2[j] = D2[j - 1] + 1;
                    else if(D1[j] < D1[j - 1] + cost)
                        D2[j] = D1[j] + 1;
                    else
                        D2[j] = D1[j - 1] + cost;
                }
            }
        }
        return D2[n];
    }
}
