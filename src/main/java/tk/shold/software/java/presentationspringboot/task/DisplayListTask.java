package tk.shold.software.java.presentationspringboot.task;

import lombok.extern.java.Log;
import tk.shold.software.java.presentationspringboot.model.Displayable;

import java.text.MessageFormat;
import java.util.List;

@Log
public class DisplayListTask implements Runnable {

    // map of path to delay
    private final List<Displayable> displayables;

    private int last = 0;

    public DisplayListTask(List<Displayable> displayables) {
        this.displayables = displayables;
    }

    public Displayable getCurrent() {
        return displayables.get(last);
    }

    public Displayable getPrevious() {
        return displayables.get(last == 0 ? displayables.size() - 1 : last - 1);
    }

    @Override
    public void run() {
        log.info(() -> MessageFormat.format("Displaying {0} of \"{1}\" items: \"{2}\"", last, displayables.size(), getCurrent().getPath()));
        // ...
        last = (last + 1) % displayables.size();
    }
}
