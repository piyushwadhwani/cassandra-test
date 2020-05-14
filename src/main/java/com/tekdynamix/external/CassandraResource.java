package com.tekdynamix.external;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class CassandraResource {

    @Inject
    CassandraService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        return service.getResource();
    }
}
