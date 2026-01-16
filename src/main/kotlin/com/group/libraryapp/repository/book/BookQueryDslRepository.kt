package com.group.libraryapp.repository.book

import com.group.libraryapp.dto.user.response.BookStatResponse

interface BookQueryDslRepository {
    fun getStats(): List<BookStatResponse>
}