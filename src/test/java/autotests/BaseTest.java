package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.dsl.JsonPathSupport;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDb;

    @Step("Выполнение SQL запроса в базе")
    protected void updateDatabase(TestCaseRunner runner, String query) {
        runner.$(sql(testDb).statement(query));
    }

    @Step("GET запрос")
    protected void sendGetRequest(TestCaseRunner runner, String path, HttpClient httpClient) {
        runner.$(http()
                .client(httpClient)
                .send()
                .get(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Step("GET запрос с query параметром")
    protected void sendGetRequestWithQueryParameter(TestCaseRunner runner, HttpClient httpClient,
                                  String path, String queryName, String queryValue) {
        runner.$(http()
                .client(httpClient)
                .send()
                .get(path)
                .queryParam(queryName, queryValue));
    }

    @Step("POST запрос с JSON телом")
    protected void sendPostRequestWithJsonBody(TestCaseRunner runner, String path, Object body, HttpClient httpClient) {
        runner.$(http()
                .client(httpClient)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    @Step("DELETE запрос с query параметром")
    protected void sendDeleteRequestWithQueryParameter(TestCaseRunner runner, HttpClient httpClient,
                                     String path, String queryName, String queryValue) {
        runner.$(http()
                .client(httpClient)
                .send()
                .delete(path)
                .queryParam(queryName, queryValue));
    }

    @Step("Валидация ответа через Payload")
    protected void validateResponsePayloadMessage(TestCaseRunner runner, HttpClient httpClient,
                                                  Object expectedPayload) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    @Step("Валидация ответа через JsonPath")
    protected void validateResponseJsonPath(TestCaseRunner runner, HttpClient httpClient,
                                            JsonPathSupport body) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .validate(body));
    }

    @Step("Валидация ответа из Resources")
    protected void validateResponseResource(TestCaseRunner runner, HttpClient httpClient,
                                            String resourcePath) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new ClassPathResource(resourcePath))
                .extract(fromBody().expression("$.id", "duckId")));
    }

    @Step("Валидация ответа из Resources без id")
    protected void validateResponseResourceMessage(TestCaseRunner runner, HttpClient httpClient,
                                                   String resourcePath) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new ClassPathResource(resourcePath)));
    }

    @Step("Валидация ответа строкой")
    protected void validateResponseString(TestCaseRunner runner, HttpClient httpClient,
                                          String expectedBody) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(expectedBody));
    }

    @Step("Валидация ответа через Payload с сохранением id")
    protected void validateResponsePayloadWithId(TestCaseRunner runner, HttpClient httpClient,
                                                 Object expectedPayload, String variableName) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper()))
                .extract(fromBody().expression("$.id", variableName)));
    }

    @Step("Валидация ответа строкой с сохранением id")
    protected void validateResponseStringWithId(TestCaseRunner runner, HttpClient httpClient,
                                                String expectedBody, String variableName) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(expectedBody)
                .extract(fromBody().expression("$.id", variableName)));
    }

    @Step("Валидация пустого JSON-ответа")
    protected void validateEmptyJsonResponse(TestCaseRunner runner, HttpClient httpClient) {
        validateResponseString(runner, httpClient, "{}");
    }

    @Step("Валидация ответа 404 через Payload")
    protected void validateNotFoundPayloadMessage(TestCaseRunner runner, HttpClient httpClient,
                                                  Object expectedPayload) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.NOT_FOUND)
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    @Step("Валидация ответа 404 через JsonPath")
    protected void validateNotFoundJsonPath(TestCaseRunner runner, HttpClient httpClient,
                                            JsonPathSupport body) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.NOT_FOUND)
                .message()
                .type(MessageType.JSON)
                .validate(body));
    }

    @Step("Валидация ответа 404 через Resource")
    protected void validateNotFoundResource(TestCaseRunner runner, HttpClient httpClient,
                                            String resourcePath) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.NOT_FOUND)
                .message()
                .type(MessageType.JSON)
                .body(new ClassPathResource(resourcePath)));
    }

    @Step("Валидация ответа 404")
    protected void validateNotFound(TestCaseRunner runner, HttpClient httpClient) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.NOT_FOUND));
    }

    @Step("Получение id из ответа")
    protected void extractIdFromResponse(TestCaseRunner runner, HttpClient httpClient,
                                         String jsonPath, String variableName) {
        runner.$(http()
                .client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression(jsonPath, variableName)));
    }

    @Step("PUT запрос с query параметрами")
    protected void sendPutRequestWithQueryParameters(TestCaseRunner runner, HttpClient httpClient, String path,
                                  String... queryParams) {
        var request = http()
                .client(httpClient)
                .send()
                .put(path);
        for (int i = 0; i < queryParams.length; i += 2) {
            request = request.queryParam(queryParams[i], queryParams[i + 1]);
        }
        runner.$(request);
    }
}
