package tk.shold.software.java.presentationspringboot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tk.shold.software.java.presentationspringboot.model.Rule;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Log
public class RulesService {

    private final ObjectMapper objectMapper;
    private final String rulesFilePath;

    @Autowired
    public RulesService(@Qualifier("YAMLObjectMapper") ObjectMapper objectMapper,
                        @Value("${scheduler.rules.path}") String rulesFilePath) {
        this.objectMapper = objectMapper;
        this.rulesFilePath = rulesFilePath;
    }

    @Cacheable("rules")
    public List<Rule> getRules() throws IOException {
        log.info("Fetching rules...");
        return objectMapper.readValue(new File(rulesFilePath), new TypeReference<List<Rule>>() {});
    }

    @CacheEvict(value = "rules", allEntries = true)
    @Scheduled(fixedRate = 3600000)
    public void cacheEvict() {
    }

}
