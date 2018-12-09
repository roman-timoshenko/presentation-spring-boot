package tk.shold.software.java.presentationspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class PresentationUnit {
    private final List<String> files;
    private final String category;
}
