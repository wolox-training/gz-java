package wolox.training.models;

public class UserDTO {
  private String currentPassword;
  private String newPassword;

  public String getCurrentPassword() {
    return this.currentPassword;
  }

  public void setCurrentPassword(String password) {
    this.currentPassword = password;
  }

  public void setNewPassword(String password) {
    this.newPassword = password;
  }

  public String getNewPassword() {
    return this.newPassword;
  }
}
