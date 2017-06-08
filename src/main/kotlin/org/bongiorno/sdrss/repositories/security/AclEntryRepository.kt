package org.bongiorno.sdrss.repositories.security

import org.bongiorno.sdrss.domain.security.AclClass
import org.bongiorno.sdrss.domain.security.AclEntry
import org.bongiorno.sdrss.domain.security.Authority
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.security.access.prepost.PreAuthorize


@PreAuthorize("hasRole('ADMIN')")
interface AclEntryRepository : PagingAndSortingRepository<AclEntry, Long>
