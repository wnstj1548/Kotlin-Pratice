package com.group.libraryapp.repository.user

import com.group.libraryapp.domain.user.User

interface UserQueryDslRepository {
    fun findAllWithHistories(): List<User>
}