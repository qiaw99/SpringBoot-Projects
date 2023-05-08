package com.udacity.pricing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void getPriceById(){
		ResponseEntity<String> response = this.restTemplate
				.getForEntity("http://localhost:8762/prices/1", String.class);

		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	}

	@Test
	public void getAllPrices(){
		ResponseEntity<List> response = this.restTemplate
				.getForEntity("http://localhost:8762/prices", List.class);

		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	}

}
