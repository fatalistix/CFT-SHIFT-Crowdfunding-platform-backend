package ru.cft.shift.intensive.balashov.crowdfunding.actuator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.health.Status;
import org.springframework.dao.DataAccessException;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Endpoint(id = "database")
@ConditionalOnEnabledHealthIndicator("database")
public class DatabaseStatusEndpoint {
    private final Logger log = LoggerFactory.getLogger(DatabaseStatusEndpoint.class);
    private final CassandraOperations cassandraOperations;

    @Autowired
    public DatabaseStatusEndpoint(CassandraOperations cassandraOperations) {
        this.cassandraOperations = cassandraOperations;
    }

    @ReadOperation
    public Map<String, Status> cassandraDatabaseStatus() {
        return Map.of("cassandra", cassandraStatus());
    }

    private Status cassandraStatus() {
        try {
            cassandraOperations.select("select * from system.local", Object.class);
            log.info("Actuator: Cassandra is up");
            return Status.UP;
        } catch (DataAccessException e) {
            log.warn("Actuator: Cassandra is down");
            return Status.DOWN;
        }
    }
}
