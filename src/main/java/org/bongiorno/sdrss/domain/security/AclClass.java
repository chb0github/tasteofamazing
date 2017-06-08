
package org.bongiorno.sdrss.domain.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "acl_class")
public class AclClass {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "class")
    private Class<?> clazz;


    public AclClass(Class<?> clazz) {
        this.clazz = clazz;
    }
}
