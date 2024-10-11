package com.example.service.user;

import com.example.user.UserDTO;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<UserDTO> registerUser(CreateUserParams params);

    boolean changeUsername(String token, String newUsername);

    @Nullable UserDTO findUser(String email, String password);

    boolean changePassword(String email, String password, String newPassword);
}