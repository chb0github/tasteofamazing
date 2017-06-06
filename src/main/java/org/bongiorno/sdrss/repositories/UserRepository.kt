package org.bongiorno.sdrss.repositories

import org.bongiorno.sdrss.domain.security.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository


interface UserRepository : CrudRepository<User, String> {
    @Query("select o from User o where o.username = ?#{principal.username} or 1=?#{hasRole('ROLE_ADMIN') ? 1 : 0}")
    override fun findAll(): Iterable<User>
}
