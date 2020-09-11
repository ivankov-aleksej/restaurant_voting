package com.example.restaurant_voting.config;

import com.example.restaurant_voting.model.Dish;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

//https://stackoverflow.com/questions/51451891/how-to-hide-json-field-when-using-springdata-pageable

@JsonComponent
public class DishSerializer extends JsonSerializer<Dish> {
    @Override
    public void serialize(Dish value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("id", value.id());
        gen.writeObjectField("name", value.getName());
        gen.writeObjectField("price", value.getPrice());

        gen.writeObjectFieldStart("menu");

        gen.writeNumberField("id", value.getMenu().id());
        gen.writeObjectField("actionDate", value.getMenu().getActionDate());

        gen.writeEndObject();

        gen.writeEndObject();
    }
}
