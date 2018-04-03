package net.proselyte.springsecurityapp.controller;

import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.Order;
import net.proselyte.springsecurityapp.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class BookingController extends Controller {

    /**
     *checking requirements, setting keeping time,if ammount is 0 adding to waiting list, if not 0 - opening the order
     * @param id
     * @param cookieUserCode
     * @return if meets the requirements "true", otherwise "false" (already has it or waiting in queue, document status is reference,)
     */
    @RequestMapping(value = "/takeItem", method = POST)
    public String takeItem(@RequestParam(value = "documentId", required = true, defaultValue = "0") String id,
                           @CookieValue(value = "user_code", required = false) Cookie cookieUserCode) {
        int documentId = Integer.parseInt(id);
        Date date = new Date();
        Document d = documentService.get(documentId);
        User u = userService.get(getIdFromCookie(cookieUserCode.getValue()));
        List<Order> orders = orderService.getOrdersByUserId(u.getId());

        boolean check = false;
        for (Order or : orders) {
            if (or.getItemId() == documentId && (or.getStatus().equals("open") || or.getStatus().equals("queue"))) {
                check = true;
            }
        }
        if (check) return "false";
        if (d == null) return "false";
        int currentAmount = d.getAmount();
        if (d.getStatus().equals("reference")) return "false";
        //-- some conditions
        long keepingTime = 0;
        String userStatus = u.getStatus();
        String documentStatus = d.getStatus();
        if (documentStatus.equals("bestseller")) keepingTime = 1209600000;
        else {
            if (userStatus.equals("disabled") || userStatus.equals("activated")) keepingTime = 1728000000;
            else if (userStatus.equals("visiting professor")) keepingTime = 604800000L;
            else keepingTime = 2 * 1728000000L;

        }
        //------
        if (currentAmount == 0) {
            orderService.save(new Order(u.getId(), documentId, date.getTime(), date.getTime()+keepingTime, "queue"));
            return "You are in queue";
        }
        //------
        d.setAmount(d.getAmount() - 1);
        documentService.save(d);
        orderService.save(new Order(u.getId(), documentId, date.getTime(), (date.getTime() + keepingTime), "open"));
        return "true";
    }

    /**
     * get list of all documents
     * @param cookieUserCode
     * @return list of all documents
     */
    @RequestMapping(value = "/listItems", method = POST)
    public String listItems(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode) {
        if (isCookieWrong(cookieUserCode)) return "false";
        User u = getClientUserObject(getIdFromCookie(cookieUserCode.getValue()));

        String divList = (u.getStatus().equals("admin") ? "<div id=new_doc_box>" +
                "<div class=new_doc id=new_book>+ Add a new book</div>" +
                "<div class=new_doc id=new_av>+ Add a new audio/video</div>" +
                "<div class=add_block id=add_block_book>" +
                "<input class=add_inputs id=add_book_title placeholder=Title />" +
                "<input class=add_inputs id=add_book_author placeholder=Authors />" +
                "<input class=add_inputs id=add_book_publisher placeholder=Publisher />" +
                "<input class=add_inputs id=add_book_year placeholder=Year />" +
                "<input class=add_inputs id=add_book_edition placeholder=Edition />" +
                "<input class=add_inputs id=add_book_note placeholder=Note />" +
                "<div class=add_save id=add_save_book>Add</div>" +
                "</div>" +
                "<div class=add_block id=add_block_av>" +
                "<input class=add_inputs id=add_av_title placeholder=Title />" +
                "<input class=add_inputs id=add_av_author placeholder=Authors />" +
                "<div class=add_save id=add_save_av>Add</div>" +
                "</div>" +
                "</div>" : "");


        for (Document d : getAllDocuments()) {
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
                    "<div class=bookit id=" + d.getId() + ">Book</div>" +
                    ((u.getStatus().equals("admin"))?(
                            "<div class=modifyit id=" + d.getId() + ">Modify</div>" +
                                    "<div class=queue id=" + d.getId() + ">Queue</div>"):"") +
                    "<div class=queue_box></div>" +
                    "</div>";
        }


        return divList;
    }


    /**
     * returning document to the system, finishing the order
     * @param cookieUserCode
     * @param orderId
     * @return "true" if successfully, "false" if cookie is wrong
     */
    @RequestMapping(value = "/returnDocument", method = RequestMethod.POST)
    public String returnDocument(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                                 @RequestParam(value = "orderId") String orderId){
        if (isCookieWrong(cookieUserCode)) return "false";

        Order or = orderService.get(Integer.parseInt(orderId));
        or.setStatus("finished");
        orderService.save(or);
        return "true";
    }


    @RequestMapping(value = "/goToQueue", method = RequestMethod.POST)
    public String goToQueue(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                            @RequestParam(value = "id") String documentId){
        if (isCookieWrong(cookieUserCode)) return "false";
        Document d = documentService.get(Integer.parseInt(documentId));
        List<Order> orderQueue = new LinkedList<>();
        for (User u : getQueueForDocument(d)){
            orderQueue.add(orderService.getOrdersByUserIdAndItemId(u.getId(),d.getId()).get(0));
        }
        String div = "";
        User u;
        int i = 1;
        for (Order or : orderQueue) {

            d = documentService.get(or.getItemId());
            u = userService.get(or.getUserId());
            div += "<div class=settings_list_orders>" +
                    "<img src=/resources/img/avatars/0.png width=82px height=82px class=settings_orders_list_avatar />" +
                    "<div class=settings_orders_list_specs_box>" +
                    "<b style=\"text-decoration:underline;\"> (" + i + ") " + u.getName() + " " + u.getSurname()  + "</b></br>" +
                    "<b>Status: </b>"+u.getStatus()+"</br>" +
                    "<b>Fine: </b>" + getFine(or,d) + "</br>" +
                    "<b>Return date:</b>" + getDate(or.getFinishTime()) +
                    "</div>" +
                    "<div class=otdat id="+ or.getId()+">Queue Request</div>" +
                    "</div>";
            i++;
        }
        return (i == 1)?"Queue is empty":div;
    }




}
