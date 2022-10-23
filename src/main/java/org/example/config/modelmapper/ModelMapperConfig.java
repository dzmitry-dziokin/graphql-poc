package org.example.config.modelmapper;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.LOOSE)
            .setSkipNullEnabled(true)
            .setFieldAccessLevel(PRIVATE);
        return mapper;
    }

    @Slf4j
    @Component
    private static class ModelMapperValidator implements ApplicationListener<ContextRefreshedEvent> {

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            log.info("Validating model mapper...");
            event.getApplicationContext().getBean(ModelMapper.class).validate();
            log.info("Model mapper configuration validated successfully");
        }
    }

}
