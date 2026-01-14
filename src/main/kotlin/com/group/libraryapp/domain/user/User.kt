package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    var age: Int?,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf()
) {
    fun loanBook(book: Book) {
        this.userLoanHistories.add(UserLoanHistory(user = this, bookName = book.name, isReturn = false))
    }

    fun returnBook(bookName: String) {
        val targetHistory = userLoanHistories.first {history -> history.bookName == bookName}
        targetHistory.doReturn()
    }
}