package app.persistence;

import app.entities.User;
import app.entities.Role;
import app.exceptions.DatabaseException;
import app.config.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User getUserByEmail(String email, ConnectionPool cp)
            throws DatabaseException {

        String sql = """
            SELECT user_id, email, password_hash, role
            FROM public.users
            WHERE email = ?
        """;

        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        Role.valueOf(rs.getString("role"))
                );
            }

            return null;

        } catch (SQLException e) {
            throw new DatabaseException("Error finding user", e.getMessage());
        }
    }

    public static void createUser(User user, ConnectionPool cp)
            throws DatabaseException {

        String sql = """
            INSERT INTO public.users (email, password_hash, role)
            VALUES (?, ?, ?)
        """;

        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error creating user", e.getMessage());
        }
    }

    public static List<User> getAllUsers(ConnectionPool cp)
            throws DatabaseException {

        List<User> users = new ArrayList<>();

        String sql = """
            SELECT user_id, email, password_hash, role
            FROM public.users
        """;

        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        Role.valueOf(rs.getString("role"))
                ));
            }

            return users;

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching users", e.getMessage());
        }
    }
}