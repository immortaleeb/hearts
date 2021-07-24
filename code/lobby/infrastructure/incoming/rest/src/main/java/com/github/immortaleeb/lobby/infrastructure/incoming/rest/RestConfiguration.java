package com.github.immortaleeb.lobby.infrastructure.incoming.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.shared.LobbyId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;

@EnableWebMvc
@Configuration
public class RestConfiguration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jackson2HttpMessageConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9000");
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        var converter = new MappingJackson2HttpMessageConverter();
        var builder = jacksonBuilder();
        builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
        builder.modules(customSerialization());
        converter.setObjectMapper(builder.build());
        return converter;
    }

    private SimpleModule customSerialization() {
        var simpleModule = new SimpleModule();
        simpleModule.addSerializer(LobbyId.class, new LobbyIdSerializer());
        simpleModule.addDeserializer(LobbyId.class, new LobbyIdDeserializer());
        simpleModule.addSerializer(PlayerId.class, new PlayerIdSerializer());
        simpleModule.addDeserializer(PlayerId.class, new PlayerIdDeserializer());
        return simpleModule;
    }

    private Jackson2ObjectMapperBuilder jacksonBuilder() {
        var builder = new Jackson2ObjectMapperBuilder();
        builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return builder;
    }

    public static class LobbyIdSerializer extends JsonSerializer<LobbyId> {
        @Override
        public void serialize(LobbyId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.asString());
        }

    }

    public static class LobbyIdDeserializer extends JsonDeserializer<LobbyId> {
        @Override
        public LobbyId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LobbyId.of(p.readValueAs(String.class));
        }
    }

    public static class PlayerIdSerializer extends JsonSerializer<PlayerId> {
        @Override
        public void serialize(PlayerId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.asString());
        }
    }

    public static class PlayerIdDeserializer extends JsonDeserializer<PlayerId> {
        @Override
        public PlayerId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return PlayerId.of(p.readValueAs(String.class));
        }
    }
}
