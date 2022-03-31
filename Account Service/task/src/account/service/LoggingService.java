package account.service;

import account.model.Event;
import account.model.Logging;
import account.repos.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoggingService {

    @Autowired
    private LogRepository logRepository;

    public List<Logging> getAllEvents() {
        return logRepository.findAll();
    }

    public void log(Event event, String subject, String object, String path) {
        Logging log = new Logging();
        log.setDate(LocalDate.now());
        log.setAction(event);
        log.setObject(object);
        log.setSubject(subject);
        log.setPath(path);
        logRepository.save(log);
    }
}
