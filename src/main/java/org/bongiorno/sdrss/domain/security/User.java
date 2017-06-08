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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Identifiable;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;


@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "users")
public class User implements UserDetails, Identifiable<String>{

    @Id
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private Boolean enabled;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Collection<Authority> authorities;

    public User(String username, String password, Boolean enabled, String ... authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = stream(authorities).map(a -> new Authority(this,a)).collect(toSet());

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getId() {
        return username;
    }

    @Override
    public String toString() {
        return username;
    }
}
