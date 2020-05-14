package com.tekdynamix.cassandra;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.CassandraQueryWaitStrategy;
import org.testcontainers.junit.jupiter.Container;

import java.util.Collections;
import java.util.Map;

import static org.jboss.resteasy.resteasy_jaxrs.i18n.LogMessages.LOGGER;

public class CassandraDBResource implements QuarkusTestResourceLifecycleManager {

    private static final Logger log = LoggerFactory.getLogger(CassandraDBResource.class);
    private static GenericContainer<?> cassandraContainer;

    @Override
    public Map<String, String> start() {
        cassandraContainer = new CassandraContainer<>();
        cassandraContainer.setWaitStrategy(new CassandraQueryWaitStrategy());
        cassandraContainer.start();
        String exposedPort =
                String.valueOf(cassandraContainer.getMappedPort(CassandraContainer.CQL_PORT));
        LOGGER.infof("Started %s on port %s", cassandraContainer.getDockerImageName(), exposedPort);
        return Collections.singletonMap("quarkus.cassandra.docker_port", exposedPort);
    }

    @Override
    public void stop() {
        if (cassandraContainer != null && cassandraContainer.isRunning()) {
            cassandraContainer.stop();
        }
    }

}
