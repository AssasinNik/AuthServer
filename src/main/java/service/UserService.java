package service;


import user.ChangePasswordParams;
import user.CreateUserParams;
import user.LoginUserParams;
import com.example.user.UserDTO;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<UserDTO> registerUser(CreateUserParams params);

    CompletableFuture<Boolean> changeUsername(String email, String name, String newUsername);

    CompletableFuture<UserDTO> findUser(LoginUserParams params);

    CompletableFuture<Boolean> changePassword(ChangePasswordParams params);
}
