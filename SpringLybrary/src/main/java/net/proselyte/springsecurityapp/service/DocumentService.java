package net.proselyte.springsecurityapp.service;

import net.proselyte.springsecurityapp.model.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DocumentService {
    public List<Document> getAllDocuments();

    public Document getDocumentByOrderId();

    public Document get(int id);
    public void save(Document document);
    public void delete(Document document);
}
