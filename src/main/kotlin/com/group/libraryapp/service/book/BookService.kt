package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.repository.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.book.QBook.book
import com.group.libraryapp.repository.user.UserRepository
import com.group.libraryapp.repository.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.user.response.BookStatResponse
import com.querydsl.core.util.MathUtils.result
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    fun saveBook(request: BookRequest) {
        bookRepository.save(Book(name = request.name, type = request.type))
    }

    fun loanBook(request: BookLoanRequest) {
        val book = bookRepository.findByName(request.bookName) ?: throw IllegalArgumentException("잘못된 책 이름입니다.")
        if(userLoanHistoryRepository.findByBookNameAndStatus(bookName = request.bookName, status = UserLoanStatus.LOANED) != null) {
            throw IllegalArgumentException("진작 대출되어 있는 책입니다")
        }

        val user = userRepository.findByName(request.userName) ?: throw IllegalArgumentException("유효하지 않은 유저 이름입니다")
        user.loanBook(book)
    }

    fun returnBook(request: BookReturnRequest) {
        val user = userRepository.findByName(request.userName) ?: throw IllegalArgumentException("유효하지 않은 유저 이름입니다")
        user.returnBook(request.bookName)
    }

    @Transactional(readOnly = true)
    fun countLoanedBook(): Int {
        return userLoanHistoryRepository.countByStatus(UserLoanStatus.LOANED).toInt()
    }

    @Transactional(readOnly = true)
    fun getBookStatics(): List<BookStatResponse> {
        return bookRepository.getStats()
//        return bookRepository.findAll()
//            .groupBy { book -> book.type }
//            .map { (type, books) -> BookStatResponse(type = type, count = books.size.toLong() ) }

//        val results = mutableListOf<BookStatResponse>()
//        val books = bookRepository.findAll()
//
//        for(book in books) {
//            results.firstOrNull {dto -> dto.type == book.type}?.plusOne()
//                ?: results.add(BookStatResponse(type = book.type, 1))
//        }
//
//        return results
    }
}