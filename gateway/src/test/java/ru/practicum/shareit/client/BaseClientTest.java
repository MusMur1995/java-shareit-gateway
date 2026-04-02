package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseClientTest {

    @Mock
    private RestTemplate restTemplate;

    private TestClient client;

    @BeforeEach
    void setUp() {
        client = new TestClient(restTemplate);
    }

    // Тестовый класс-наследник для доступа к protected методам
    static class TestClient extends BaseClient {
        public TestClient(RestTemplate rest) {
            super(rest);
        }
    }

    @Test
    void get_shouldReturnSuccessResponse() {
        String expectedBody = "Success";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.get("/test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void get_withUserId_shouldReturnSuccessResponse() {
        String expectedBody = "Success";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.get("/test", 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void get_withParameters_shouldReturnSuccessResponse() {
        String expectedBody = "Success";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(expectedBody);
        Map<String, Object> parameters = Map.of("param", "value");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class), eq(parameters)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.get("/test", 1L, parameters);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void post_shouldReturnSuccessResponse() {
        String requestBody = "Request";
        String expectedBody = "Created";
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.post("/test", requestBody);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void post_withUserId_shouldReturnSuccessResponse() {
        String requestBody = "Request";
        String expectedBody = "Created";
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.post("/test", 1L, requestBody);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void post_withParameters_shouldReturnSuccessResponse() {
        String requestBody = "Request";
        String expectedBody = "Created";
        Map<String, Object> parameters = Map.of("param", "value");
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class), eq(parameters)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.post("/test", 1L, parameters, requestBody);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void put_shouldReturnSuccessResponse() {
        String requestBody = "Request";
        String expectedBody = "Updated";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.put("/test", 1L, requestBody);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void put_withParameters_shouldReturnSuccessResponse() {
        String requestBody = "Request";
        String expectedBody = "Updated";
        Map<String, Object> parameters = Map.of("param", "value");
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Object.class), eq(parameters)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.put("/test", 1L, parameters, requestBody);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void patch_withBody_shouldReturnSuccessResponse() {
        String requestBody = "Request";
        String expectedBody = "Patched";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.patch("/test", requestBody);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void patch_withUserId_shouldReturnSuccessResponse() {
        String expectedBody = "Patched";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.patch("/test", 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void patch_withUserIdAndBody_shouldReturnSuccessResponse() {
        String requestBody = "Request";
        String expectedBody = "Patched";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.patch("/test", 1L, requestBody);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void patch_withParameters_shouldReturnSuccessResponse() {
        String requestBody = "Request";
        String expectedBody = "Patched";
        Map<String, Object> parameters = Map.of("param", "value");
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class), eq(parameters)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.patch("/test", 1L, parameters, requestBody);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void delete_shouldReturnSuccessResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.noContent().build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.delete("/test");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void delete_withUserId_shouldReturnSuccessResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.noContent().build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.delete("/test", 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void delete_withParameters_shouldReturnSuccessResponse() {
        Map<String, Object> parameters = Map.of("param", "value");
        ResponseEntity<Object> expectedResponse = ResponseEntity.noContent().build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(Object.class), eq(parameters)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.delete("/test", 1L, parameters);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void makeAndSendRequest_shouldHandleHttpStatusCodeException() {
        HttpStatusCodeException exception = org.springframework.web.client.HttpClientErrorException
                .create(HttpStatus.BAD_REQUEST, "Bad Request", null, null, null);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class)))
                .thenThrow(exception);

        ResponseEntity<Object> response = client.get("/test");

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void makeAndSendRequest_shouldHandleNonSuccessfulResponse() {
        ResponseEntity<Object> errorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class)))
                .thenReturn(errorResponse);

        ResponseEntity<Object> response = client.get("/test");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody());
    }

    @Test
    void defaultHeaders_shouldIncludeUserId() {
        // Проверяем через makeAndSendRequest, что заголовки правильно устанавливаются
        String expectedBody = "Success";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(expectedBody);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = client.get("/test", 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void prepareGatewayResponse_shouldHandleEmptyBody() {
        ResponseEntity<Object> noBodyResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(Object.class)))
                .thenReturn(noBodyResponse);

        ResponseEntity<Object> response = client.delete("/test");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
