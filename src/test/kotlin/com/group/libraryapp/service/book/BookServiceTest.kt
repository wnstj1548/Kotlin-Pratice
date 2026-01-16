package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.repository.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.repository.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.repository.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.user.response.BookStatResponse
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
) {

    @AfterEach
    fun afterEach() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("책 등록 테스트")
    fun saveBookTest() {
        //given
        val request = BookRequest(name = "이상한 나라의 엘리스", type= BookType.COMPUTER)

        //when
        bookService.saveBook(request)

        //then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("이상한 나라의 엘리스")
    }

    @Test
    @DisplayName("책 대출 테스트")
    fun loanBookTest() {
        //given
        bookRepository.save(Book.fixture(name = "이상한 나라의 엘리스"))
        val savedUser = userRepository.save(User(name = "김준서", age = null))
        val request = BookLoanRequest("김준서", "이상한 나라의 엘리스")

        //when
        bookService.loanBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("이상한 나라의 엘리스")
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.LOANED)
    }

    @Test
    @DisplayName("책이 대출이 되어있다면, 신규 대출이 실패한다")
    fun loanBookFailTest() {
        //given
        bookRepository.save(Book.fixture(name = "이상한 나라의 엘리스"))
        val savedUser = userRepository.save(User(name = "김준서", age = null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(user = savedUser, bookName = "이상한 나라의 엘리스"))
        val request = BookLoanRequest("김준서", "이상한 나라의 엘리스")

        //when & then
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }

    @Test
    @DisplayName("책 반납이 정상 동작한다")
    fun returnBookTest() {
        //given
        bookRepository.save(Book.fixture(name ="이상한 나라의 엘리스"))
        val savedUser = userRepository.save(User(name = "김준서", age = null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(user = savedUser, bookName = "이상한 나라의 엘리스"))
        val request = BookReturnRequest("김준서", "이상한 나라의 엘리스")

        //when
        bookService.returnBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @Test
    @DisplayName("책 대여 건수를 정상 확인한다")
    fun countLoanedBookTest() {
        //given
        val savedUser = userRepository.save(User(name = "김준서", age = null))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(savedUser, bookName = "A", status = UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUser, bookName = "B", status = UserLoanStatus.RETURNED),
            UserLoanHistory.fixture(savedUser, bookName = "C", status = UserLoanStatus.RETURNED)
            )
        )

        //when
        val result = bookService.countLoanedBook()

        //then
        assertThat(result).isEqualTo(1)
    }

    @Test
    @DisplayName("분야 별 책 권수를 정상 확인한다")
    fun getBookStaticsTest() {
        //given
        bookRepository.saveAll(listOf(
            Book.fixture("A", BookType.COMPUTER),
            Book.fixture("B", BookType.COMPUTER),
            Book.fixture("C", BookType.SCIENCE),
        ))

        //when
        val results = bookService.getBookStatics()

        //then
        assertThat(results).hasSize(2)
        assertCount(results, BookType.COMPUTER, 2L)
        assertCount(results, BookType.SCIENCE, 1L)
    }

    private fun assertCount(results: List<BookStatResponse>, type: BookType, count: Long) {
        val sciences = results.first { book -> book.type == type }
        assertThat(sciences.count).isEqualTo(count)
    }
}