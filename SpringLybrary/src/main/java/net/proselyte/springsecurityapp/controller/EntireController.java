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
public class EntireController extends net.proselyte.springsecurityapp.controller.Controller
{
    @Autowired
    private UserService userService;

    /**
     * check queue, if within one day user haven`t take document status sets closed
     * @param request
     * @param cookieUserCode
     * @param model
     * @return main page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(//@RequestParam(value = "name", required = false, defaultValue = "ITP_Project") String name,
                        HttpServletRequest request,
                        @CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                        Model model) {
        Date date = new Date();

        if (cookieUserCode != null) {
            User u = userService.getByCookie(cookieUserCode.getValue());
            if (u == null) return "index";

            //check queue
            List<Document> documentList = documentService.getAllDocuments();
            int userId;
            long startTime;
            for (Order or:orderService.getOrdersByUserAndStatus(u,"queue")){
                if (u.getPositionInQueue(getQueueForDocument(documentService.get(or.getItemId())))== 0
                        && documentService.get(or.getItemId()).getAmount()>0){

                    or.setStatus("waitForAccept");
                    startTime = or.getStartTime();
                    or.setStartTime(or.getFinishTime());
                    or.setFinishTime(or.getFinishTime()+or.getFinishTime()-startTime);
                    orderService.save(or);
                }
            }


            for (Order order: orderService.getOrdersByStatus("waitForAccept")){
                if (date.getTime() - order.getStartTime() > 3600000*24) order.setStatus("closed");
                orderService.save(order);
            }


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

    /**
     * delet cookie and exit
     * @param response
     * @param cookieUserCode
     * @return main page
     */
    @RequestMapping(value = "/exit", method = RequestMethod.POST)
    public String exit(HttpServletResponse response,
                       @CookieValue(value = "user_code", required = false) Cookie cookieUserCode) {

        cookieUserCode.setMaxAge(0);
        response.addCookie(cookieUserCode);

        return "index";
    }


    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "ITP_Project") String name, Model model) {
        model.addAttribute("users", getAllUsers());
        model.addAttribute("documents", getAllDocuments());
        model.addAttribute("orders", orderService.getOpenOrdersByUserId(1).toString());
        userService.add(new User("ttt","ttt","ttt","rrr","disabled"));

        return "welcome";
    }
}
