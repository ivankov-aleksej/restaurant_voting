package com.example.restaurant_voting.to;

import com.example.restaurant_voting.model.Menu;
import com.example.restaurant_voting.model.Restaurant;
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
    private Menu menu;
    private Long count;

    public VoteTo(LocalDate date, Integer menuId, Integer restaurantId, String restaurantName,
                  LocalDate actionDate, Long count) {
        this.date = date;
        this.menu = new Menu(menuId, new Restaurant(restaurantId, restaurantName), actionDate);
        this.count = count;
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "date=" + date +
                ", menu" + menu +
                ", count=" + count +
                '}';
    }
}
