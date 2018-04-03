package net.proselyte.springsecurityapp.controller;

import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.Order;
import net.proselyte.springsecurityapp.model.User;
import net.proselyte.springsecurityapp.service.OrderService;
import net.proselyte.springsecurityapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
public class EntireController extends net.proselyte.springsecurityapp.controller.Controller//extends net.repositories.controllers.Controller
{
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(//@RequestParam(value = "name", required = false, defaultValue = "ITP_Project") String name,
                        HttpServletRequest request,
                        @CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                        Model model) {
        Date date = new Date();

        //check queue
        List<Document> documentList = documentService.getAllDocuments();
        int userId;
        Order or;
        for (Document d: documentService.getAllDocuments()){
            if (d.getAmount() > 0 ){
                if (getQueueForDocument(d).size() > 0) {
                    userId = getQueueForDocument(d).get(0).getId();
                    or = orderService.getOrdersByUserIdAndItemId(userId, d.getId()).get(0);
                    or.setStatus("waitForAccept");
                    or.setFinishTime(date.getTime() +  or.getFinishTime() - or.getStartTime());
                    or.setStartTime(date.getTime());
                }
            }
        }

        for (Order order: orderService.getOrdersByStatus("waitForAccept")){
            if (date.getTime() - order.getStartTime() > 3600000*24) order.setStatus("closed");
            orderService.save(order);
        }



        if (cookieUserCode != null) {
            User u = userService.getByCookie(cookieUserCode.getValue());
            if (u == null) return "index";
                        model.addAttribute("name", u.getName()+" "+u.getSurname());
            model.addAttribute("status", u.getStatus());
            model.addAttribute("fine", u.getFine()+"$");
            model.addAttribute("booki", createUserHistoryBlock(u));
            model.addAttribute("address", u.getAddress());
            model.addAttribute("id", u.getId());
            model.addAttribute("phone", u.getPhone());
            return "usercard";
        }
        return "index";
    }

    @RequestMapping(value = "/exit", method = RequestMethod.POST)
    public String exit(HttpServletResponse response,
                       @CookieValue(value = "user_code", required = false) Cookie cookieUserCode) {

        cookieUserCode.setMaxAge(0);
        response.addCookie(cookieUserCode);

        return "index";
    }

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "ITP_Project") String name, Model model) {
        model.addAttribute("users", getAllUsers());
        model.addAttribute("documents", getAllDocuments());
        model.addAttribute("orders", orderService.getOpenOrdersByUserId(1).toString());
        userService.add(new User("ttt","ttt","ttt","rrr","disabled"));

        return "welcome";
    }
}
