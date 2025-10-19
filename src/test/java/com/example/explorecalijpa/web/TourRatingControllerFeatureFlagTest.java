package com.example.explorecalijpa.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.explorecalijpa.business.TourRatingService;

/**
 * Tests that verify the tour rating API can be disabled via feature flags.
 * When features.tour-ratings=false, ALL rating endpoints should return 404 Not
 * Found.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "features.tour-ratings=false")
public class TourRatingControllerFeatureFlagTest {

  private static final int TOUR_ID = 999;
  private static final int CUSTOMER_ID = 1000;
  private static final String TOUR_RATINGS_URL = "/tours/" + TOUR_ID + "/ratings";
  private static final String TOUR_RATINGS_AVERAGE_URL = "/tours/" + TOUR_ID + "/ratings/average";
  private static final String TOUR_RATINGS_DELETE_URL = "/tours/" + TOUR_ID + "/ratings/" + CUSTOMER_ID;
  private static final String TOUR_RATINGS_BATCH_URL = "/tours/" + TOUR_ID + "/ratings/batch?score=5";

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private TourRatingService serviceMock;

  private HttpEntity<String> createJsonRequest(String jsonBody) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(jsonBody, headers);
  }

  @Test
  void getAllRatingsDisabledReturnsNotFound() {
    ResponseEntity<String> res = restTemplate.withBasicAuth("user", "password")
        .getForEntity(TOUR_RATINGS_URL, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void createRatingDisabledReturnsNotFound() {
    String ratingJson = "{\"customerId\":1000,\"score\":5,\"comment\":\"Great tour!\"}";
    HttpEntity<String> request = createJsonRequest(ratingJson);

    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .postForEntity(TOUR_RATINGS_URL, request, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void getAverageDisabledReturnsNotFound() {
    ResponseEntity<String> res = restTemplate.withBasicAuth("user", "password")
        .getForEntity(TOUR_RATINGS_AVERAGE_URL, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void updateWithPutDisabledReturnsNotFound() {
    String ratingJson = "{\"customerId\":1000,\"score\":4,\"comment\":\"Updated comment\"}";
    HttpEntity<String> request = createJsonRequest(ratingJson);

    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .exchange(TOUR_RATINGS_URL, HttpMethod.PUT, request, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void updateWithPatchDisabledReturnsNotFound() {
    String ratingJson = "{\"customerId\":1000,\"score\":3}";
    HttpEntity<String> request = createJsonRequest(ratingJson);

    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .exchange(TOUR_RATINGS_URL, HttpMethod.PATCH, request, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void deleteRatingDisabledReturnsNotFound() {
    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .exchange(TOUR_RATINGS_DELETE_URL, HttpMethod.DELETE, null, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void createManyRatingsDisabledReturnsNotFound() {
    String customersJson = "[1000, 1001, 1002]";
    HttpEntity<String> request = createJsonRequest(customersJson);

    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .postForEntity(TOUR_RATINGS_BATCH_URL, request, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  // Additional comprehensive tests per method for TDD approach

  @Test
  void getRatingsWithUserAuthDisabledReturnsNotFound() {
    ResponseEntity<String> res = restTemplate.withBasicAuth("user", "password")
        .getForEntity(TOUR_RATINGS_URL, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void getRatingsWithAdminAuthDisabledReturnsNotFound() {
    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .getForEntity(TOUR_RATINGS_URL, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void createRatingWithMinimalPayloadDisabledReturnsNotFound() {
    String minimalRatingJson = "{\"customerId\":1000,\"score\":5}";
    HttpEntity<String> request = createJsonRequest(minimalRatingJson);

    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .postForEntity(TOUR_RATINGS_URL, request, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void getAverageWithUserAuthDisabledReturnsNotFound() {
    ResponseEntity<String> res = restTemplate.withBasicAuth("user", "password")
        .getForEntity(TOUR_RATINGS_AVERAGE_URL, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void getAverageWithAdminAuthDisabledReturnsNotFound() {
    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .getForEntity(TOUR_RATINGS_AVERAGE_URL, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void updateWithPutMinimalPayloadDisabledReturnsNotFound() {
    String minimalRatingJson = "{\"customerId\":1000,\"score\":4}";
    HttpEntity<String> request = createJsonRequest(minimalRatingJson);

    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .exchange(TOUR_RATINGS_URL, HttpMethod.PUT, request, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void updateWithPatchMinimalPayloadDisabledReturnsNotFound() {
    String minimalRatingJson = "{\"customerId\":1000}";
    HttpEntity<String> request = createJsonRequest(minimalRatingJson);

    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .exchange(TOUR_RATINGS_URL, HttpMethod.PATCH, request, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void deleteRatingWithAdminAuthDisabledReturnsNotFound() {
    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .exchange(TOUR_RATINGS_DELETE_URL, HttpMethod.DELETE, null, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  void createManyRatingsWithMinimalPayloadDisabledReturnsNotFound() {
    String minimalCustomersJson = "[1000]";
    HttpEntity<String> request = createJsonRequest(minimalCustomersJson);

    ResponseEntity<String> res = restTemplate.withBasicAuth("admin", "admin123")
        .postForEntity(TOUR_RATINGS_BATCH_URL, request, String.class);
    assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}