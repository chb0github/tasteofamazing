/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bongiorno.sdrss.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.Identifiable;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "authorities")
public class Authority implements GrantedAuthority, Identifiable<Long> {

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @Id
    @GeneratedValue
    private Integer id;

    private String role;

    public Authority(User user, String role) {
        this.user = user;
        this.role = role;

    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return role;
    }

}
