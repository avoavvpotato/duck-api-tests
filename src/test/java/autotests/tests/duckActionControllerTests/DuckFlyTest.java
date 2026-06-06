package autotests.tests.duckActionControllerTests;

import autotests.clients.DuckFlyClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;

public class DuckFlyTest extends DuckFlyClient {

    @Test(description = "Проверка того что уточка полетела если крылья ACTIVE")
    @CitrusTest
    public void successfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties request = new DuckProperties()
                .color("yellow")
                .height(0.01)
                .material("rubber")
                .sound("quack")
                .wingsState("ACTIVE");

        createDuck(runner, request);
        getDuckId(runner);

        duckFly(runner, "${duckId}");

        //PAYLOAD
        DuckMessageResponse expected = new DuckMessageResponse()
                .message("I am flying :)");

        validateResponsePayloadMessage(runner, expected);

        //validateResponseJsonPath(
        //runner,
        // TODO: По документации ожидается "I’m flying".
        // jsonPath().expression("$.message", "I’m flying")
        //jsonPath().expression("$.message", "I am flying :)")
        //);

        // RESOURCES
        // validateResponseResourceMessage(runner, "duckFlyTest/flyActiveResponse.json");

        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того что уточка не полетела если крылья FIXED")
    @CitrusTest
    public void unsuccessfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties request = new DuckProperties()
                .color("yellow")
                .height(0.01)
                .material("rubber")
                .sound("quack")
                .wingsState("FIXED");

        createDuck(runner, request);
        getDuckId(runner);

        //PAYLOAD
        duckFly(runner, "${duckId}");

        DuckMessageResponse expected = new DuckMessageResponse()
                .message("I can not fly :C");

        // validateResponseJsonPath(
        //runner,
        // TODO: По документации ожидается "I can’t fly".
        // jsonPath().expression("$.message", "I can’t fly")
        //jsonPath().expression("$.message", "I can not fly :C")
        //);

        // RESOURCES
        // validateResponseResourceMessage(runner, "duckFlyTest/flyFixedResponse.json");

        validateResponsePayloadMessage(runner, expected);

        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того что уточка не полетела если крылья UNDEFINED")
    @CitrusTest
    public void undefinedFly(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties request = new DuckProperties()
                .color("yellow")
                .height(0.01)
                .material("rubber")
                .sound("quack")
                .wingsState("UNDEFINED");

        createDuck(runner, request);
        getDuckId(runner);

        //PAYLOAD
        duckFly(runner, "${duckId}");

        DuckMessageResponse expected = new DuckMessageResponse()
                .message("Wings are not detected :(");

        //validateResponseJsonPath(
        //runner,
        // TODO: По документации ожидается UNDEFINED.
        //jsonPath().expression("$.message", "UNDEFINED")
        //jsonPath().expression("$.message", "Wings are not detected :(")
        //);

        // RESOURCES
        // validateResponseResourceMessage(runner, "duckFlyTest/flyUndefinedResponse.json");

        validateResponsePayloadMessage(runner, expected);

        deleteDuck(runner, "${duckId}");
    }
}
