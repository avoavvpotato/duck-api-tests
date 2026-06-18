package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;
import org.springframework.context.annotation.Description;

public class DuckCreateClient extends DuckClient {
    @Step("Эндпоинт для внесения уточки в базу")
    public void createDuck(TestCaseRunner runner, Object userData) {
        sendPostRequestWithJsonBody(runner, "/api/duck/create", userData, duckService);
    }
}
