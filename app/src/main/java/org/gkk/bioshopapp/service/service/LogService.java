package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.service.model.log.LogServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface LogService {

    void seedLogInDb(LogServiceModel logServiceModel);

    void deleteAllByTimeLessThanEqual(LocalDateTime localDateTime);

    Page<LogServiceModel> getPaginated(Pageable pageable);

}
