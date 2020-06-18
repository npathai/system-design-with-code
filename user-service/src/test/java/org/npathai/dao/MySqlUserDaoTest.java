package org.npathai.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.npathai.config.MySqlDatasourceConfiguration;
import org.npathai.model.User;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.unitils.reflectionassert.ReflectionAssert;

import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class MySqlUserDaoTest {

    @Container
    public MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("user_db")
            .withUsername("testUser")
            .withPassword("unsecured")
            .withInitScript("test-user-init.sql")
            .withExposedPorts(3306);

    private MySqlUserDao dao;

    @BeforeEach
    public void setUp() throws SQLException {
        MySqlDatasourceConfiguration configuration = new MySqlDatasourceConfiguration();
        configuration.setUser("testUser");
        configuration.setPassword("unsecured");
        configuration.setUrl(String.format("jdbc:mysql://%s:%d/user_db",
                mysqlContainer.getContainerIpAddress(), mysqlContainer.getMappedPort(3306)));
        dao = new MySqlUserDao(configuration);
    }

    @Test
    public void canGetRootUser() throws DataAccessException {
        User rootUser = new User();
        rootUser.setUsername("root");
        rootUser.setPassword("root");

        Optional<User> optionalRoot = dao.getUserByName("root");
        assertThat(optionalRoot).isNotEmpty();
        ReflectionAssert.assertLenientEquals(rootUser, optionalRoot.get());
    }
}