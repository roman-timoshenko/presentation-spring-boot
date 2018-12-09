package tk.shold.software.java.presentationspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Displayable {
    private String path;
    private int delay;
}
