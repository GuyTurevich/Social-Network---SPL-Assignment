package bgu.spl.net.srv;

import java.util.Objects;

public class User {

    private String username;
    private String password;
    private String birthday;

    public User(String _username, String _password, String _birthday){
        username = _username;
        password = _password;
        birthday = _birthday;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) { // Username is unique
        User user = (User) o;
        return this.username.equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, birthday);
    }
}
