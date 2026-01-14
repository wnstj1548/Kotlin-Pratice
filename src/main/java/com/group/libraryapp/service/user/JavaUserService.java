package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.JavaUser;
import com.group.libraryapp.domain.user.JavaUserRepository;
import com.group.libraryapp.dto.user.request.JavaUserCreateRequest;
import com.group.libraryapp.dto.user.request.JavaUserUpdateRequest;
import com.group.libraryapp.dto.user.response.JavaUserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JavaUserService {

  private final JavaUserRepository userRepository;

  public JavaUserService(JavaUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public void saveUser(JavaUserCreateRequest request) {
    JavaUser newUser = new JavaUser(request.getName(), request.getAge());
    userRepository.save(newUser);
  }

  @Transactional(readOnly = true)
  public List<JavaUserResponse> getUsers() {
    return userRepository.findAll().stream()
        .map(JavaUserResponse::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public void updateUserName(JavaUserUpdateRequest request) {
    JavaUser user = userRepository.findById(request.getId()).orElseThrow(IllegalArgumentException::new);
    user.updateName(request.getName());
  }

  @Transactional
  public void deleteUser(String name) {
    JavaUser user = userRepository.findByName(name).orElseThrow(IllegalArgumentException::new);
    userRepository.delete(user);
  }

}
