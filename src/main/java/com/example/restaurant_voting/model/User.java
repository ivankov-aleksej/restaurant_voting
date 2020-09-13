package com.example.restaurant_voting.model;

import com.example.restaurant_voting.util.JsonDeserializers;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User extends BaseEntity implements Serializable {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotEmpty
    @Size(max = 128)
    private String email;


    @Column(name = "password", nullable = false)
    @Size(max = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonDeserialize(using = JsonDeserializers.PasswordDeserializer.class)
    private String password;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "registered", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @NotNull
    private LocalDateTime registered = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role"
            , joinColumns = {@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_role_user_id"
            , foreignKeyDefinition = "foreign key (user_id) references users on delete cascade"))}
            , uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_unique")})
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User(String email, String password, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(Integer id, String email, String password, Role role, Role... roles) {
        this(email, password, EnumSet.of(role, roles));
        setId(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "idl='" + this.getId() + '\'' +
                ", email='" + email + '\'' +
                ", registered=" + registered +
                ", roles=" + roles +
                '}';
    }
}
