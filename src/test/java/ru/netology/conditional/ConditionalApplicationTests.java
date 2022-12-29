package ru.netology.conditional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.netology.conditional.profile.DevProfile;
import ru.netology.conditional.profile.ProductionProfile;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConditionalApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    private GenericContainer<?> devapp = new GenericContainer<>("devapp")
            .withExposedPorts(8080);

    @Container
    private GenericContainer<?> prodapp = new GenericContainer<>("prodapp")
            .withExposedPorts(8081);

    @BeforeEach
    public void setUp() {
        devapp.start();
        prodapp.start();
    }

    @Test
    void contextLoads() {
        ResponseEntity<String> devResponse = restTemplate.getForEntity("http://localhost:" +
                devapp.getMappedPort(8080) + "/profile", String.class);

        Assertions.assertEquals(devResponse.getBody(), new DevProfile().getProfile());

        ResponseEntity<String> prodResponse = restTemplate.getForEntity("http://localhost:" +
                prodapp.getMappedPort(8081) + "/profile", String.class);

        Assertions.assertEquals(prodResponse.getBody(), new ProductionProfile().getProfile());

    }

}
