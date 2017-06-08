package org.bongiorno.sdrss.domain.security;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.springframework.hateoas.Identifiable;

@Getter
@Entity
@Table(name = "acl_object_identity")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AclObjectIdentity implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Basic
    @JoinColumn(name = "object_id_class", referencedColumnName = "id")
    private String objectIdClass;

    @NotNull
    private Long objectIdIdentity;

    @OneToOne
    private AclObjectIdentity parentObject;

    @NotNull
    @OneToOne
    private AclSid ownerSid;

    @NotNull
    private Boolean entriesInheriting;

    /**
     * Basically says
     * @param aclSid this guys has access to
     * @param classId this instance of
     * @param aclClass this class
     */
    public AclObjectIdentity(Class<?> aclClass, Long classId, AclSid aclSid) {
        this.objectIdClass = aclClass.getName();
        this.objectIdIdentity = classId;
        this.ownerSid = aclSid;
        this.entriesInheriting = false;
    }
}
