package com.tekdynamix.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.CassandraQueryWaitStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class CassandraDBResource implements QuarkusTestResourceLifecycleManager {

    private static final Logger log = LoggerFactory.getLogger(CassandraDBResource.class);
    private static GenericContainer<?> cassandraContainer;
    private static CassandraContainer<?> dd;
   // @ConfigProperty(name = "congruent.cassandra.signup.keyspace")
    //Optional<String> keyspaceProp;

    //@ConfigProperty(name = "congruent.cassandra.signup.table")
    //Optional<String> tableProp;
    String table="signup";
    String keyspace="congruent";


    @Override
    public Map<String, String> start() {

      //  this.table = tableProp.orElse("signup");
      //  this.keyspace = keyspaceProp.orElse("congruent");


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
        } catch (Exception e) {
            log.error("Error in Initializing Resource ", e);
            HashMap<String, String> hm = new HashMap<>();
            hm.put("quarkus.cassandra.port", "9999");
            hm.put("quarkus.cassandra.host", "localhost");
            return hm;
        }
    }

    private void initializeDatabase(String host, String port) {
        Cluster cluster = null;

        log.info("Initializing Database...");


        try {

            cluster = Cluster.builder()                                                   // (1)
                    .withoutJMXReporting().withoutMetrics().addContactPoint(host).withPort(Integer.parseInt(port))
                    .build();
            Session session = cluster.connect();
            ResultSet rs = session.execute("select release_version from system.local");    // (3)
            Row row = rs.one();
            log.info("Release Version for Cassandra is" + row.getString("release_version"));
            log.info("Initializing Keyspace ..... {}", this.keyspace);
            rs = session.execute("CREATE KEYSPACE IF NOT EXISTS " + keyspace + " WITH replication "
                    + "= {'class':'SimpleStrategy', 'replication_factor':1}");    // (3)
            row = rs.one();
            log.info("Keyspace Cassandra is {} ", row);

            log.info("Initializing Table ..... {}", table);
            rs = session.execute("CREATE TABLE IF NOT EXISTS "
                    + keyspace + "." + table + " (username text PRIMARY KEY, password text,email text)");
            row = rs.one();
            log.info("Table Cassandra is {} ", row);


        } catch (Exception e) {
            log.error("Catch Exception while creating cassandra test db", e);

        } finally {
            if (cluster != null) cluster.close();                                          // (5)
        }
    }

    private void initializeKeyspace(Session session) {
    }

    @Override
    public void stop() {
        if (cassandraContainer != null && cassandraContainer.isRunning()) {
            cassandraContainer.stop();
        }
    }

}


