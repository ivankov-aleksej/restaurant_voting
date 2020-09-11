package com.example.restaurant_voting.util;

import com.example.restaurant_voting.model.Dish;

public class EntityUtil {

    private EntityUtil() {
    }

    public static Dish updateDish(Dish origDish, Dish newDish) {
        origDish.setName(newDish.getName());
        origDish.setPrice(newDish.getPrice());
        return origDish;
    }
}
