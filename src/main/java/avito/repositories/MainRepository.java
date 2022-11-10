package avito.repositories;

import avito.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class MainRepository {

    final private JdbcTemplate jdbc;
    final private static String TABLE_CLIENTS = "clients";

    @Autowired
    public MainRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void addNewClient(int user_id,BigDecimal initialAmount) {
        String sql = "INSERT INTO " + TABLE_CLIENTS + "(id,amount) VALUES (?,?)";

        PreparedStatementSetter preparedStatement = (ps) -> {
            ps.setInt(1,user_id);
            ps.setBigDecimal(2, initialAmount);
        };

        jdbc.update(sql, preparedStatement);
    }

    public void addOnBalance(int user_id, BigDecimal newAmount) {
        String sql = "UPDATE clients\n" +
                "    SET amount = ?\n" +
                "WHERE id = ?;";

        PreparedStatementSetter preparedStatement = (ps) -> {

            ps.setBigDecimal(1, newAmount);
            ps.setInt(2, user_id);
        };
        jdbc.update(sql, preparedStatement);
    }

    public void saveServiceRecord(String table, int user_id, int id_service, int id_order, BigDecimal amount) {
        String sql = "INSERT INTO " + table + "(user_id,service_id,order_id,amount) \n" +
                "VALUES  (?,?,?,?);";

        PreparedStatementSetter preparedStatement = (ps) -> {

            ps.setInt(1, user_id);
            ps.setInt(2, id_service);
            ps.setInt(3, id_order);
            ps.setBigDecimal(4, amount);
        };

        jdbc.update(sql, preparedStatement);
    }

    public void removeServiceRecord(String table, int user_id, int id_service, int id_order, BigDecimal amount) {
        String sql = "DELETE FROM " + table + "\n" +
                "WHERE user_id = ? AND service_id = ? AND order_id = ? AND amount = ?;\n";

        PreparedStatementSetter preparedStatement = (ps) -> {
            ps.setInt(1, user_id);
            ps.setInt(2, id_service);
            ps.setInt(3, id_order);
            ps.setBigDecimal(4, amount);
        };
        jdbc.update(sql, preparedStatement);
    }

    public BigDecimal getBalance(int user_id) {
        String sql = "SELECT amount \n" +
                "FROM clients\n" +
                "WHERE id = ?";

        PreparedStatementSetter preparedStatement = (ps) -> {
            ps.setInt(1, user_id);
        };

        RowMapper<BigDecimal> rowMapper = (rs, rowNum) -> {
            return rs.getBigDecimal("amount");
        };

        List<BigDecimal> list = jdbc.query(sql, preparedStatement, rowMapper);
        return (list.size() != 0) ? list.get(0) : null;

    }

    public boolean isConsistRecord(String table, int user_id, int id_service, int id_order, BigDecimal amount){
        String sql = "SELECT *\n" +
                "FROM " + table +"\n" +
                "WHERE user_id = ? AND service_id = ? AND order_id = ? AND amount = ?;";
        PreparedStatementSetter preparedStatement = (ps) -> {
            ps.setInt(1, user_id);
            ps.setInt(2, id_service);
            ps.setInt(3, id_order);
            ps.setBigDecimal(4, amount);
        };

        RowMapper<Payment> rowMapper = (rs, rowNum) -> {
            Payment payment = new Payment();
            payment.setUser_id(rs.getInt("user_id"));
            payment.setService_id(rs.getInt("service_id"));
            payment.setOrder_id(rs.getInt("order_id"));
            payment.setAmount(rs.getBigDecimal("amount"));
            return payment;
        };

        List<Payment> list = jdbc.query(sql,preparedStatement,rowMapper);
        return !list.isEmpty();
    }
}
