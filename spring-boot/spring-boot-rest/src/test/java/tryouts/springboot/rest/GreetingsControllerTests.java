package tryouts.springboot.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest
public class GreetingsControllerTests {

    @Test
    public void testDefaultGreeting() {
        RestTemplate template = new TestRestTemplate();
        ResponseEntity<String> result = template.getForEntity("http://localhost:8080/greeting", String.class);
        assertThat(result.getBody(), containsString("World"));
    }

    @Test
    public void testNamedGreeting() {
        RestTemplate template = new TestRestTemplate();
        ResponseEntity<String> result = template.getForEntity("http://localhost:8080/greeting?name=Yavuz", String.class);
        assertThat(result.getBody(), containsString("Yavuz"));
    }

}
