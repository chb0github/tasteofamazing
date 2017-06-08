package org.bongiorno.sdrss.repositories.security

import org.bongiorno.sdrss.domain.security.AclObjectIdentity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.security.access.prepost.PreAuthorize


@PreAuthorize("hasRole('ROOT')")
interface AclObjectIdentityRepository : PagingAndSortingRepository<AclObjectIdentity, Long>
