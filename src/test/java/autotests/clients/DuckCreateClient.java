package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;
import org.springframework.context.annotation.Description;

public class DuckCreateClient extends DuckClient {
    //Payload
    @Step("Валидация ответа create через Payload")
    public void validateResponsePayload(TestCaseRunner runner, Object expectedPayload) {
        validateResponsePayloadWithId(runner, duckService, expectedPayload, "duckId");
    }

    //String
    @Step("Валидация ответа create через строку")
    public void validateResponseString(TestCaseRunner runner, String expectedBody) {
        validateResponseStringWithId(runner, duckService, expectedBody, "duckId");
    }
}
