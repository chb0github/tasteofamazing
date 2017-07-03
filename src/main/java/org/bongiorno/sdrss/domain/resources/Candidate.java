package org.bongiorno.sdrss.domain.resources;

import java.time.Instant;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode
@Entity
@ToString
public class Candidate implements Post {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Instant date;

    @NotNull
    private String name;

    public Candidate() {
        System.out.println();
    }
}
