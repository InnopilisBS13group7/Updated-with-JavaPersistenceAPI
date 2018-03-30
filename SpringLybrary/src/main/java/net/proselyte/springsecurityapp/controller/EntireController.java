package net.proselyte.springsecurityapp.controller;

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

        if (cookieUserCode != null) {
            User u = userService.getByCookie(cookieUserCode.getValue());
            if (u == null) return "index";


            //create page-----
            long keepingTime;
            int i = 0;
            int margin = -5;
            String booki = "";
            for (Order or: orderService.getOpenOrdersByUserId(u.getId())) {
                i++;
                keepingTime = or.getFinishTime();
                booki = booki + "<div class=\"books\" style=\"margin-left:" + margin + "px\"> " +
                        "<div class=books_inside>" + getDate(keepingTime) +
                        "<div class=return_book id=" + or.getId() + ">Return the book</div></div>" +
                        "<img src=\"/resources/img/books/1.jpg\" width=\"190px\" height=\"289px\" /> " +
                        "<p class=\"bookname\">" + "3 PIGS</p> " +
                        "</div>";
                margin += 198;
                if (i % 4 == 0) margin = -5;

            }


            model.addAttribute("name", u.getName()+" "+u.getSurname());
            model.addAttribute("status", u.getStatus());
            model.addAttribute("fine", u.getFine()+"$");
            model.addAttribute("booki", createUserHistoryBlock(u));
            model.addAttribute("address", u.getAddress());
            model.addAttribute("id", u.getId());
            model.addAttribute("phone", u.getPhone());
            //if <check>


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
