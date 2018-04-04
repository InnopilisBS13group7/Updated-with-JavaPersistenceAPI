package net.proselyte.springsecurityapp.service;

import net.proselyte.springsecurityapp.dao.DocumentRepository;
import net.proselyte.springsecurityapp.dao.OrderRepository;
import net.proselyte.springsecurityapp.model.Document;
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
    public Document getDocumentByOrderId() {
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
    public void delete(Document document) {
        documentRepository.delete(document);
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
    public String queueRequest(Document d){
        for (Order o: orderService.getOrdersByItemIdAndStatus(d.getId(),"open")){
            //послать уведомления чтобы вернули книжки
            o.setStatus("finished");
            o.setFinishTime(System.currentTimeMillis());
            orderRepository.save(o);
        }
        //послать уведомления

        //deieting queue
        for (Order o:orderService.getQueue(d.getId())){
            orderService.delete(o);
        }
        return "true";
    }
}
