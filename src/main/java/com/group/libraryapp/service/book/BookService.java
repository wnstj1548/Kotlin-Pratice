package com.group.libraryapp.service.book;

import com.group.libraryapp.domain.book.JavaBook;
import com.group.libraryapp.domain.book.JavaBookRepository;
import com.group.libraryapp.domain.user.JavaUser;
import com.group.libraryapp.domain.user.JavaUserRepository;
import com.group.libraryapp.domain.user.loanhistory.JavaUserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

  private final JavaBookRepository bookRepository;
  private final JavaUserRepository userRepository;
  private final JavaUserLoanHistoryRepository userLoanHistoryRepository;

  public BookService(
      JavaBookRepository bookRepository,
      JavaUserRepository userRepository,
      JavaUserLoanHistoryRepository userLoanHistoryRepository
  ) {
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
    this.userLoanHistoryRepository = userLoanHistoryRepository;
  }

  @Transactional
  public void saveBook(BookRequest request) {
    JavaBook newBook = new JavaBook(request.getName());
    bookRepository.save(newBook);
  }

  @Transactional
  public void loanBook(BookLoanRequest request) {
    JavaBook book = bookRepository.findByName(request.getBookName()).orElseThrow(IllegalArgumentException::new);
    if (userLoanHistoryRepository.findByBookNameAndIsReturn(request.getBookName(), false) != null) {
      throw new IllegalArgumentException("진작 대출되어 있는 책입니다");
    }

    JavaUser user = userRepository.findByName(request.getUserName()).orElseThrow(IllegalArgumentException::new);
    user.loanBook(book);
  }

  @Transactional
  public void returnBook(BookReturnRequest request) {
    JavaUser user = userRepository.findByName(request.getUserName()).orElseThrow(IllegalArgumentException::new);
    user.returnBook(request.getBookName());
  }
}
