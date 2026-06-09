package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.dsl.JsonPathSupport;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckSwimClient extends DuckClient {
    @Step("Эндпоинт для плавания уточки")
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id));
    }

    @Step("Эндпоинт для получения всех id уточек")
    public void getAllIds(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/getAllIds")
        );
    }

    @Step("Валидация ответа 404 из Resources")
    @Description("Валидация ответа 404 из Resources")
    public void validateNotFoundResource(TestCaseRunner runner, String resourcePath) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
                        .message()
                        .type(MessageType.JSON)
                        .body(new ClassPathResource(resourcePath))
        );
    }

    @Step("Валидация ответа 404 через Payload")
    @Description("Валидация ответа 404 с Payload")
    public void validateNotFoundPayloadMessage(TestCaseRunner runner, Object expectedPayload) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
                        .message()
                        .type(MessageType.JSON)
                        .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper()))
        );
    }

    @Step("Валидация ответа 404 через JsonPath")
    public void validateNotFoundJsonPath(TestCaseRunner runner,
                                         JsonPathSupport body) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
                        .message()
                        .type(MessageType.JSON)
                        .validate(body)
        );
    }

    @Step("Валидация ответа 404")
    public void validateNotFound(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
        );
    }
}
