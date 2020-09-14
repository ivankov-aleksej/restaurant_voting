package com.example.restaurant_voting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Restaurant extends BaseEntity implements Serializable {

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Size(max = 128)
    private String name;

    public Restaurant(Integer id, @NotEmpty @Size(max = 128) String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ",name='" + name + '\'' +
                '}';
    }
}
