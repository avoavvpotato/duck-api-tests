package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.MessageType;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckPropertiesClient extends DuckClient {
    @Step("Эндпоинт для получения свойств уточки")
    public void duckProperties(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/properties", "id", id);
    }

    @Step("Валидация пустого ответа")
    public void validateEmptyJsonResponse(TestCaseRunner runner) {
        validateEmptyJsonResponse(runner, duckService);
    }
}
