
package org.bongiorno.sdrss.domain.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import org.springframework.hateoas.Identifiable;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "acl_class")
public class AclClass implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "class")
    private Class<?> clazz;


    public AclClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return clazz.getName();
    }
}
