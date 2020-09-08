package org.npathai.dao;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.npathai.config.MySqlDatasourceConfiguration;
import org.npathai.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.npathai.metrics.ServiceTags.DatabaseTags.*;

public class MySqlUserDao implements UserDao {
    private static final String SELECT_BY_NAME_SQL = "select username, password, email, BIN_TO_UUID(id, true) as uid "
            + "from users where username=?";

    private final HikariDataSource dataSource;
    private final MeterRegistry meterRegistry;

    public MySqlUserDao(MySqlDatasourceConfiguration datasourceConfiguration, MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        dataSource = new HikariDataSource();
        dataSource.setUsername(datasourceConfiguration.getUser());
        dataSource.setPassword(datasourceConfiguration.getPassword());
        dataSource.setJdbcUrl(datasourceConfiguration.getUrl());
    }

    @Override
    public Optional<User> getUserByName(String username) throws DataAccessException {
        Tags commonTags = databaseTags("user.service", "authentication",
                "getByUserName");

        meterRegistry.counter("database.requests.total", commonTags).increment();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = prepareGetByUsernameStatement(connection, username);
             ResultSet resultSet = ps.executeQuery();
        ) {
            if (!resultSet.next()) {
                return Optional.empty();
            }
            meterRegistry.counter("database.responses.total", databaseSuccessTags(commonTags)).increment();
            return Optional.of(toUser(resultSet));
        } catch (SQLException ex) {
            meterRegistry.counter("database.responses.total", databaseErrorTags(commonTags)).increment();
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
        user.setId(resultSet.getString("uid"));
        return user;
    }
}
