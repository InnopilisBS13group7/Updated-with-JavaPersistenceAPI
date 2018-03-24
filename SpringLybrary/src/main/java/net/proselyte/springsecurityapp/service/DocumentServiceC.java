package net.proselyte.springsecurityapp.service;

import net.proselyte.springsecurityapp.dao.DocumentRepository;
import net.proselyte.springsecurityapp.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceC implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

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
}
