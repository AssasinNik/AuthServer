package com.example.service.user;

public class ChangeUsernameParams {
    private String old_parol_user;
    private String new_parol_user;
    private String username;

    public ChangeUsernameParams(String old_parol_user, String new_parol_user, String username) {
        this.old_parol_user = old_parol_user;
        this.new_parol_user = new_parol_user;
        this.username = username;
    }
    public String getOld_parol_user() {
        return old_parol_user;
    }
    public String getNew_parol_user() {
        return new_parol_user;
    }
    public String getUsername() {
        return username;
    }
    public void setOld_parol_user(String old_parol_user) {
        this.old_parol_user = old_parol_user;
    }
    public void setNew_parol_user(String new_parol_user) {
        this.new_parol_user = new_parol_user;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
