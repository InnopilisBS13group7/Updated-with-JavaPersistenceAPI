package net.proselyte.springsecurityapp.controller;

import net.proselyte.springsecurityapp.model.User;
import net.proselyte.springsecurityapp.service.LogServiceC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RegistrationController extends Controller {

    @Autowired
    protected LogServiceC logServiceC=new LogServiceC();
    /**
     * if there is no such user creates new user account
     * @param name
     * @param surname
     * @param email
     * @param password
     * @param response
     * @return user card page
     * @throws SQLException
     */
    @RequestMapping(value = "/registration", method = POST)
    public String registration(@RequestParam(value = "name", required = false, defaultValue = "Not found") String name,
                               @RequestParam(value = "surname", required = false, defaultValue = "Not found") String surname,
                               @RequestParam(value = "email", required = false, defaultValue = "Not found") String email,
                               @RequestParam(value = "password", required = false, defaultValue = "Not found") String password,
                               HttpServletResponse response) throws SQLException {

        Boolean check = addNewUserToTheSystem(name, surname, email, password,"student");
        if (!check) return "false";
        User u = userService.get(email);
        //-------create Cookie

        createNewCookieForUser(email, response);

        logServiceC.save("user "+name+" "+surname+" registered");
        //------create page
        return createUserCardPage(u.getId());
    }


}
