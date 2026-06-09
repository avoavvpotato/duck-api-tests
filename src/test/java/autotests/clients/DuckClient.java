package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.dsl.JsonPathSupport;
import com.consol.citrus.message.MessageType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDb;

    // Методы для работы с БД
    @Step("Выполнение SQL запроса в базе")
    public void updateDatabase(TestCaseRunner runner, String query) {
        runner.$(sql(testDb).statement(query));
    }

    @Step("Проверка данных уточки в базе")
    public void validateDataInDatabase(TestCaseRunner runner, String id,
                                       String color, String height,
                                       String material, String sound, String wingsState) {
        String sqlQuery = "select COLOR, HEIGHT, MATERIAL, SOUND, WINGS_STATE from DUCK where ID = " + id;
        runner.$(query(testDb)
                .statement(sqlQuery)
                .validate("COLOR", color)
                .validate("HEIGHT", height)
                .validate("MATERIAL", material)
                .validate("SOUND", sound)
                .validate("WINGS_STATE", wingsState));
    }

    @Step("Эндпоинт для удаления уточки из базы")
    public void deleteDuck(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", id)
        );
    }

    @Step("Получение id уточки из ответа")
    public void getDuckId(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
    }

    @Step("Валидация ответа через JsonPath")
    public void validateResponseJsonPath(TestCaseRunner runner,
                                         JsonPathSupport body) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .validate(body)
        );
    }

    // Payload
    @Step("Валидация ответа через Payload")
    public void validateResponsePayloadMessage(TestCaseRunner runner, Object expectedPayload) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper()))
        );
    }

    @Step("Эндпоинт для внесения уточки в базу")
    public void createDuck(TestCaseRunner runner, Object userData) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .body(new ObjectMappingPayloadBuilder(userData, new ObjectMapper()))
        );
    }

    // Resources
    @Step("Валидация ответа из Resources")
    public void validateResponseResource(TestCaseRunner runner, String resourcePath) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(new ClassPathResource(resourcePath))
                        .extract(fromBody().expression("$.id", "duckId"))
        );
    }

    @Step("Валидация ответа из Resources без id")
    public void validateResponseResourceMessage(TestCaseRunner runner, String resourcePath) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(new ClassPathResource(resourcePath))
        );
    }
}
