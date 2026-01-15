package com.group.libraryapp.controller.book

import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.service.book.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book")
class BookController(
    private val bookService: BookService
) {
    @PostMapping
    fun saveBook(@RequestBody request: BookRequest): ResponseEntity<Void> {
        bookService.saveBook(request)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/loan")
    fun loanBook(@RequestBody request: BookLoanRequest): ResponseEntity<Void> {
        bookService.loanBook(request)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/return")
    fun returnBook(@RequestBody request: BookReturnRequest): ResponseEntity<Void> {
        bookService.returnBook(request)
        return ResponseEntity.noContent().build()
    }
}