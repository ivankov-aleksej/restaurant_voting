package com.example.restaurant_voting.config;

import com.example.restaurant_voting.to.VoteTo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

//https://stackoverflow.com/questions/51451891/how-to-hide-json-field-when-using-springdata-pageable

@JsonComponent
public class StatisticSerializer extends JsonSerializer<VoteTo> {
    @Override
    public void serialize(VoteTo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("date", value.getDate());
        gen.writeObjectField("restaurant", value.getMenu().getRestaurant());

        gen.writeObjectFieldStart("menu");

        gen.writeNumberField("id", value.getMenu().id());
        gen.writeObjectField("actionDate", value.getMenu().getActionDate());

        gen.writeEndObject();
        gen.writeNumberField("count", value.getCount());

        gen.writeEndObject();
    }
}
