package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PriceCatalogMapper {

    public static double getCurrentPrice(
            int componentId,
            ConnectionPool connectionPool)
            throws DatabaseException {

        String sql = """
                SELECT unit_price
                FROM price_catalog
                WHERE cp_id = ?
                AND valid_to IS NULL
                """;

        try (Connection connection = connectionPool.getConnection();

             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, componentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("unit_price");
            }

            throw new DatabaseException(
                    "No active price found for component id: "
                            + componentId
            );

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Error fetching component price",
                    e.getMessage()
            );
        }
    }
}