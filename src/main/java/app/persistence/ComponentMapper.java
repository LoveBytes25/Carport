package app.persistence;

import app.entities.Component;
import app.exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class ComponentMapper {

    public static Component findComponent(String name, double width, double height, double length, ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
                SELECT cp_id, name, width, height, length, unit, description
                FROM component
                WHERE name = ?
                AND width = ?
                AND height = ?
                AND length = ?
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setDouble(2, width);
            ps.setDouble(3, height);
            ps.setDouble(4, length);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapComponent(rs);
            }

            throw new DatabaseException("Component not found");

        } catch (SQLException e) {
            throw new DatabaseException("Error finding component", e.getMessage());
        }
    }

    private static Component mapComponent(ResultSet rs) throws SQLException {
        Component component = new Component();

        component.setId(rs.getInt("cp_id"));
        component.setName(rs.getString("name"));
        component.setWidth(rs.getDouble("width"));
        component.setHeight(rs.getDouble("height"));
        component.setLength(rs.getDouble("length"));
        component.setUnit(rs.getString("unit"));
        component.setDescription(rs.getString("description"));

        return component;
    }
}