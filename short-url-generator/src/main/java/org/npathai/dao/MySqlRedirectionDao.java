package org.npathai.dao;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.npathai.model.Redirection;
import org.npathai.properties.ApplicationProperties;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public class MySqlRedirectionDao implements RedirectionDao {

    public static final String SELECT_BY_ID_SQL = "select * from redirection where id = ?";
    private static final String INSERT_SQL = "insert into redirection (id, long_url, created_at) values (?, ?, ?)";
    private final MysqlDataSource dataSource;

    // FIXME constructor is doing real work
    // FIXME use connection pooling
    public MySqlRedirectionDao(Properties properties) throws SQLException {
        dataSource = new MysqlDataSource();
        dataSource.setUser(Objects.requireNonNull(properties.getProperty(ApplicationProperties.MYSQL_USER.name())));
        dataSource.setPassword(Objects.requireNonNull(properties.getProperty(ApplicationProperties.MYSQL_PASSWORD.name())));
        dataSource.setUrl(Objects.requireNonNull(properties.getProperty(ApplicationProperties.MYSQL_URL.name())));
        // FIXME how to ensure DB availability when services are starting dynamically and don't have control over when
        // DB will start. Commenting this check because causes intermittent failures in docker compose
        // dataSource.getConnection().close();
    }

    @Override
    public void save(Redirection redirection) throws DataAccessException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {

            preparedStatement.setString(1, redirection.id());
            preparedStatement.setString(2, redirection.longUrl());
            preparedStatement.setTimestamp(3, new Timestamp(redirection.createdAt()));

            int count = preparedStatement.executeUpdate();
            assert count == 1;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public Optional<Redirection> getById(@Nonnull String id) throws DataAccessException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = createSelectByIdStatement(connection, id);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(createShortUrl(resultSet));
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    private PreparedStatement createSelectByIdStatement(Connection connection, String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_SQL);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private Redirection createShortUrl(ResultSet resultSet) throws SQLException {
        return new Redirection(resultSet.getString("id"), resultSet.getString("long_url"),
                resultSet.getTimestamp("created_at").getTime());
    }
}
