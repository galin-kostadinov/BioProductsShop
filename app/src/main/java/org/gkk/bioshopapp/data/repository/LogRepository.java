package org.gkk.bioshopapp.data.repository;

import org.gkk.bioshopapp.data.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogRepository extends JpaRepository<Log, String> {

    Optional<Log> findByUsername(String username);
    
}
