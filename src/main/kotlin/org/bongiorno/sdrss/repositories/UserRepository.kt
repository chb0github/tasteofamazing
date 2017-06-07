package org.bongiorno.sdrss.repositories

import org.bongiorno.sdrss.domain.security.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.security.access.prepost.PostAuthorize


interface UserRepository : CrudRepository<User, String> {
    @Query("select o from User o where o.username = ?#{principal.username} or 1=?#{hasRole('ROLE_ADMIN') ? 1 : 0}")
    override fun findAll(): Iterable<User>

//    @PostAuthorize("hasPermission(returnObject, 'READ')")
    override fun findOne(id: String?): User
    // to use this the user's id must be a long. But if it's a long then this won't work for userdetails
//    @PostAuthorize("hasPermission(returnObject, 'READ')")
//    override fun findOne(id: Long?): User
//
//    @PostAuthorize("hasPermission(returnObject, 'READ')")
//    fun findByUsername(username: String): User
//
//    @PreAuthorize("hasPermission(entity, 'WRITE')")
//    override fun delete(entity: User?)
//
//    @PreAuthorize("id == principal.id")
//    override fun delete(id: Long?)
}
