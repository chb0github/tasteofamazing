package org.bongiorno.sdrss.domain.resources;


import java.util.Date;
import org.springframework.hateoas.Identifiable;

public interface Post extends Identifiable<Long> {
    Long getId();

    Date getDate();

    String getName();
}
