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
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "action_date"}, name = "restaurant_id_action_date_unique")})
public class Menu extends BaseEntity implements Serializable {

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey(name = "fk_menu_restaurant_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "action_date", updatable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    @NotNull
    @Future(groups = View.Persist.class)
    private LocalDate actionDate;

    //    https://discourse.hibernate.org/t/hibernate-lazy-mode-doesnt-work-with-spring-boot/1535/6
    //    spring.jpa.open-in-view=false
    //    You need to use a JPQL query with JOIN FETCH the lazy associations.
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Dish> dishes = new ArrayList<>();

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + this.getId() +
                ", restaurantId=" + restaurant.getId() +
                ", actionDate=" + actionDate +
                '}';
    }
}
