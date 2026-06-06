package autotests.tests.duckControllerTests;

import autotests.clients.DuckDeleteClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;

public class DuckDeleteTest extends DuckDeleteClient {
    @Test(description = "Проверка что уточка удаляется")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties request = new DuckProperties()
                .color("yellow")
                .height(0.01)
                .material("rubber")
                .sound("quack")
                .wingsState("ACTIVE");

        createDuck(runner, request);
        getDuckId(runner);

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
