package com.tekdynamix.cassandra;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.CassandraQueryWaitStrategy;

import java.util.HashMap;
import java.util.Map;


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

        log.info("Started {} on port {}", cassandraContainer.getDockerImageName(), exposedPort);
        log.info("Started {} on host {}", cassandraContainer.getDockerImageName(), cassandraContainer.getTestHostIpAddress());

        HashMap<String, String> hm = new HashMap<>();
        hm.put("quarkus.cassandra.port", exposedPort);
        hm.put("quarkus.cassandra.host", "localhost");
        return hm;
    }

    @Override
    public void stop() {
        if (cassandraContainer != null && cassandraContainer.isRunning()) {
            cassandraContainer.stop();
        }
    }

}
