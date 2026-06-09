package autotests.tests.duckControllerTests;

import autotests.clients.DuckDeleteClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;

@Epic("Тесты duck-controller")
@Feature("Удаление уточки")
@Story("Эндпоинт /api/duck/delete")
public class DuckDeleteTest extends DuckDeleteClient {
    @Test(description = "Проверка что уточка удаляется")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId", "100003");

        updateDatabase(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state) " +
                        "values (${duckId}, 'yellow', 0.01, 'rubber', 'quack', 'ACTIVE')");

        validateDataInDatabase(runner, "${duckId}",
                "yellow", "0.01", "rubber", "quack", "ACTIVE");

        deleteDuck(runner, "${duckId}");

        //PAYLOAD
        //DuckMessageResponse expected = new DuckMessageResponse()
        //        .message("Duck is deleted");

        //validateResponsePayloadMessage(runner, expected);

        //validateResponseJsonPath(
        //      runner,
        //    jsonPath().expression("$.message", "Duck is deleted")
        //);

        // RESOURCES
        validateResponseResourceMessage(runner, "duckDeleteTest/deleteResponse.json");
    }
}
