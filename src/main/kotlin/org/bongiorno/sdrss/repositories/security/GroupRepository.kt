package org.bongiorno.sdrss.repositories.security

import org.bongiorno.sdrss.domain.security.Group
import org.springframework.data.repository.CrudRepository


interface GroupRepository : CrudRepository<Group, Long>
