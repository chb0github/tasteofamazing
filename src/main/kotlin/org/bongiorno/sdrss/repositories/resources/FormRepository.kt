package org.bongiorno.sdrss.repositories.resources

import org.bongiorno.sdrss.domain.resources.Form
import org.springframework.data.repository.CrudRepository
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PostFilter


interface FormRepository : org.springframework.data.repository.CrudRepository<Form, Long> {


    @org.springframework.security.access.prepost.PostFilter("hasPermission(filterObject, 'READ')")
    override fun findAll(): Iterable<org.bongiorno.sdrss.domain.resources.Form>

    @org.springframework.security.access.prepost.PostAuthorize("hasPermission(returnObject, 'WRITE')")
    override fun findOne(aLong: Long?): org.bongiorno.sdrss.domain.resources.Form
}
