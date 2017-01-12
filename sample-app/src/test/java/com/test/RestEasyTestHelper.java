package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.SocketUtils;

import io.restassured.RestAssured;

/**
 * Integration test for a rest services using spring boot. This test will start
 * the spring boot application in an embedded container and executes real http
 * requests against it. The responses are verified in the test.
 *
 * @author Christian Fehmer
 *
 */
public class RestEasyTestHelper {

    private RestEasyTestHelper() {
        // helper class
    }

    /**
     * Starts the application in an embedded container once per test class on a
     * random port.
     * 
     * @param applicationClass
     *            the main application class, most likely annotated with
     *            {@link SpringBootApplication} and containing a static main
     *            method with a <code>SpringApplication.run(...)</code>
     *            statement.
     * @param basePath
     *            base path of the service under test, e.g.
     *            /sample-app/my-service. Contains of the
     *            {@link javax.ws.rs.ApplicationPath} and
     *            {@link javax.ws.rs.Path} annotations of your application class
     *            and the service implementation.
     */
    public static ConfigurableApplicationContext startServer(Class<?> applicationClass, Class<?> testClass, String basePath) {
        // find a free port
        int port = SocketUtils.findAvailableTcpPort(10000);

        // start the application on the specific port. Note that this test
        // class is also part of the applications spring context
        ConfigurableApplicationContext context = SpringApplication.run(new Object[] { applicationClass, testClass}, new String[] { "--server.port=" + port });

        // set base url for all restassured interactions
        RestAssured.baseURI = "http://localhost:" + port + basePath;

        return context;
    }

    /**
     * shutdown the application after the test class is finished
     */
    public static void stopServer(ConfigurableApplicationContext context) {
        if (context != null)
            context.close();
    }
}
