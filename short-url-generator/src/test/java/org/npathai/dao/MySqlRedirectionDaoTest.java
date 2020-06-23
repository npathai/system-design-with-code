package org.npathai.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.npathai.config.MySqlDatasourceConfiguration;
import org.npathai.model.Redirection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.unitils.reflectionassert.ReflectionAssert;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
class MySqlRedirectionDaoTest {

    private static final String ID = "AAAAA";
    private static final String LONG_URL = "www.google.com";
    private static final long CREATED_AT = System.currentTimeMillis();

    @Container
    public MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("short_url_generator")
            .withUsername("testUser")
            .withPassword("unsecured")
            .withInitScript("test-init.sql")
            .withExposedPorts(3306);

    private MySqlRedirectionDao dao;

    @BeforeEach
    public void setUp() throws SQLException {
        MySqlDatasourceConfiguration configuration = new MySqlDatasourceConfiguration();
        configuration.setUser("testUser");
        configuration.setPassword("unsecured");
        configuration.setUrl(String.format("jdbc:mysql://%s:%d/short_url_generator",
                mysqlContainer.getContainerIpAddress(), mysqlContainer.getMappedPort(3306)));
        dao = new MySqlRedirectionDao(configuration);
    }

    @Test
    public void canGetSavedRedirectionFromDatabase() throws DataAccessException {
        Redirection originalRedirection = new Redirection(ID, LONG_URL, CREATED_AT,
                CREATED_AT + 10000,
                UUID.randomUUID().toString());
        dao.save(originalRedirection);

        Optional<Redirection> foundRedirection = dao.getById(originalRedirection.id());
        assertThat(foundRedirection).isNotEmpty();
        ReflectionAssert.assertLenientEquals(originalRedirection, foundRedirection.get());
    }

    @Test
    public void returnsEmptyOptionalAfterDeletingRedirectionFromDatabase() throws DataAccessException {
        Redirection originalRedirection = new Redirection(ID, LONG_URL, CREATED_AT,
                CREATED_AT + 10000);
        dao.save(originalRedirection);
        dao.deleteById(originalRedirection.id());

        Optional<Redirection> foundRedirection = dao.getById(originalRedirection.id());
        assertThat(foundRedirection).isEmpty();
    }
}
