package com.example.restaurant_voting.to;

import com.example.restaurant_voting.model.Menu;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoteTo {
    private LocalDate date;
    @JsonIgnoreProperties({"dishes", "createdOn"})
    private Menu menu;
    private Long count;

    @Override
    public String toString() {
        return "VoteTo{" +
                "date=" + date +
                ", menuId=" + menu.getId() +
                ", count=" + count +
                '}';
    }
}
