package tk.shold.software.java.presentationspringboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableCaching
@EnableScheduling
public class ApplicationConfiguration {

    @Bean
    @Qualifier("YAMLObjectMapper")
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

}
