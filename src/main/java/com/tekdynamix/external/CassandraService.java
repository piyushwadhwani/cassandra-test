package com.tekdynamix.external;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CassandraService {


    public String getResource() {

        return "hello";

    }
}
