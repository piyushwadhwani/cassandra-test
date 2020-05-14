package com.tekdynamix.external;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
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


    @Inject
    Vertx vertx;
    private static final Logger log = LoggerFactory.getLogger(CassandraService.class);


    CassandraClient client;

    @PostConstruct
    void init() {
        log.info("Initializing Cassandra Client");
        CassandraClientOptions options = new CassandraClientOptions()
                .addContactPoint(dbHost).setPort(Integer.parseInt(dbPort));
        client = CassandraClient.createShared(vertx, "sharedClientName", options);
        log.info(" Cassandra Client Connection Status {}", client.isConnected());
    }


    public String getResource() {

        return "hello";

    }
}
