package org.npathai.dao;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.npathai.config.MySqlDatasourceConfiguration;
import org.npathai.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MySqlUserDao implements UserDao {
    private static final String SELECT_BY_NAME_SQL = "select username, password, email from users where username=?";

    private final MysqlDataSource dataSource;

    public MySqlUserDao(MySqlDatasourceConfiguration datasourceConfiguration) {
        dataSource = new MysqlDataSource();
        dataSource.setUser(datasourceConfiguration.getUser());
        dataSource.setPassword(datasourceConfiguration.getPassword());
        dataSource.setUrl(datasourceConfiguration.getUrl());
    }

    @Override
    public Optional<User> getUserByName(String username) throws DataAccessException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = prepareGetByUsernameStatement(connection, username);
             ResultSet resultSet = ps.executeQuery();
        ) {
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(toUser(resultSet));
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    private PreparedStatement prepareGetByUsernameStatement(Connection connection, String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_NAME_SQL);
        preparedStatement.setString(1, username);
        return preparedStatement;
    }

    private User toUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        return user;
    }
}
