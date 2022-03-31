package account.controller;

import account.model.Logging;
import account.service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security")
public class StatisticController {


    @Autowired
    LoggingService loggingService;

    @GetMapping("/events")
    public List<Logging> getLogInfo() {
        return loggingService.getAllEvents();
    }

}
