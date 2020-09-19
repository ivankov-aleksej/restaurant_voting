package com.example.restaurant_voting.model;

import com.example.restaurant_voting.View;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = "Menu"),
        @NamedEntityGraph(name = "Menu.restaurant.dishes",
                attributeNodes = {@NamedAttributeNode("restaurant"), @NamedAttributeNode("dishes")}),
        @NamedEntityGraph(name = "Menu.restaurant", attributeNodes = {@NamedAttributeNode("restaurant")})
})
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "action_date"},
        name = "restaurant_id_action_date_unique")})
public class Menu extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey(name = "fk_menu_restaurant_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    @Column(name = "action_date", updatable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    @NotNull
    @Future(groups = View.Persist.class)
    private LocalDate actionDate;

    //    https://discourse.hibernate.org/t/hibernate-lazy-mode-doesnt-work-with-spring-boot/1535/6
    //    spring.jpa.open-in-view=false
    //    You need to use a JPQL query with JOIN FETCH the lazy associations.
    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Dish> dishes = new ArrayList<>();

    public Menu(Integer id, @NotNull(groups = View.Persist.class) Restaurant restaurant,
                @NotNull @Future(groups = View.Persist.class) LocalDate actionDate) {
        super(id);
        this.restaurant = restaurant;
        this.actionDate = actionDate;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", restaurant=" + restaurant +
                ", actionDate=" + actionDate +
                '}';
    }
}
