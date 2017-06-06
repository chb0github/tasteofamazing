package org.bongiorno.sdrss.domain.security;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AclEntry {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Long aclObjectIdentity;

    @NotNull
    private Integer aceOrder;

    @NotNull
    private Long sid;


    @NotNull
    private Integer mask;


    @NotNull
    private Boolean granting;

    @NotNull

    private Boolean auditSuccess;

    @NotNull
    private Boolean auditFailure;


}
