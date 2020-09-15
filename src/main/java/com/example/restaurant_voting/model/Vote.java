package com.example.restaurant_voting.model;

import com.example.restaurant_voting.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedEntityGraphs(value = {
        @NamedEntityGraph(
                name = "Vote.menu.restaurant",
                attributeNodes = @NamedAttributeNode(value = "menu", subgraph = "menu.restaurant"),
                subgraphs = @NamedSubgraph(name = "menu.restaurant", attributeNodes = @NamedAttributeNode("restaurant"))),
        @NamedEntityGraph(name = "Vote.menu", attributeNodes = @NamedAttributeNode("menu"))
})
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "user_id"}, name = "date_user_id_unique")})
public class Vote extends BaseEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_vote_user_id"), nullable = false, updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_vote_menu_id"), nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Menu menu;

    @Column(name = "date", updatable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    @NotNull
    private LocalDate date = DateUtil.getDate();

    public Vote(@NotNull Menu menu, @NotNull User user) {
        this.user = user;
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", user=" + user +
                ", menu=" + menu +
                ", date=" + date +
                '}';
    }
}
