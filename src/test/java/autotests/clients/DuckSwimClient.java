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
        sendGetRequestWithQueryParameter(runner, duckService, "/api/duck/action/swim", "id", id);
    }

    @Step("Эндпоинт для получения всех id уточек")
    public void getAllIds(TestCaseRunner runner) {
        sendGetRequest(runner, "/api/duck/getAllIds", duckService);
    }

    @Step("Валидация ответа 404 из Resources")
    public void validateNotFoundResource(TestCaseRunner runner, String resourcePath) {
        validateNotFoundResource(runner, duckService, resourcePath);
    }

    @Step("Валидация ответа 404 через Payload")
    public void validateNotFoundPayloadMessage(TestCaseRunner runner, Object expectedPayload) {
        validateNotFoundPayloadMessage(runner, duckService, expectedPayload);
    }

    @Step("Валидация ответа 404 через JsonPath")
    public void validateNotFoundJsonPath(TestCaseRunner runner,
                                         JsonPathSupport body) {
        validateNotFoundJsonPath(runner, duckService, body);
    }

    @Step("Валидация ответа 404")
    public void validateNotFound(TestCaseRunner runner) {
        validateNotFound(runner, duckService);
    }
}
