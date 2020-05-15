package com.tekdynamix.external;

import com.datastax.oss.driver.api.core.CqlSession;
import io.vertx.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CassandraService {


    @ConfigProperty(name = "quarkus.cassandra.host")
    String dbHost;

    @ConfigProperty(name = "quarkus.cassandra.port")
    String dbPort;


    @ConfigProperty(name = "congruent.cassandra.signup.keyspace")
    String keysapce;

    @ConfigProperty(name = "congruent.cassandra.signup.table")
    String table;


    @Inject
    Vertx vertx;
    private static final Logger log = LoggerFactory.getLogger(CassandraService.class);


    @Inject
    CqlSession session;


    //CassandraClient client;

    @PostConstruct
    void init() {
        log.info("Initializing Cassandra Client");

        //CassandraClientOptions options = new CassandraClientOptions()
        //      .addContactPoint(dbHost).setPort(Integer.parseInt(dbPort));
        //client = CassandraClient.createShared(vertx, "sharedClientName", options);
        log.info(" Cassandra Client Connection Status {}", session.getMetadata());


    }


    public String getResource() {

        session.executeReactive("SELECT * FROM " + keysapce + "." + table);

       /* client.executeWithFullFetch("SELECT * FROM "+keysapce+"."+table+"  ", executeWithFullFetch -> {
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
