package com.example.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Dish extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_dish_menu_id"))
    @JsonIgnoreProperties({"dishes", "restaurant"})
    @NotNull
    private Menu menu;

    @Column(name = "name", nullable = false)
    @NotEmpty
    @Size(max = 128)
    private String name;

    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0.00", inclusive = false)
    @Digits(integer = 3, fraction = 2)
    @NotNull
    private BigDecimal price;

    @Override
    public String toString() {
        return "Dish{" +
                "id='" + this.getId() + '\'' +
                ", menuId='" + menu.getId() + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
