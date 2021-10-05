package proger.savcheg.cibinternstesttask;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.http.HttpRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SockDAO {
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/socks";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Savcheg";


    private static Connection connection;

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Sock> showAll() {
        List<Sock> sockList = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM sockslist";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Sock sock = new Sock();
                sock.setId(resultSet.getInt("id"));
                sock.setColor(resultSet.getString("color"));
                sock.setCottonPart(resultSet.getInt("cottonpart"));
                sock.setQuantity(resultSet.getInt("quantity"));

                sockList.add(sock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sockList;
    }

    public void newSocks(Sock sock) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into sockslist values (?,?,?,?)");
            preparedStatement.setInt(1, sock.hashCode());
            preparedStatement.setString(2, sock.getColor());
            preparedStatement.setInt(3, sock.getCottonPart());
            preparedStatement.setInt(4, sock.getQuantity());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Sock> showWithParam(String color, String operation, int cottonPart) {
        List<Sock> sockList = new ArrayList<>();
        try {
            PreparedStatement preparedStatementMT =
                    connection.prepareStatement("select * from sockslist where cottonpart > ? and color = ?");
            PreparedStatement preparedStatementLT =
                    connection.prepareStatement("select * from sockslist where cottonpart < ? and color = ?");
            PreparedStatement preparedStatementE =
                    connection.prepareStatement("select * from sockslist where cottonpart = ? and color = ?");
            preparedStatementMT.setInt(1, cottonPart);
            preparedStatementMT.setString(2, color);
            preparedStatementLT.setInt(1, cottonPart);
            preparedStatementLT.setString(2, color);
            preparedStatementE.setInt(1, cottonPart);
            preparedStatementE.setString(2, color);
            ResultSet resultSet = null;
            if (operation.equals("moreThan"))
                resultSet = preparedStatementMT.executeQuery();
            if (operation.equals("lessThan"))
                resultSet = preparedStatementLT.executeQuery();
            if (operation.equals("equal"))
                resultSet = preparedStatementE.executeQuery();

            while (resultSet.next()) {
                Sock sock = new Sock();
                sock.setId(resultSet.getInt("id"));
                sock.setColor(resultSet.getString("color"));
                sock.setCottonPart(resultSet.getInt("cottonpart"));
                sock.setQuantity(resultSet.getInt("quantity"));

                sockList.add(sock);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sockList;
    }

    public HttpStatus income(Sock sock) { //принимает JSON файл
        /**
         * Если в БД есть объект с id = hash(color, cottonPart) то увеличить количество пар носков
         *
         * Иначе создать новый объект в таблице
         */
        try {
            PreparedStatement checkStatement =
                    connection.prepareStatement("select * from sockslist where id = ?");
            PreparedStatement updateStatement =
                    connection.prepareStatement("update sockslist set quantity = quantity + ? where id = ?");
            checkStatement.setInt(1, sock.hashCode());
            ResultSet resultSet = checkStatement.executeQuery();
            if (!resultSet.next()) {
                sock.setId(sock.hashCode());
                newSocks(sock);
            } else {
                updateStatement.setInt(1, sock.getQuantity());
                updateStatement.setInt(2, sock.hashCode());
                updateStatement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.OK;
    }

    public HttpStatus outcome(Sock sock) {
        System.out.println(sock);
        try {
            PreparedStatement checkStatement =
                    connection.prepareStatement("select * from sockslist where id = ?");
            PreparedStatement updateStatement =
                    connection.prepareStatement("update sockslist set quantity = quantity - ? where id = ?");
            PreparedStatement deleteStatement =
                    connection.prepareStatement("delete from sockslist where id = ?");

            checkStatement.setInt(1, sock.hashCode());
            ResultSet resultSet = checkStatement.executeQuery();
            updateStatement.setInt(1, sock.getQuantity());
            updateStatement.setInt(2, sock.hashCode());
            deleteStatement.setInt(1, sock.hashCode());

            if (!resultSet.next())
                return HttpStatus.BAD_REQUEST;
            if (resultSet.getInt("quantity") < sock.getQuantity())
                return HttpStatus.BAD_REQUEST;
            if (resultSet.getInt("quantity") == sock.getQuantity()){
             deleteStatement.executeUpdate();
             return HttpStatus.OK;
            }
            updateStatement.executeUpdate();
            return HttpStatus.OK;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
