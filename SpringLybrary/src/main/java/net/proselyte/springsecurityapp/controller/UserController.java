package net.proselyte.springsecurityapp.controller;

import net.proselyte.springsecurityapp.model.Order;
import net.proselyte.springsecurityapp.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import java.sql.ResultSet;
import java.util.Date;

@RestController
public class UserController extends Controller {


    @RequestMapping(value = "/profileSettings", method = RequestMethod.POST)
    public String profileSettings(@RequestParam(value = "name", required = false, defaultValue = "") String name,
                                    @RequestParam(value = "surname", required = false, defaultValue = "") String surname,
                                    @RequestParam(value = "address", required = false, defaultValue = "") String address,
                                    @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
                                    @RequestParam(value = "newPassword", required = false, defaultValue = "") String newPassword,
                                    @RequestParam(value = "currentPassword", required = false, defaultValue = "") String currentPassword,
                                         @CookieValue(value = "user_code", required = false) Cookie cookieUserCode){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User u = userService.get(getIdFromCookie(cookieUserCode.getValue()));
        if (u == null) return "false";
        int id = u.getId();
        String encodedOldPassword = u.getPassword();
        String encodedNewPassword = encoder.encode(newPassword);
        String query;
        if (!currentPassword.equals("")) {
            Boolean passwordValidation = encoder.matches(currentPassword, encodedOldPassword);
            if (!passwordValidation) return "false";
            else if (newPassword.length()>=8) {
                u.setName(name);
                u.setSurname(surname);
                u.setAddress(address);
                u.setPhone(phone);
                u.setPassword(encodedNewPassword);
            } else {
                u.setName(name);
                u.setSurname(surname);
                u.setAddress(address);
                u.setPhone(phone);
            }
        } else {
            u.setName(name);
            u.setSurname(surname);
            u.setAddress(address);
            u.setPhone(phone);
        }
        userService.save(u);
        return "true";
    }

    @RequestMapping(value = "/acceptDocument", method = RequestMethod.POST)
    public String acceptDocument(@RequestParam(value = "orderId", required = false, defaultValue = "Not found") String orderId){

        Order or = orderService.get(Integer.parseInt(orderId));
        or.setStatus("open");
        Date date = new Date();
        long start = date.getTime();
        or.setFinishTime(start+or.getFinishTime()-or.getStartTime());
        or.setStartTime(start);
        orderService.save(or);
        return "true";
    }

}
