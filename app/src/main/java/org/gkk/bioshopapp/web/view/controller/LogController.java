package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.service.model.log.LogServiceModel;
import org.gkk.bioshopapp.service.service.LogService;
import org.gkk.bioshopapp.web.annotation.PageTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class LogController extends BaseController {
    private static final Integer DEFAULT_PAGE_SIZE = 10;

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Logs")
    public String getAllOrders(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("time").descending());

        Page<LogServiceModel> logPage = this.logService.getPaginated(pageable);
        model.addAttribute("logPage", logPage);

        int totalPages = logPage.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "log/logs";
    }
}
