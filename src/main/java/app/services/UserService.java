package app.services;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.UserMapper;
import app.persistence.ConnectionPool;

public class UserService {

    private final ConnectionPool connectionPool;

    public UserService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    // REGISTER
    public void register(User user) throws DatabaseException {

        ValidationUtil.validateEmail(user.getEmail());
        ValidationUtil.validatePassword(user.getPassword());

        String hashedPassword = PasswordHasher.hash(user.getPassword());
        user.setPassword(hashedPassword);

        UserMapper.createUser(user, connectionPool);
    }

    // LOGIN
    public User login(String email, String rawPassword) throws DatabaseException {

        User user = UserMapper.getUserByEmail(email, connectionPool);

        if (user == null) {
            throw new DatabaseException("User not found");
        }

        boolean ok = PasswordHasher.verify(rawPassword, user.getPassword());

        if (!ok) {
            throw new DatabaseException("Invalid password");
        }

        return user;
    }
}