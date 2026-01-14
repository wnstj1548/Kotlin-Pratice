package com.group.libraryapp.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JavaBookRepository extends JpaRepository<JavaBook, Long> {

  Optional<JavaBook> findByName(String bookName);

}
