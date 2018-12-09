package tk.shold.software.java.presentationspringboot.service;

import lombok.extern.java.Log;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Service
public class DirectoryParserService {

    @Cacheable("folders")
    public List<String> getFileInDir(String path) {

        log.info(() -> MessageFormat.format("Fetching files for path: \"{0}\"", path));
        try {
            return Files.walk(Paths.get(path))
                    .filter(f -> f.toFile().isFile())
                    .filter(f ->
                            f.toString().endsWith(".jpg") ||
                            f.toString().endsWith(".png") ||
                            f.toString().endsWith(".bmp")
                    )
                    .map(f -> f.toAbsolutePath().toString())
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getSubfoldersInDir(String path)
    {
        log.info(() -> MessageFormat.format("Fetching directories in: \"{0}\"", path));
        try {

                return Files.walk(Paths.get(path))
                        .filter(f -> f.toFile().isDirectory())
                        .filter(f -> !f.equals(Paths.get(path)))
                        .map(f -> f.toAbsolutePath().toString())
                        .sorted()
                        .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
