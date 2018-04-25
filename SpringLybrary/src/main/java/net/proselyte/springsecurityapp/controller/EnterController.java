package net.proselyte.springsecurityapp.controller;

import net.proselyte.springsecurityapp.model.User;
import net.proselyte.springsecurityapp.service.LogServiceC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class EnterController extends net.proselyte.springsecurityapp.controller.Controller {

    @Autowired
    protected LogServiceC logServiceC=new LogServiceC();

    /**
     * checking if there is such user in the system and matching password, creating cookie
     * @param email
     * @param password
     * @param response
     * @return user card page if successfully, else "false" string
     */
    @RequestMapping(value = "/enter", method = POST)
    public String enter(@RequestParam(value = "email", required = false, defaultValue = "Not found") String email,
                        @RequestParam(value = "password", required = false, defaultValue = "Not found") String password,
                        HttpServletResponse response) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User u = userService.get(email);
        Boolean passwordValidation;
        //-----catch mistakes
        if (u == null) return "false";
        passwordValidation = encoder.matches(password, u.getPassword());
        if (!passwordValidation) return "false";
        //create cookie----


        createNewCookieForUser(email, response);

        logServiceC.save("user "+email+" entered");
        //-------create page
        return createUserCardPage(u.getId());
    }

}
