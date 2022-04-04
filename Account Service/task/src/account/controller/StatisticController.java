package account.controller;

import account.model.Logging;
import account.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security")
@Secured({"ROLE_AUDITOR"})
public class StatisticController {

    @Autowired
    private LoginAttemptService loggingService;

    @GetMapping("/events")
    public List<Logging> getLogInfo() {
        return loggingService.getAllEvents();
    }

}
