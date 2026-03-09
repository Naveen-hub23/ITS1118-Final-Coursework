package lk.ijse.triplea.dao;

import lk.ijse.triplea.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CRUDUtil {
    public static <T> T execute(String sql, Object... args) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        for (int i = 0; i < args.length; i++) {
            pstm.setObject((i + 1), args[i]);
        }

        if (sql.startsWith("SELECT") || sql.startsWith("select")) {
            return (T) pstm.executeQuery();
        }
        return (T) (Boolean) (pstm.executeUpdate() > 0);
    }
}

