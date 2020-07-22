package org.gkk.bioshopapp.data.repository;

import org.gkk.bioshopapp.data.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {

    Optional<Log> findByUsername(String username);

    void deleteAllByTimeLessThanEqual(LocalDateTime time);

    List<Log> findAllByOrderByTimeDesc();

}
