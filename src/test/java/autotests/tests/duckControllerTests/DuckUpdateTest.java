package autotests.tests.duckControllerTests;

import autotests.clients.DuckUpdateClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;

public class DuckUpdateTest extends DuckUpdateClient {
    @Test(description = "Проверка изменения цвета и высоты уточки")
    @CitrusTest
    public void updateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties request = new DuckProperties()
                .color("yellow")
                .height(0.01)
                .material("rubber")
                .sound("quack")
                .wingsState("ACTIVE");

        createDuck(runner, request);
        getDuckId(runner);

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

        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка изменения цвета и звука уточки")
    @CitrusTest
    public void updateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties request = new DuckProperties()
                .color("yellow")
                .height(0.01)
                .material("rubber")
                .sound("quack")
                .wingsState("ACTIVE");

        createDuck(runner, request);
        getDuckId(runner);

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

        deleteDuck(runner, "${duckId}");
    }
}
