package com.example.restaurant_voting.model;

import com.example.restaurant_voting.View;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = "Dish.menu", attributeNodes = @NamedAttributeNode("menu"))
})
//@JsonSerialize(using = DishSerializer.class)
public class Dish extends BaseEntity implements Serializable {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_dish_menu_id"))
    @NotNull(groups = View.Persist.class)
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
                "id='" + id + '\'' +
                ", menu='" + menu + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
