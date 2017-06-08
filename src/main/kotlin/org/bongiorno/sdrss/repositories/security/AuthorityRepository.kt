package org.bongiorno.sdrss.repositories.security

import org.bongiorno.sdrss.domain.security.Authority
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.security.access.prepost.PreAuthorize


@org.springframework.security.access.prepost.PreAuthorize("hasRole('ROLE_USER')")
interface AuthorityRepository : org.springframework.data.repository.PagingAndSortingRepository<Authority, Long> {

    /*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#save(S)
	 */
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun <S : org.bongiorno.sdrss.domain.security.Authority> save(s: S): S

    /*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#delete(java.io.Serializable)
	 */
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun delete(aLong: Long?)
}
