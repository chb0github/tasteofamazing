
package org.bongiorno.sdrss.domain.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.hateoas.Identifiable;

@Getter
@Entity
@Table(name = "acl_sid")
@NoArgsConstructor
@EqualsAndHashCode
public class AclSid implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    private Boolean principal;

    private String sid;


    public AclSid(User u) {
        this(true,u.getUsername());
    }

    public AclSid(Group g) {
        this(false,g.getName());
    }


    public AclSid(Boolean isPrincipal, String sid) {
        this.sid = sid;
        this.principal = isPrincipal;
    }
}
