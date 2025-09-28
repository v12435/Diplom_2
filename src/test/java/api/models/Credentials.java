package api.models;

public class Credentials {
    public String email;
    public String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "{ \"email\":\"" + email + "\", \"password\":\"" + password + "\" }";
    }
}
