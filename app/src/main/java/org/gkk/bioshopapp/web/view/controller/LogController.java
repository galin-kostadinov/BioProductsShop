package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.service.model.log.LogServiceModel;
import org.gkk.bioshopapp.service.service.LogService;
import org.gkk.bioshopapp.web.annotation.PageTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logs")
public class LogController extends BaseController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Logs")
    public String getAllLogs(Model model, Pageable pageable) {
        Page<LogServiceModel> logServiceModels = this.logService.getPaginated(pageable);
        model.addAttribute("logServiceModels", logServiceModels);

        return "log/logs";
    }
}
