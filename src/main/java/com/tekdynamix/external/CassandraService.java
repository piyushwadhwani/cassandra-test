package com.tekdynamix.external;

import com.datastax.driver.core.Row;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CassandraService {


    @ConfigProperty(name = "quarkus.cassandra.host")
    String dbHost;

    @ConfigProperty(name = "quarkus.cassandra.port")
    String dbPort;


    @Inject
    Vertx vertx;
    private static final Logger log = LoggerFactory.getLogger(CassandraService.class);


   // CassandraClient client;

    @PostConstruct
    void init() {
       /* log.info("Initializing Cassandra Client");
        CassandraClientOptions options = new CassandraClientOptions()
                .addContactPoint(dbHost).setPort(Integer.parseInt(dbPort));
        client = CassandraClient.createShared(vertx, "sharedClientName", options);
        log.info(" Cassandra Client Connection Status {}", client.isConnected());
        */

    }


    public String getResource() {
        /*client.executeWithFullFetch("SELECT * FROM my_keyspace.my_table where my_key = 'my_value'", executeWithFullFetch -> {
            if (executeWithFullFetch.succeeded()) {
                List<Row> rows = executeWithFullFetch.result();
                for (Row row : rows) {
                   log.info("Rows {}",row);
                }
            } else {
                log.error("Unable to execute the query",executeWithFullFetch.cause());
                executeWithFullFetch.cause().printStackTrace();
            }
        });*/

        return "hello";

    }
}
