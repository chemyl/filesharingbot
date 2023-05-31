package org.amaltsev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NodeApplication {

    //    Runner class. main() as an entry point.
    //    Run as SpringBoot Application with Tomcat inside
    public static void main(String[] args) {
        SpringApplication.run(NodeApplication.class);
    }
}
