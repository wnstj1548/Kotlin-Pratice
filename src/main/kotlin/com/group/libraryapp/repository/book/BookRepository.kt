package com.group.libraryapp.repository.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookType
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository: JpaRepository<Book, Long>, BookQueryDslRepository {
    fun findByName(bookName: String): Book?
}