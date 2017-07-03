package org.bongiorno.sdrss.domain.resources;


import java.time.Instant;
import org.springframework.hateoas.Identifiable;

public interface Post extends Identifiable<Long> {

  Long getId();

  Instant getDate();

  String getName();
}
