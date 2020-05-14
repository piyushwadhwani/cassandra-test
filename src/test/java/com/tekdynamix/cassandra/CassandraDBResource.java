package com.tekdynamix.cassandra;

import com.datastax.driver.core.Row;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.CassandraQueryWaitStrategy;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CassandraDBResource implements QuarkusTestResourceLifecycleManager {

    private static final Logger log = LoggerFactory.getLogger(CassandraDBResource.class);
    private static GenericContainer<?> cassandraContainer;

    @Inject
    Vertx vertx;

    @Override
    public Map<String, String> start() {
        cassandraContainer = new CassandraContainer<>();
        cassandraContainer.setWaitStrategy(new CassandraQueryWaitStrategy());
        cassandraContainer.start();

        String exposedPort =
                String.valueOf(cassandraContainer.getMappedPort(CassandraContainer.CQL_PORT));

        log.info("Started {} on port {}", cassandraContainer.getDockerImageName(), exposedPort);
        log.info("Started {} on Network {}", cassandraContainer.getDockerImageName(), cassandraContainer.getNetwork());

        HashMap<String, String> hm = new HashMap<>();
        hm.put("quarkus.cassandra.port", exposedPort);
        hm.put("quarkus.cassandra.host", "localhost");

        initializeDatabase("localhost",exposedPort);
        return hm;
    }

    private void initializeDatabase(String host, String port) {
        CassandraClientOptions options = new CassandraClientOptions()
                .addContactPoint(host).setPort(Integer.parseInt(port));
        CassandraClient client = CassandraClient.create(vertx, options);
        client.executeWithFullFetch( "CREATE KEYSPACE IF NOT EXISTS k1 WITH replication "
                + "= {'class':'SimpleStrategy', 'replication_factor':1}",executeWithFullFetch -> {
            if (executeWithFullFetch.succeeded()) {
                log.info("Keyspace Creation Successful ");
                List<Row> rows = executeWithFullFetch.result();
                for (Row row : rows) {
                    // handle each row here
                }
            } else {
                System.out.println("Unable to execute the query");
                executeWithFullFetch.cause().printStackTrace();
            }
        });
        client.executeWithFullFetch("CREATE TABLE IF NOT EXISTS k1.product(id uuid PRIMARY KEY, description text)"
                ,executeWithFullFetch -> {
            if (executeWithFullFetch.succeeded()) {
                log.info("Table Creation Successful ");
                List<Row> rows = executeWithFullFetch.result();
                for (Row row : rows) {
                    // handle each row here
                }
            } else {
                log.error("Unable to execute the query",executeWithFullFetch.cause());
                executeWithFullFetch.cause().printStackTrace();
            }
        });
    }

    @Override
    public void stop() {
        if (cassandraContainer != null && cassandraContainer.isRunning()) {
            cassandraContainer.stop();
        }
    }

}
