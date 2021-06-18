public class UserBuilder {

    private final User user;

    public UserBuilder(User user) {
        this.user = user;
    }

    public UserBuilder id(Long id) {
        user.setId(id);
        return this;
    }

    public UserBuilder fullName(String fullname) {
        user.setFullName(fullname);
        return this;
    }

    public UserBuilder email(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder age(short age) {
        user.setAge(age);
        return this;
    }

    public UserBuilder phone(int phoneNumber) {
        user.setPhoneNumber(phoneNumber);
        return this;
    }

    public User build() {
        return user;
    }
}
