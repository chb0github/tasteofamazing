package org.bongiorno.sdrss.domain.resources;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@Entity
@ToString
public class Report implements Post {


  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  private Instant date;

  @NotNull
  private String name;
}
