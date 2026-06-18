package autotests.clients;

import autotests.BaseTest;
import io.qameta.allure.Step;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.dsl.JsonPathSupport;

import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

public class DuckClient extends BaseTest {
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

    @Step("Получение id уточки из ответа")
    public void getDuckId(TestCaseRunner runner) {
        extractIdFromResponse(runner, duckService, "$.id", "duckId");
    }

    @Step("Валидация ответа через Payload")
    public void validateResponsePayloadMessage(TestCaseRunner runner, Object expectedPayload) {
        validateResponsePayloadMessage(runner, duckService, expectedPayload);
    }

    @Step("Валидация ответа через JsonPath")
    public void validateResponseJsonPath(TestCaseRunner runner, JsonPathSupport body) {
        validateResponseJsonPath(runner, duckService, body);
    }

    @Step("Валидация ответа из Resources")
    public void validateResponseResource(TestCaseRunner runner, String resourcePath) {
        validateResponseResource(runner, duckService, resourcePath);
    }

    @Step("Валидация ответа из Resources без id")
    public void validateResponseResourceMessage(TestCaseRunner runner, String resourcePath) {
        validateResponseResourceMessage(runner, duckService, resourcePath);
    }

    @Step("Валидация ответа create через Payload")
    public void validateResponsePayload(TestCaseRunner runner, Object expectedPayload) {
        validateResponsePayloadWithId(runner, duckService, expectedPayload, "duckId");
    }
    @Step("Валидация ответа create через строку")
    public void validateResponseString(TestCaseRunner runner, String expectedBody) {
        validateResponseStringWithId(runner, duckService, expectedBody, "duckId");
    }
}
