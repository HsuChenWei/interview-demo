package com.interview.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;


@Configuration
public class DataBindConfiguration {

    @Bean
    public ModelMapper modelMapper(ObjectMapper objectMapper) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setAmbiguityIgnored(true);

        return modelMapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder() {

            @Override
            public void configure(ObjectMapper objectMapper) {
                super.configure(objectMapper);
                // using ISO8601 datetime format
                objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.NONE);
                objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.PUBLIC_ONLY);
                objectMapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.PUBLIC_ONLY);
                objectMapper.registerModule(new Jdk8Module());
            }

        };
    }

}
