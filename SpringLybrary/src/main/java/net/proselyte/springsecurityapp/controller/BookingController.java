package net.proselyte.springsecurityapp.controller;

import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.Order;
import net.proselyte.springsecurityapp.model.User;
import net.proselyte.springsecurityapp.service.LogServiceC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class BookingController extends Controller {

    @Autowired
    protected LogServiceC logServiceC;

    /**
     *checking requirements, setting keeping time,if ammount is 0 adding to waiting list, if not 0 - opening the order
     * @param id
     * @param cookieUserCode
     * @return if meets the requirements "true", otherwise "false" (already has it or waiting in queue, document status is reference,)
     */
    @RequestMapping(value = "/takeItem", method = POST)
    public String takeItem(@RequestParam(value = "documentId", required = true, defaultValue = "0") String id,
                           @CookieValue(value = "user_code", required = false) Cookie cookieUserCode) {
        User u=userService.getByCookie(cookieUserCode.getValue());
        int documentId = Integer.parseInt(id);
        int userId = getIdFromCookie(cookieUserCode.getValue());
        logServiceC.save(u,"took document "+" "+documentId);
        return userService.checkoutDocument(documentId,userId);
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

        String divList = ((u.getStatus().equals("admin")||u.getStatus().equals("lib2")||u.getStatus().equals("lib3") ) ? "<div id=new_doc_box>" +
                "<div class=new_doc id=new_book>+ Add a new book</div>" +
                "<div class=new_doc id=new_av>+ Add a new audio/video</div>" +
                "<div class=add_block id=add_block_book>" +
                "<input class=add_inputs id=add_book_title placeholder=Title />" +
                "<input class=add_inputs id=add_book_author placeholder=Authors />" +
                "<input class=add_inputs id=add_book_publisher placeholder=Publisher />" +
                "<input class=add_inputs id=add_book_year placeholder=Year />" +
                "<input class=add_inputs id=add_book_edition placeholder=Edition />" +
                "<input class=add_inputs id=add_book_note placeholder=Note />" +
                "<input class=add_inputs id=add_book_number placeholder=\"Number of copies\" />" +
                "<div class=add_save id=add_save_book>Add</div>" +
                "</div>" +
                "<div class=add_block id=add_block_av>" +
                "<input class=add_inputs id=add_av_title placeholder=Title />" +
                "<input class=add_inputs id=add_av_author placeholder=Authors />" +
                "<input class=add_inputs id=add_av_number placeholder=\"Number of copies\" />" +
                "<div class=add_save id=add_save_av>Add</div>" +
                "</div>" +
                "</div>" : "");

        divList += "<div id=booking_search_box>" +
                "<div id=booking_search>" +
                "<input type=text id=booking_search_name placeholder=\"Search\" />" +
                "<select id=booking_search_select>" +
                "<option>By title</option>" +
                "<option>By title</option>" +
                "<option>By author</option>" +
                "<option>By publisher</option>" +
                "<option>By year</option>" +
                "<option>By type</option>" +
                "<option>By edition</option>" +
                "<option>By note</option>" +
                "</select>" +
                "<div id=booking_available_id></div>" +
                "<div id=booking_available>" +
                "<div id=booking_available_text>Only available</div>" +
                "</div>" +
                "</div>" +
                "</div>";

        /*for (Document d : getAllDocuments()) {
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
                    "<div class=deleteit id=" + d.getId() + ">Delete</div>" +
                    "<div class=bookit id=" + d.getId() + ">Book</div>" +
                    ((u.getStatus().equals("admin"))?(
                            "<div class=modifyit id=" + d.getId() + ">Modify</div>" +
                                    "<div class=queue id=" + d.getId() + ">Queue</div>"):"") +
                    "<div class=queue_box></div>" +
                    "</div>";
        }*/

        divList = divList + getListOfDocuments(getAllDocuments(), u);
        return divList;
    }

    @RequestMapping(value = "/bookingUpdate", method = POST)
    public String bookingUpdate(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode) {
        if (isCookieWrong(cookieUserCode)) return "false";
        User u = getClientUserObject(getIdFromCookie(cookieUserCode.getValue()));
        String divList =  getListOfDocuments(getAllDocuments(), u);
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
        User u=userService.getByCookie(cookieUserCode.getValue());
        if (isCookieWrong(cookieUserCode)) return "false";
        logServiceC.save(u,"returned document "+" "+orderId);
        return documentService.returnDocument(Integer.parseInt(orderId));
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

    @RequestMapping(value = "/bookingSearch", method = RequestMethod.POST)
    public String bookingSearch(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                                @RequestParam(value = "text") String searchText,
                                @RequestParam(value = "type") String searchType,
                                @RequestParam(value = "available") String isAvailable){
        User u = getClientUserObject(getIdFromCookie(cookieUserCode.getValue()));
        List<Document> list = getAllDocuments();
        String type = searchType.substring(3,searchType.length());
        type = type.toLowerCase();
        System.out.println("IT's search type: "+type);
        Predicate<Document> pr;
        pr = isAvailable.equals("True")? d -> d.isAppropriateForSearch(searchText,searchType) && d.getAmount() > 0 : d -> d.isAppropriateForSearch(searchText,searchType);
        list = list.stream().filter(pr).collect(Collectors.toList());

        return getListOfDocuments(list, u);
    }




}
