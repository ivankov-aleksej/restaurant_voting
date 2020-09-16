package com.example.restaurant_voting.config.serializer;

import com.example.restaurant_voting.model.Vote;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

//https://stackoverflow.com/questions/51451891/how-to-hide-json-field-when-using-springdata-pageable

@JsonComponent
public class VoteSerializer extends JsonSerializer<Vote> {
    @Override
    public void serialize(Vote value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("id", value.id());
        gen.writeObjectField("date", value.getDate());
        gen.writeObjectField("restaurant", value.getMenu().getRestaurant());

        gen.writeObjectFieldStart("menu");

        gen.writeNumberField("id", value.getMenu().id());
        gen.writeObjectField("actionDate", value.getMenu().getActionDate());

        gen.writeEndObject();

        gen.writeEndObject();
    }
}
