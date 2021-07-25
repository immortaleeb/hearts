package com.github.immortaleeb.hearts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.GameId;
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
import java.util.function.Function;

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
        simpleModule.addSerializer(LobbyId.class, new SimpleSerializer<>(LobbyId::asString));
        simpleModule.addDeserializer(LobbyId.class, new SimpleDeserializer<>(LobbyId::of));
        simpleModule.addSerializer(PlayerId.class, new SimpleSerializer<>(PlayerId::asString));
        simpleModule.addDeserializer(PlayerId.class, new SimpleDeserializer<>(PlayerId::of));
        simpleModule.addSerializer(GameId.class, new SimpleSerializer<>(GameId::asString));
        simpleModule.addDeserializer(GameId.class, new SimpleDeserializer<>(GameId::of));
        return simpleModule;
    }

    private Jackson2ObjectMapperBuilder jacksonBuilder() {
        var builder = new Jackson2ObjectMapperBuilder();
        builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return builder;
    }

    public static class SimpleSerializer<T> extends JsonSerializer<T> {
        private final Function<T, String> asString;

        public SimpleSerializer(Function<T, String> asString) {
            this.asString = asString;
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(asString.apply(value));
        }
    }

    public static class SimpleDeserializer<T> extends JsonDeserializer<T> {
        private final Function<String, T> parse;

        public SimpleDeserializer(Function<String, T> parse) {
            this.parse = parse;
        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return parse.apply(p.readValueAs(String.class));
        }
    }

}
