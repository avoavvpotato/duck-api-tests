package autotests.tests.duckControllerTests;

import autotests.clients.DuckUpdateClient;
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

import static com.consol.citrus.actions.CreateVariablesAction.Builder.createVariable;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;

@Epic("Тесты duck-controller")
@Feature("Изменение уточки")
@Story("Эндпоинт /api/duck/update")
public class DuckUpdateTest extends DuckUpdateClient {
    @Test(description = "Проверка изменения цвета и высоты уточки")
    @CitrusTest
    public void updateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(createVariable("duckId", "citrus:randomNumber(10)"));

        runner.$(doFinally().actions(context ->
                updateDatabase(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));


        updateDatabase(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state) " +
                        "values (${duckId}, 'yellow', 0.01, 'rubber', 'quack', 'ACTIVE')");

        updateDuck(runner, "${duckId}", "red", 0.02, "rubber", "quack", "ACTIVE");

        //PAYLOAD
        DuckMessageResponse expected = new DuckMessageResponse()
                .message("Duck with id = ${duckId} is updated");

        validateResponsePayloadMessage(runner, expected);

        //validateResponseJsonPath(
        //      runner,
        //    jsonPath().expression("$.message", "Duck with id = ${duckId} is updated")
        //);

        // RESOURCES
        //validateResponseResourceMessage(runner, "duckUpdateTest/updateResponse.json");

        validateDataInDatabase(runner, "${duckId}",
                "red", "0.02", "rubber", "quack", "ACTIVE");
    }

    @Test(description = "Проверка изменения цвета и звука уточки")
    @CitrusTest
    public void updateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(createVariable("duckId", "citrus:randomNumber(10)"));

        runner.$(doFinally().actions(context ->
                updateDatabase(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));

        updateDatabase(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state) " +
                        "values (${duckId}, 'yellow', 0.01, 'rubber', 'quack', 'ACTIVE')");

        updateDuck(runner, "${duckId}", "white", 0.01, "rubber", "xru-xru", "ACTIVE");

        DuckMessageResponse expected = new DuckMessageResponse()
                .message("Duck with id = ${duckId} is updated");

        validateResponsePayloadMessage(runner, expected);

        //validateResponseJsonPath(
        //      runner,
        //      jsonPath().expression("$.message", "Duck with id = ${duckId} is updated")
        //);

        // RESOURCES
        //validateResponseResourceMessage(runner, "duckUpdateTest/updateResponse.json");

        validateDataInDatabase(runner, "${duckId}",
                "white", "0.01", "rubber", "xru-xru", "ACTIVE");
    }
}
