package org.bongiorno.sdrss.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.Identifiable;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "authorities")
public class Authority implements GrantedAuthority, Identifiable<Long> {

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @Id
    @GeneratedValue
    private Long id;

    private String role;

    public Authority(User user, String role) {
        this.user = user;
        this.role = role;

    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return role;
    }

}
