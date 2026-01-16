package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.repository.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.BookHistoryResponse
import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.util.findByIdOrThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
) {

    fun saveUser(request: UserCreateRequest) {
        val user = User(name = request.name, age = request.age)
        userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun getUsers() : List<UserResponse> {
//        return userRepository.findAll().map(UserResponse::fromEntity)
        return userRepository.findAll().map { UserResponse.fromEntity(it) }
    }

    fun updateUserName(request: UserUpdateRequest) {
        val user: User = userRepository.findByIdOrThrow(request.id)
        user.updateName(request.name)
    }

    fun deleteUser(name: String) {
        val user: User = userRepository.findByName(name) ?: throw IllegalArgumentException("해당하는 유저가 없습니다")
        userRepository.delete(user)
    }

    fun getUserLoanHistories(): List<UserLoanHistoryResponse> {
        return userRepository.findAllWithHistories().map(UserLoanHistoryResponse::of)
    }
}