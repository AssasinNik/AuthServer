package com.example.service.user;

public interface UserService {

    UserDTO registerUser(CreateUserParams params);

    boolean changeUsername(String token, String newUsername);

    UserDTO findUser(String email, String password);

    boolean changePassword(String email, String password, String newPassword, String token);

}
