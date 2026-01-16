package com.group.libraryapp.controller.user

import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.service.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun saveUser(@RequestBody request: UserCreateRequest): ResponseEntity<Void> {
        userService.saveUser(request)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserResponse>> {
        return ResponseEntity.ok(userService.getUsers())
    }

    @PutMapping
    fun updateUserName(@RequestBody request: UserUpdateRequest): ResponseEntity<Void> {
        userService.updateUserName(request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping
    fun deleteUser(@RequestParam name: String): ResponseEntity<Void> {
        userService.deleteUser(name)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/loan")
    fun getUserHistories(): ResponseEntity<List<UserLoanHistoryResponse>> {
        return ResponseEntity.ok(userService.getUserLoanHistories())
    }
}