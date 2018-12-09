package tk.shold.software.java.presentationspringboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Lazy(false)
public class PostConstructService {
    private final ScheduleService scheduleService;

    @Autowired
    public PostConstructService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostConstruct
    public void postConstruct() {
        //scheduleService.reload();
    }
}
