package com.example.restaurant_voting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Restaurant extends BaseEntity {

    @Column(nullable = false)
    @NotEmpty
    @Size(max = 128)
    private String name;

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + this.getId() + '\'' +
                ",name='" + name + '\'' +
                '}';
    }
}
