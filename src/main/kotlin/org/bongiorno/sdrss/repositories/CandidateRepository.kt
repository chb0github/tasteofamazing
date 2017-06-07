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
package org.bongiorno.sdrss.repositories

import org.bongiorno.sdrss.domain.resources.Candidate
import org.springframework.data.repository.CrudRepository
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PostFilter


interface CandidateRepository : CrudRepository<Candidate, Long> {
    @PostFilter("hasPermission(filterObject, 'READ')")
    override fun findAll(): Iterable<Candidate>

    @PostAuthorize("hasPermission(returnObject, 'WRITE')")
    override fun findOne(aLong: Long?): Candidate
}
