package org.bongiorno.sdrss.domain.resources;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@EqualsAndHashCode
@Entity
@ToString
public class Report  implements  Post {


    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Date date;

    @NotNull
    private String name;
}
