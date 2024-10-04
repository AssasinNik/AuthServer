package com.example.service.user;

public class LoginUserParams {
    public String email;
    public String parol_user;
    public String new_parol;

    public LoginUserParams(String email, String parol_user, String new_parol) {
        this.email = email;
        this.parol_user = parol_user;
        this.new_parol = new_parol;
    }
    public String getEmail(){
        return email;
    };
    public String getParol_user(){
        return parol_user;
    }
    public String getNew_parol(){
        return new_parol;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setParol_user(String parol_user){
        this.parol_user = parol_user;
    }
    public void setNew_parol(String new_parol){
        this.new_parol = new_parol;
    }
}
