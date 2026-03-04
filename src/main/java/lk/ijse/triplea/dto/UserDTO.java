package lk.ijse.triplea.dto;

public class UserDTO {

    private int id;
    private String username;
    private String password;
    private String phone;
    private String role;
    private String status;
    private String securityQuestion;
    private String securityAnswer;

    public UserDTO() {}

    public UserDTO(String username, String password, String phone, String role, String status) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }

    public UserDTO(int id, String username, String password, String phone, String role, String status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
}