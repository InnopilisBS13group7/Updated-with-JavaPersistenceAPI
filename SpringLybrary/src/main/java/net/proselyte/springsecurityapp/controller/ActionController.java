package net.proselyte.springsecurityapp.controller;

import net.proselyte.springsecurityapp.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.sql.SQLException;
import java.util.List;

@RestController
public class ActionController extends Controller {

    /**
     * to go to profile page
     * @param cookieUserCode individual cookie of user
     * @return user card page
     */

    @RequestMapping(value = "/toProfile", method = RequestMethod.POST)
    public String toProfile(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode) {
        if (isCookieWrong(cookieUserCode)) return "false";
        int userId = getIdFromCookie(cookieUserCode.getValue());
//        String notify = "<script>$(\"#alert_message\").text(\"Poshel Nahui\");"+
//        "$(\"#alert_back\").slideDown(0).animate({\"opacity\":\"1\"}, 200);</script>";
        return createUserCardPage(userId);
    }

    /**
     * to go to settings page
     * @param cookieUserCode individual cookie of user
     * @return settings page
     */
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String settings(@CookieValue(value = "user_code", required = true) Cookie cookieUserCode) {
        if (isCookieWrong(cookieUserCode)) return "false";

        User u = getClientUserObject(getIdFromCookie(cookieUserCode.getValue()));
        String div = "<div id=settings_block>" +
                (isLibrarian(u.getStatus()) ? "<div id=settings_type_menu>" +
                        "<div class=settings_type id=settings_type_profile>Profile</div>" +
                        "<div class=settings_type id=settings_type_users>Users</div>" +
                        "<div class=settings_type id=settings_type_orders>Orders</div>" +
                        ((u.getStatus().equals("admin"))?"<div class=settings_type id=settings_type_history>History</div>" : "") +
                        "<div id=settings_type_line></div>" +
                        "</div>" : "") +





                "<div class=settings_type_box id=settings_profile>" +
                "<p class=setting_parameter_name><b>Change name</b></p>" +
                "<input type=text class=settings_input id=settings_name placeholder=\"New name\" style=\"margin-top:-8px\" value=\"" + u.getName() + "\" />" +
                "<input type=text class=settings_input id=settings_surname placeholder=\"New surname\" value=\"" + u.getSurname() + "\" />" +
                "<p class=setting_parameter_name style=\"margin-top:16px\"><b>Change password</b></p>" +
                "<input type=password class=settings_input id=settings_current_password placeholder=\"Current password\" style=\"margin-top:-8px\" />" +
                "<input type=password class=settings_input id=settings_new_password placeholder=\"New password\" />" +
                "<p class=setting_parameter_name style=\"margin-top:16px\"><b>Change adress</b></p>" +
                "<input type=text class=settings_input id=settings_adress placeholder=\"Your adress\" style=\"margin-top:-8px\" value=\""+u.getAddress()+"\" />" +
                "<p class=setting_parameter_name style=\"margin-top:16px\"><b>Change phone number</b></p>" +
                "<input type=text class=settings_input id=settings_phone placeholder=\"Phone number\" style=\"margin-top:-8px\" value=\""+u.getPhone()+"\" />" +
                "<p class=setting_parameter_name style=\"margin-top:16px\"><b>Your id is "+u.getId()+"</b></p>" +
                "<p class=setting_parameter_name style=\"margin-top:16px\"><b>Your type is "+u.getStatus()+"</b></p>" +
                "<div id=settings_profile_save>Save</div>" +
                "</div>" +
                createListOfUsersBlock(getAllUsers(),u) +

                createListOfOrdersBlock(getAllOrders()) +

                createListOfHistoryBlock(getAllHistory())+

                "<div id=settings_alert>Changes are successfully saved</div>" +

                "</div>";
        return div;
    }

    /**
     *
     * @param cookieUserCode individual cookie of user
     * @param userId id num in userlist
     * @param configString string from browser
     * @return page with orders
     */
    @RequestMapping(value = "/ordersSearch", method = RequestMethod.POST)
    public String ordersSearch(@CookieValue(value = "user_code", required = false) Cookie cookieUserCode,
                               @RequestParam(value = "id", required = false, defaultValue = "0") int userId,
                               @RequestParam(value = "type") String configString) {
        if (isCookieWrong(cookieUserCode)) return "false";

        return createListOfOrdersBlock(getAllOrders(),configString,userId);
    }
}


