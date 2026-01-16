package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.book.BookType

data class BookStatResponse(
    val type: BookType,
    var count: Long,
) {
    fun plusOne() {
        count = count + 1
    }
}