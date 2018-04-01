package net.proselyte.springsecurityapp.controller;

import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.Order;
import net.proselyte.springsecurityapp.model.User;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
public class AdminController extends Controller {

    @RequestMapping(value = "/addDocument", method = RequestMethod.POST)
    public String addDocument(@RequestParam(value = "title", required = false, defaultValue = "Not found") String title,
                                     @RequestParam(value = "author", required = false, defaultValue = "Not found") String author,
                                     @RequestParam(value = "publisher", required = false, defaultValue = "Not found") String publisher,
                                     @RequestParam(value = "note", required = false, defaultValue = "Not found") String description,
                                     @RequestParam(value = "year", required = false, defaultValue = "0") String year,
                                     @RequestParam(value = "status", required = false, defaultValue = "Not found") String status,
                                     @RequestParam(value = "edition", required = false, defaultValue = "Not found") String edition){
        Document d = new Document(title,author,status,0,description,"#","book",Integer.parseInt(year),publisher,edition);
        documentService.save(d);
        return "true";
    }

    @RequestMapping(value = "/addAV", method = RequestMethod.POST)
    public String addDocument(@RequestParam(value = "title", required = false, defaultValue = "Not found") String title,
                                     @RequestParam(value = "author", required = false, defaultValue = "Not found") String author) {

        Document d = new Document(title,author,"AV",0,"none","#","av",0,"none","none");
        documentService.save(d);
        return "true";
    }

    @RequestMapping(value = "/modifyDocument", method = RequestMethod.POST)
    public String modifyDocument(@RequestParam(value = "documentId") String id,
                                        @RequestParam(value = "title") String title,
                                        @RequestParam(value = "author") String author,
                                        @RequestParam(value = "publisher") String publisher,
                                        @RequestParam(value = "note", required = false, defaultValue = "Not_found") String description,
                                        @RequestParam(value = "edition", required = false, defaultValue = "Not_found") String edition,
                                        @RequestParam(value = "year", required = false, defaultValue = "Not_found") String year){
        Document d = documentService.get(Integer.parseInt(id));
        if (d ==null) return "false";//does not exist such book
        d.setTitle(title);
        d.setAuthor(author);
        d.setPublisher(publisher);
        d.setDescription(description);
        d.setEdition(edition);
        d.setYear(Integer.parseInt(year));

        documentService.save(d);
        return "true";
    }

    @RequestMapping(value = "/addNewUser", method = RequestMethod.POST)
    public String addNewuser(@RequestParam(value = "password", required = false, defaultValue = "No password") String password,
                                    @RequestParam(value = "name", required = false, defaultValue = "No name") String name,
                                    @RequestParam(value = "email", required = false, defaultValue = "No email") String email,
                                    @RequestParam(value = "surname", required = false, defaultValue = "No surname") String surname,
                                    @RequestParam(value = "status", required = false, defaultValue = "No status") String status) {
        boolean check = addNewUserToTheSystem(name,surname,email,password,status);
        return check?"true":"false";
    }

    @RequestMapping(value = "/modifyUser", method = RequestMethod.POST)
    public String modifyUser(@RequestParam(value = "id", required = false, defaultValue = "No id") String id,
                                    @RequestParam(value = "name", required = false, defaultValue = "No name") String name,
                                    @RequestParam(value = "address", required = false, defaultValue = "No address") String address,
                                    @RequestParam(value = "phone", required = false, defaultValue = "No phone") String phone,
                                    @RequestParam(value = "type", required = false, defaultValue = "No type") String status){
        String surname = name.substring(name.indexOf(' ')+1, name.length());
        name = name.substring(0,name.indexOf(' '));
        User u = userService.get(Integer.parseInt(id));
        if (u == null) return "false";//does not exist such user
        u.setName(name);
        u.setSurname(surname);
        u.setAddress(address);
        u.setPhone(phone);
        u.setStatus(status);
        userService.save(u);
        return "true";
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUser(@RequestParam(value = "id", required = false, defaultValue = "0") String id) {
        User u = userService.get(Integer.parseInt(id));
        if (u == null) return "false";//does not exist such user
        userService.delete(u);
        return "true";
    }

    @RequestMapping(value = "/deleteDocumentById", method = RequestMethod.POST)
    public String deleteDocumentById(@RequestParam(value = "id", required = false, defaultValue = "0") String id){
        Document d = documentService.get(Integer.parseInt(id));
        if (d==null) return "false";//does not exist such book
        documentService.delete(d);
        return "true";
    }

 /*   @RequestMapping(value = "/deleteDocumentByParameters", method = RequestMethod.POST)
    public String deleteDocumentByParameters(@RequestParam(value = "title", required = false, defaultValue = "Not found") String title,
                                                    @RequestParam(value = "author", required = false, defaultValue = "Not found") String author,
                                                    @RequestParam(value = "type", required = false, defaultValue = "Not found") String type) {
        DBHandler db;
        db = new DBHandler();
        Statement statement = db.getConnection().createStatement();

        String getQuery = "select * from documents where title = '" + title + "' and author = '" + author + "' and type = '" + type + "'";
        ResultSet resultSet = statement.executeQuery(getQuery);
        if (!resultSet.next()) return "false";//already exist such book

        String query = "DELETE from documents "+
                "where title = '" + title + "' and author = '" + author + "' and type = '" + type + "'";
        statement.execute(query);
        return "true";
    }
*/
    @RequestMapping(value = "/closeOrder", method = RequestMethod.POST)
    public String closeOrder(@RequestParam(value = "orderId", required = false, defaultValue = "Not found") String orderId){

        Order or = orderService.get(Integer.parseInt(orderId));
        Document document = documentService.get(or.getItemId());
        if (!or.getStatus().equals("queue")) document.setAmount(document.getAmount()+1);
        if (or == null) return "false";//does not exist such order
        or.setStatus("closed");
        orderService.save(or);
        documentService.save(document);
        return "true";
    }

    @RequestMapping(value = "/queueRequest", method = RequestMethod.POST)
    public String queueRequest(@RequestParam(value = "orderId", required = false, defaultValue = "Not found") String orderId){

        Order or = orderService.get(Integer.parseInt(orderId));
        if (documentService.get(or.getItemId()).getAmount() == 0) return "false";
        or.setStatus("waitForAccept");
        long start = or.getStartTime();
        or.setStartTime(or.getFinishTime());
        or.setFinishTime(or.getFinishTime()+or.getFinishTime()-or.getStartTime());
        orderService.save(or);
        return "true";
    }

}
