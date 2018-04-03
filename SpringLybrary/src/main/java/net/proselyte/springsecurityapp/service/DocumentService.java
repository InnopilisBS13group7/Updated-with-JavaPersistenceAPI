package net.proselyte.springsecurityapp.service;

import net.proselyte.springsecurityapp.model.Document;
import net.proselyte.springsecurityapp.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DocumentService {
    public List<Document> getAllDocuments();

    public Document getDocumentByOrderId();

    public Document get(int id);
    public void save(Document document);
    public void delete(Document document);

    public String returnDocument(int orderId);
    public List<User> getQueueForDocument(Document document);
}
