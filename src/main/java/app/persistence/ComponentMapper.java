package app.persistence;

import app.entities.Component;
import app.exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ComponentMapper {

    public static List<Component>
    findComponentsByName(
            String name,
            ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
                SELECT cp_id,
                       name,
                       width,
                       height,
                       length,
                       unit,
                       description
                FROM component
                WHERE name = ?
                ORDER BY length ASC
                """;

        try (Connection connection =
                     connectionPool.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            List<Component> components =
                    new ArrayList<>();

            while (rs.next()) {

                components.add(
                        mapComponent(rs)
                );
            }

            if (components.isEmpty()) {

                throw new DatabaseException(
                        "No components found for: "
                                + name
                );
            }

            return components;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Error finding components",
                    e.getMessage()
            );
        }
    }

    private static Component mapComponent(
            ResultSet rs)
            throws SQLException {

        Component component =
                new Component();

        component.setId(
                rs.getInt("cp_id")
        );

        component.setName(
                rs.getString("name")
        );

        component.setWidth(
                rs.getDouble("width")
        );

        component.setHeight(
                rs.getDouble("height")
        );

        component.setLength(
                rs.getDouble("length")
        );

        component.setUnit(
                rs.getString("unit")
        );

        component.setDescription(
                rs.getString("description")
        );

        return component;
    }
}