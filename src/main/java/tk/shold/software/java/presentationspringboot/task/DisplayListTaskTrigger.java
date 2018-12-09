package tk.shold.software.java.presentationspringboot.task;

import lombok.extern.java.Log;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.util.Date;
import java.util.Objects;

@Log
public class DisplayListTaskTrigger implements Trigger {

    private final Date start;
    private final Date end;

    private final DisplayListTask displayListTask;

    public DisplayListTaskTrigger(Date start, Date end, DisplayListTask displayListTask) {
        this.start = start;
        this.end = end;
        this.displayListTask = displayListTask;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        final Date nextDate = triggerContext.lastScheduledExecutionTime() == null ?
                start :
                new Date(Objects.requireNonNull(triggerContext.lastScheduledExecutionTime()).getTime()
                        + displayListTask.getPrevious().getDelay());
        return (end != null && nextDate.after(end)) ? null : nextDate;
    }
}
