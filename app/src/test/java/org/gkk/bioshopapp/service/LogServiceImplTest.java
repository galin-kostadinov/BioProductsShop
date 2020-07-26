package org.gkk.bioshopapp.service;

import org.gkk.bioshopapp.base.TestBase;
import org.gkk.bioshopapp.data.model.Log;
import org.gkk.bioshopapp.data.repository.LogRepository;
import org.gkk.bioshopapp.service.model.log.LogServiceModel;
import org.gkk.bioshopapp.service.service.LogService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LogServiceImplTest extends TestBase {

    @MockBean
    LogRepository logRepository;

    @Autowired
    LogService logService;

    @Test
    public void seedLogInDb_shouldSaveLogInDb() {
        String username = UUID.randomUUID().toString();
        String description = UUID.randomUUID().toString();
        String editedElementId = UUID.randomUUID().toString();

        LogServiceModel logServiceModel = new LogServiceModel(username, description, editedElementId, LocalDateTime.now());

        logService.seedLogInDb(logServiceModel);

        ArgumentCaptor<Log> argumentLog = ArgumentCaptor.forClass(Log.class);
        Mockito.verify(logRepository).saveAndFlush(argumentLog.capture());

        Log log = argumentLog.getValue();

        assertNotNull(log);
        assertEquals(logServiceModel.getUsername(), log.getUsername());
        assertEquals(logServiceModel.getPropertyId(), log.getPropertyId());
        assertEquals(logServiceModel.getDescription(), log.getDescription());
    }

    @Test
    public void getPaginated_() {
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);

        List<Log> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(new Log("username" + i, UUID.randomUUID().toString(), UUID.randomUUID().toString(), LocalDateTime.now()));
        }

        Page<Log> logServiceModelPage = new PageImpl<>(list, pageable, list.size());

        Mockito.when(logRepository.findAll(pageable)).thenReturn(logServiceModelPage);

        Page<LogServiceModel> pageResult = logService.getPaginated(pageable);

        assertNotNull(pageResult);
        assertEquals(10, pageResult.getTotalElements());

        int index = 0;
        for (LogServiceModel currpage : pageResult) {
            assertEquals(list.get(index).getUsername(), currpage.getUsername());
            assertEquals(list.get(index).getDescription(), currpage.getDescription());
            assertEquals(list.get(index).getTime(), currpage.getTime());
            assertEquals(list.get(index).getPropertyId(), currpage.getPropertyId());

            index++;
        }
    }
}