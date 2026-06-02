package app.persistence;

import app.persistence.ConnectionPool;
import app.dtos.RequestSummaryDTO;
import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    private final ConnectionPool connectionPool;

    public RequestMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    // Request details
    public RequestSummaryDTO getDetail(int rqId) throws DatabaseException {
        String sql = """
                SELECT
                    r.rq_id,
                    r.created_at,
                    r.status,
                    c.carp_id,
                    c.width         AS carp_width,
                    c.length        AS carp_length,
                    c.height        AS carp_height,
                    rt.name         AS roof_type_name,
                    c.height        AS roof_angle,
                    ci.first_name,
                    ci.last_name,
                    ci.email,
                    ci.phone,
                    (s.shed_id IS NOT NULL) AS has_shed,
                    s.width         AS shed_width,
                    s.length        AS shed_length,
                    z.zipcode,
                    z.town,
                    ci.address,
                    (c.width > 600 OR c.length > 800) AS flagged
                FROM request r
                JOIN carport      c  ON c.carp_id  = r.carp_id
                JOIN roof_type    rt ON rt.rt_id    = c.rt_id
                JOIN contact_info ci ON ci.ci_id    = r.ci_id
                JOIN zip          z  ON z.zip_id    = ci.zip_id
                LEFT JOIN shed    s  ON s.carp_id   = c.carp_id
                WHERE r.rq_id = ?
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, rqId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new DatabaseException("Request not found", "rq_id = " + rqId);
            }

            return new RequestSummaryDTO(
                    rs.getInt("rq_id"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getString("status"),
                    rs.getDouble("carp_width"),
                    rs.getDouble("carp_length"),
                    rs.getDouble("carp_height"),
                    rs.getString("roof_type_name"),
                    rs.getDouble("roof_angle"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getBoolean("has_shed"),
                    rs.getBoolean("flagged")
            );

        } catch (SQLException e) {
            throw new DatabaseException("Could not fetch request detail", e.getMessage());
        }
    }

    // Get the carport ID (needed for SalesPerson)
    public int getCarpIdByRequestId(int rqId) throws DatabaseException {
        String sql = "SELECT carp_id FROM request WHERE rq_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, rqId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new DatabaseException("Request not found", "rq_id = " + rqId);
            }

            return rs.getInt("carp_id");

        } catch (SQLException e) {
            throw new DatabaseException("Could not fetch carp_id", e.getMessage());
        }
    }

    // Roof types
    public List<RoofType> getAllRoofTypes() throws DatabaseException {
        String sql = "SELECT rt_id, name FROM roof_type ORDER BY name ASC";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<RoofType> roofTypes = new ArrayList<>();
            while (rs.next()) {
                roofTypes.add(new RoofType(
                        rs.getInt("rt_id"),
                        rs.getString("name")
                ));
            }
            return roofTypes;

        } catch (SQLException e) {
            throw new DatabaseException("Could not fetch roof types", e.getMessage());
        }
    }

    public ContactInfo findContactInfoByUserId(int userId) throws DatabaseException {
        String sql = """
                SELECT
                    ci.ci_id,
                    ci.first_name,
                    ci.last_name,
                    ci.address,
                    ci.phone,
                    ci.email,
                    z.zip_id,
                    z.zipcode,
                    z.town
                FROM contact_info ci
                JOIN zip z ON z.zip_id = ci.zip_id
                WHERE ci.user_id = ?
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Zip zip = new Zip(
                        rs.getInt("zip_id"),
                        rs.getString("zipcode"),
                        rs.getString("town")
                );
                return new ContactInfo(
                        rs.getInt("ci_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        zip
                );
            }
            return null; // no contact info found for this user

        } catch (SQLException e) {
            throw new DatabaseException("Could not fetch contact info", e.getMessage());
        }
    }

    // Contact info
    public int createContactInfo(Integer userId, String firstName, String lastName,
                                 String address, String phone,
                                 String email, int zipId) throws DatabaseException {
        String sql = """
                INSERT INTO contact_info (user_id, first_name, last_name, address, phone, email, zip_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING ci_id
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            if (userId != null) {
                ps.setInt(1, userId);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, address);
            ps.setString(5, phone);
            ps.setString(6, email);
            ps.setInt(7, zipId);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("ci_id");

        } catch (SQLException e) {
            throw new DatabaseException("Could not create contact info", e.getMessage());
        }
    }

    // Zip
    public Zip findZipByZipcode(String zipcode) throws DatabaseException {
        String sql = "SELECT zip_id, zipcode, town FROM zip WHERE zipcode = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, zipcode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Zip(
                        rs.getInt("zip_id"),
                        rs.getString("zipcode"),
                        rs.getString("town")
                );
            }
            return null;

        } catch (SQLException e) {
            throw new DatabaseException("Could not find zip", e.getMessage());
        }
    }

    public int createZip(String zipcode, String town) throws DatabaseException {
        String sql = """
                INSERT INTO zip (zipcode, town)
                VALUES (?, ?)
                RETURNING zip_id
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, zipcode);
            ps.setString(2, town);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("zip_id");

        } catch (SQLException e) {
            throw new DatabaseException("Could not create zip", e.getMessage());
        }
    }

    // Carport
    public int createCarport(Integer userId, int rtId,
                             double length, double width,
                             double height) throws DatabaseException {
        String sql = """
                INSERT INTO carport (user_id, rt_id, length, width, height, created_at)
                VALUES (?, ?, ?, ?, ?, NOW())
                RETURNING carp_id
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            if (userId != null) {
                ps.setInt(1, userId);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setInt(2, rtId);
            ps.setDouble(3, length);
            ps.setDouble(4, width);
            ps.setDouble(5, height);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("carp_id");

        } catch (SQLException e) {
            throw new DatabaseException("Could not create carport", e.getMessage());
        }
    }

    // Skur
    public int createShed(int carpId, double length, double width) throws DatabaseException {
        String sql = """
                INSERT INTO shed (carp_id, length, width)
                VALUES (?, ?, ?)
                RETURNING shed_id
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, carpId);
            ps.setDouble(2, length);
            ps.setDouble(3, width);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("shed_id");

        } catch (SQLException e) {
            throw new DatabaseException("Could not create shed", e.getMessage());
        }
    }

    // Request
    public int createRequest(int carpId, int ciId) throws DatabaseException {
        String sql = """
                INSERT INTO request (carp_id, ci_id, created_at, status)
                VALUES (?, ?, NOW(), 'PENDING')
                RETURNING rq_id
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, carpId);
            ps.setInt(2, ciId);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("rq_id");

        } catch (SQLException e) {
            throw new DatabaseException("Could not create request", e.getMessage());
        }
    }

    // Get summary
    public List<RequestSummaryDTO> getAllSummaries() throws DatabaseException {
        String sql = """
                SELECT
                    r.rq_id,
                    r.created_at,
                    r.status,
                    c.width                             AS carp_width,
                    c.length                            AS carp_length,
                    c.height                            AS carp_height,
                    rt.name                             AS roof_type_name,
                    c.roof_angle                        AS roof_angle,
                    ci.first_name,
                    ci.last_name,
                    ci.email,
                    ci.phone,
                    (s.shed_id IS NOT NULL)             AS has_shed,
                    (c.width > 600 OR c.length > 800)   AS flagged
                FROM request r
                JOIN carport      c  ON c.carp_id = r.carp_id
                JOIN roof_type    rt ON rt.rt_id   = c.rt_id
                JOIN contact_info ci ON ci.ci_id   = r.ci_id
                LEFT JOIN shed    s  ON s.carp_id  = c.carp_id
                ORDER BY r.created_at DESC
                """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<RequestSummaryDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new RequestSummaryDTO(
                        rs.getInt("rq_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getDouble("carp_width"),
                        rs.getDouble("carp_length"),
                        rs.getDouble("carp_height"),
                        rs.getString("roof_type_name"),
                        rs.getDouble("roof_angle"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("has_shed"),
                        rs.getBoolean("flagged")
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Could not fetch request summaries", e.getMessage());
        }


    }

    public void updateStatus(int rqId, String status) throws DatabaseException {
        String sql = "UPDATE request SET status = ? WHERE rq_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, rqId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not update request status", e.getMessage());
        }
    }
}