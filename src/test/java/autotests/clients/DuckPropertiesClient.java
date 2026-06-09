package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.MessageType;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckPropertiesClient extends DuckClient {
    @Step("Эндпоинт для получения свойств уточки")
    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/properties")
                        .queryParam("id", id)
        );
    }

    @Step("Валидация пустого ответа")
    public void validateEmptyJsonResponse(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body("{}")
        );
    }
}
