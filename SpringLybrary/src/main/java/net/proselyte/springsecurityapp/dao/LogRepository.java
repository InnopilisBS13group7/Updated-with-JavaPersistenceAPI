package net.proselyte.springsecurityapp.dao;

import net.proselyte.springsecurityapp.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Integer> {

}
