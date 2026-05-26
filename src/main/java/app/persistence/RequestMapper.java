package app.persistence;

import app.dtos.RequestSummaryDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    private final ConnectionPool connectionPool;

    public RequestMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public int create(int carpId, int ciId) throws SQLException {
        String sql = """
            INSERT INTO request (carp_id, ci_id, created_at, status)
            VALUES (?, ?, NOW(), 'PENDING')
            RETURNING rq_id
            """;
        try (Connection c  = connectionPool.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, carpId);
            ps.setInt(2, ciId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("rq_id");
        }
    }

    public List<RequestSummaryDTO> getAllSummaries() throws SQLException {
        String sql = """
            SELECT
                r.rq_id,
                r.created_at,
                r.status,
                c.width                          AS carp_width,
                c.length                         AS carp_length,
                rt.name                          AS roof_type_name,
                ci.first_name,
                ci.last_name,
                ci.email,
                ci.phone,
                (s.shed_id IS NOT NULL)          AS has_shed,
                (c.width > 600 OR c.length > 800) AS flagged
            FROM request r
            JOIN carport      c  ON c.carp_id = r.carp_id
            JOIN roof_type    rt ON rt.rt_id   = c.rt_id
            JOIN contact_info ci ON ci.ci_id   = r.ci_id
            LEFT JOIN shed    s  ON s.carp_id  = c.carp_id
            ORDER BY r.created_at DESC
            """;

        List<RequestSummaryDTO> list = new ArrayList<>();

        try (Connection c  = connectionPool.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs  = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new RequestSummaryDTO(
                        rs.getInt("rq_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getDouble("carp_width"),
                        rs.getDouble("carp_length"),
                        rs.getString("roof_type_name"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("has_shed"),
                        rs.getBoolean("flagged")
                ));
            }
        }
        return list;
    }

    /* ── Update request status ── */
    public void updateStatus(int rqId, String status) throws SQLException {
        String sql = "UPDATE request SET status = ? WHERE rq_id = ?";
        try (Connection c  = connectionPool.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, rqId);
            ps.executeUpdate();
        }
    }
}