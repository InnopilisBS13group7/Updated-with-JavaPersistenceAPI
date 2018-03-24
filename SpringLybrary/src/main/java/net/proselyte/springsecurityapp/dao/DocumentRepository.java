package net.proselyte.springsecurityapp.dao;

import net.proselyte.springsecurityapp.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

}
