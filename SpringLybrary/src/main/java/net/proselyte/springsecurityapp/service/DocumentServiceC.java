package net.proselyte.springsecurityapp.service;

import net.proselyte.springsecurityapp.dao.DocumentRepository;
import net.proselyte.springsecurityapp.dao.OrderRepository;
import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.Log;
import net.proselyte.springsecurityapp.model.Order;
import net.proselyte.springsecurityapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class DocumentServiceC implements DocumentService {

    @Autowired
    private LogService logService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    protected UserService userService;

    @Autowired
    protected OrderService orderService;

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Document
    getDocumentByOrderId() {
        return null;
    }

    @Override
    public Document get(int id) {
        return documentRepository.getOne(id);
    }

    @Override
    public void save(Document document) {
            documentRepository.save(document);
    }

    @Override
    public boolean save(User librarian,Document document) {
        if(librarian.getStatus().equals("admin")|| librarian.getStatus().equals("lib2")||librarian.getStatus().equals("lib3")){
            documentRepository.save(document);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Document document){
        documentRepository.delete(document);
    }

    @Override
    public boolean deleteSome(User librarian,Document document, int k) {
        logService.save(librarian,"deleted "+k+" of "+document.getTitle());
        if(librarian.getStatus().equals("admin")|| librarian.getStatus().equals("lib3")){
            if (document.getAmount()-k<1)
                documentRepository.delete(document);
            else document.setAmount(document.getAmount()-k);
            return true;
        }
        return false;
    }

    @Override
    public String returnDocument(int orderId) {
        Order or = orderRepository.findOne(orderId);
        or.setStatus("finished");
        orderRepository.save(or);
        return "true";
    }

    @Override
    public List<User> getQueueForDocument(Document document) {
        List<Order> queue = orderService.getQueue(document.getId());
        List<User> users = new LinkedList<>();
        for (Order or : queue) {
            users.add(userService.get(or.getUserId()));
        }
        List<User> students = new LinkedList<>();
        List<User> instructors = new LinkedList<>();
        List<User> tas = new LinkedList<>();
        List<User> visitingProfessors = new LinkedList<>();
        List<User> professors = new LinkedList<>();
        for (User u : users) {
            if (u.getStatus().equals("student")) {
                students.add(u);
                continue;
            }
            if (u.getStatus().equals("instructor")) {
                instructors.add(u);
                continue;
            }
            if (u.getStatus().equals("ta")) {
                tas.add(u);
                continue;
            }
            if (u.getStatus().equals("professor")) {
                professors.add(u);
                continue;
            }
            if (u.getStatus().equals("visitingProfessor"))
                visitingProfessors.add(u);

        }
        students.addAll(instructors);
        students.addAll(tas);
        students.addAll(visitingProfessors);
        students.addAll(professors);
        return students;
    }

    @Override
    public String queueRequest(User librarian,Document d){
        if(!(librarian.getStatus().equals("admin")|| librarian.getStatus().equals("lib2")||librarian.getStatus().equals("lib3")))
            return "false";
        for (Order o: orderService.getOrdersByItemIdAndStatus(d.getId(),"open")){
            //TODO: послать уведомления чтобы вернули книжки
            o.setStatus("closed");
            o.setFinishTime(System.currentTimeMillis());
            orderRepository.save(o);
        }
        //послать уведомления

        //deieting queue
        for (Order o:orderService.getQueue(d.getId())){
            o.setStatus("closed");
            o.setFinishTime(System.currentTimeMillis());
            orderRepository.save(o);
        }
        return "true";
    }
}
