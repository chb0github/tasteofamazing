package org.bongiorno.sdrss.domain.security;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

@Getter
@Entity
@Table(name = "acl_entry")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AclEntry {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @OneToOne
    private AclObjectIdentity aclObjectIdentity;

    @NotNull
    private Integer aceOrder;

    @NotNull
    @OneToOne
    private AclSid sid;

    @NotNull
    private Integer mask;


    @NotNull
    private Boolean granting;

    @NotNull
    private Boolean auditSuccess;

    @NotNull
    private Boolean auditFailure;

    public AclEntry(AclObjectIdentity aclObjectIdentity, AclSid sid, Permission... permissions) {
        this.aclObjectIdentity = aclObjectIdentity;
        this.sid = sid;
        this.mask = Stream.of(permissions).map(Enum::ordinal).reduce((a,b) -> a | b).orElse(0);
    }

    public enum Permission {
        NONE,
        READ,
        WRITE
    }
}
