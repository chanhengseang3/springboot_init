package com.loan24;

import lombok.extern.java.Log;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

import java.lang.management.ManagementFactory;

@Log
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        final var runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        final var listOfArguments = runtimeMxBean.getInputArguments();
        for (String argument : listOfArguments) {
            log.info(argument);
        }
        final var app = new SpringApplicationBuilder(Application.class).web(WebApplicationType.SERVLET);
        app.build().addListeners(new ApplicationPidFileWriter("./bin/shutdown.pid"));
        app.run();
    }
}
