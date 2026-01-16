package com.group.libraryapp.repository.book.impl

import com.group.libraryapp.domain.book.QBook
import com.group.libraryapp.domain.book.QBook.book
import com.group.libraryapp.dto.user.response.BookStatResponse
import com.group.libraryapp.repository.book.BookQueryDslRepository
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory

class BookQueryDslRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
): BookQueryDslRepository {

    override fun getStats(): List<BookStatResponse> {
        return queryFactory.select(
            Projections.constructor(
                BookStatResponse::class.java,
                book.type,
                book.id.count()
            )
        )
            .from(QBook.book)
            .groupBy(QBook.book.type)
            .fetch()
    }
}