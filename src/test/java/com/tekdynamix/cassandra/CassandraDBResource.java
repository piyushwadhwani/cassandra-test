package com.tekdynamix.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
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
    private static CassandraContainer<?> dd;


    @Override
    public Map<String, String> start() {
        try {
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

             initializeDatabase("localhost", exposedPort);
            return hm;
        }
        catch(Exception e) {
            log.error("Error in Initializing Resource ",e);
            HashMap<String, String> hm = new HashMap<>();
            hm.put("quarkus.cassandra.port", "9999");
            hm.put("quarkus.cassandra.host", "localhost");
            return hm;
        }
    }

    private void initializeDatabase(String host, String port) {
        Cluster cluster = null;


        try {

            cluster = Cluster.builder()                                                    // (1)
                    .addContactPoint(host).withPort(Integer.parseInt(port))
                    .build();
            Session session = cluster.connect();
            ResultSet rs = session.execute("select release_version from system.local");    // (3)
            Row row = rs.one();
            log.info("Release Version for Cassandra is "+row.getString("release_version"));


        } catch (Exception e) {
           log.error("Catch Exception while creating cassandra test db",e);

        } finally {
            if (cluster != null) cluster.close();                                          // (5)
        }
    }

    @Override
    public void stop() {
        if (cassandraContainer != null && cassandraContainer.isRunning()) {
            cassandraContainer.stop();
        }
    }

}
