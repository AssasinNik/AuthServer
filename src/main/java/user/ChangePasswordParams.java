package user;

public class ChangePasswordParams {
    private String old_parol_user;
    private String new_parol_user;
    private String email;

    public ChangePasswordParams(String old_parol_user, String new_parol_user, String email) {
        this.old_parol_user = old_parol_user;
        this.new_parol_user = new_parol_user;
        this.email = email;
    }
    public String getOld_parol_user() {
        return old_parol_user;
    }
    public String getNew_parol_user() {
        return new_parol_user;
    }
    public String getEmail() {
        return email;
    }
    public void setOld_parol_user(String old_parol_user) {
        this.old_parol_user = old_parol_user;
    }
    public void setNew_parol_user(String new_parol_user) {
        this.new_parol_user = new_parol_user;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
