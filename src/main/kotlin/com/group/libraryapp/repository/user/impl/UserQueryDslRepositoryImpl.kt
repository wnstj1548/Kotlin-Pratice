package com.group.libraryapp.repository.user.impl

import com.group.libraryapp.domain.user.QUser
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.repository.user.UserQueryDslRepository
import com.group.libraryapp.domain.user.loanhistory.QUserLoanHistory
import com.querydsl.jpa.impl.JPAQueryFactory

class UserQueryDslRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
): UserQueryDslRepository {

    override fun findAllWithHistories(): List<User> {
        return queryFactory.select(QUser.user).distinct()
            .from(QUser.user)
            .leftJoin(QUserLoanHistory.userLoanHistory).on(QUser.user.id.eq(QUserLoanHistory.userLoanHistory.user.id)).fetchJoin()
            .fetch()
    }
}