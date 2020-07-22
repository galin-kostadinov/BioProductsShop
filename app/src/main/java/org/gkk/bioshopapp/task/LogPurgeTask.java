package org.gkk.bioshopapp.task;

import org.gkk.bioshopapp.service.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class LogPurgeTask {

    private final LogService logService;

    @Autowired
    public LogPurgeTask(LogService logService) {
        this.logService = logService;
    }

    @Scheduled(cron = "${bioshopapp.log.default.purge.cron.expression}")
    public void purgeExpired() {
        this.logService.deleteAllByTimeLessThanEqual(LocalDateTime.now().minusMonths(1));
    }
}
