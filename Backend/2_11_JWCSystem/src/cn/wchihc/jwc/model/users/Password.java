package cn.wchihc.jwc.model.users;

public class Password {
    /**
     * id : 1
     * oldPassword : 1111
     * newPassword : 111
     */

    private int id;
    private String oldPassword;
    private String newPassword;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
