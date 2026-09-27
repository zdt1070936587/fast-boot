package com.fastboot.common.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;

/**
 * {@link Sensitive} 序列化器。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private final Sensitive.Strategy strategy;

    public SensitiveSerializer() {
        this(Sensitive.Strategy.DEFAULT);
    }

    public SensitiveSerializer(Sensitive.Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(mask(value, strategy));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
            throws JsonMappingException {
        if (property != null) {
            Sensitive annotation = property.getAnnotation(Sensitive.class);
            if (annotation == null) {
                annotation = property.getContextAnnotation(Sensitive.class);
            }
            if (annotation != null) {
                return new SensitiveSerializer(annotation.strategy());
            }
        }
        return this;
    }

    public static String mask(String value, Sensitive.Strategy strategy) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return switch (strategy) {
            case MOBILE -> value.length() < 7
                    ? "****"
                    : value.substring(0, 3) + "****" + value.substring(value.length() - 4);
            case EMAIL -> {
                int at = value.indexOf('@');
                if (at <= 1) {
                    yield "****";
                }
                yield value.charAt(0) + "***" + value.substring(at);
            }
            case PASSWORD -> "******";
            default -> value.length() <= 2
                    ? "**"
                    : value.charAt(0) + "***" + value.charAt(value.length() - 1);
        };
    }
}
