package com.example.restaurant_voting.config;

import com.example.restaurant_voting.model.Dish;
import com.example.restaurant_voting.model.Menu;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

//https://stackoverflow.com/questions/51451891/how-to-hide-json-field-when-using-springdata-pageable

@JsonComponent
public class MenuSerializer extends JsonSerializer<Menu> {
    @Override
    public void serialize(Menu value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("id", value.id());
        gen.writeObjectField("actionDate", value.getActionDate());
        gen.writeObjectField("restaurant", value.getRestaurant());
        gen.writeArrayFieldStart("dishes");

        for (Dish dish : value.getDishes()) {
//            https://www.baeldung.com/jackson-call-default-serializer-from-custom-serializer
            gen.writeStartObject();

            gen.writeNumberField("id", dish.id());
            gen.writeStringField("name", dish.getName());
            gen.writeObjectField("price", dish.getPrice());

            gen.writeEndObject();
        }

        gen.writeEndArray();

        gen.writeEndObject();
    }
}
