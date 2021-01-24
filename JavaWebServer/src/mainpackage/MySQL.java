package mainpackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * classe database MySQL
 * @author Francesco Lizzio
 */
public class MySQL {
    private final String driver="com.mysql.cj.jdbc.Driver";
    private String url_db="jdbc:mysql://localhost:3306/mysql_db?serverTimezone=Europe/Rome";
    private String query;
    private Connection connection;
    private ResultSet result;
    public MySQL() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        connection=DriverManager.getConnection(url_db, "root", "root");
    }
    public void setQuery(String q){
        if(q.toUpperCase().startsWith("SELECT")){
            query=q;
        }
        else{
            query="select \"Query exception\";";
        }
    }
    public ResultSet startQuery() throws SQLException{
        Statement statement=connection.createStatement();
        result=statement.executeQuery(query);
        return result;
    }
    public ResultSet startQuery(String q) throws SQLException{
        setQuery(q);
        Statement statement=connection.createStatement();
        result=statement.executeQuery(query);
        return result;
    }
    public void close() throws SQLException{
        connection.close();
    }

    public void setUrl_db(String url_db) {
        this.url_db = url_db;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl_db() {
        return url_db;
    }

    public String getQuery() {
        return query;
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet getResult() {
        return result;
    }
}
