package com.tekdynamix.external;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CassandraService {

    //@Inject
    public String getResource() {

        return "hello";

    }
}
