package org.bongiorno.sdrss.repositories

import org.bongiorno.sdrss.domain.security.Authority
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.security.access.prepost.PreAuthorize


@PreAuthorize("hasRole('ROLE_USER')")
interface AuthorityRepository : PagingAndSortingRepository<Authority, Long> {

    /*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#save(S)
	 */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun <S : Authority> save(s: S): S

    /*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#delete(java.io.Serializable)
	 */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun delete(aLong: Long?)
}
