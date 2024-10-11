package user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginUserParams {
    public String email;
    public String parol_user;

    @JsonCreator
    public LoginUserParams(@JsonProperty("email") String email,
                            @JsonProperty("parol_user") String parol_user) {
        this.email = email;
        this.parol_user = parol_user;
    }
    public String getEmail(){
        return email;
    };
    public String getParol_user(){
        return parol_user;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setParol_user(String parol_user){
        this.parol_user = parol_user;
    }
}
