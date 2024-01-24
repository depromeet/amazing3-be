package io.raemian.storage.db.core.profile

import org.springframework.data.jpa.repository.JpaRepository

interface DefaultProfileRepository : JpaRepository<DefaultProfile, Long> {
    override fun getById(id: Long): DefaultProfile =
        findById(id).orElseThrow { NoSuchElementException("존재하지 않는 기본 프로필 이미지입니다. $id") }
}
