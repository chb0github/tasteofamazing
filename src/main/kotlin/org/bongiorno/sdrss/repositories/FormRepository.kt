package org.bongiorno.sdrss.repositories

import org.bongiorno.sdrss.domain.resources.Form
import org.springframework.data.repository.CrudRepository
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PostFilter


interface FormRepository : CrudRepository<Form, Long> {


    @PostFilter("hasPermission(filterObject, 'READ')")
    override fun findAll(): Iterable<Form>

    @PostAuthorize("hasPermission(returnObject, 'WRITE')")
    override fun findOne(aLong: Long?): Form
}
