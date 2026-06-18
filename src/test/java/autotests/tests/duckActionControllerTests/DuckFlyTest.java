package autotests.tests.duckActionControllerTests;

import autotests.clients.DuckFlyClient;
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

@Epic("Тесты duck-action-controller")
@Feature("Полёт уточки")
@Story("Эндпоинт /api/duck/action/fly")
public class DuckFlyTest extends DuckFlyClient {

    @Test(description = "Проверка того что уточка полетела если крылья ACTIVE")
    @CitrusTest
    public void successfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(createVariable("duckId", "citrus:randomNumber(10)"));

        runner.$(doFinally().actions(context ->
                updateDatabase(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));

        updateDatabase(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state) " +
                        "values (${duckId}, 'yellow', 0.01, 'rubber', 'quack', 'ACTIVE')");

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
    }

    @Test(description = "Проверка того что уточка не полетела если крылья FIXED")
    @CitrusTest
    public void unsuccessfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(createVariable("duckId", "citrus:randomNumber(10)"));

        runner.$(doFinally().actions(context ->
                updateDatabase(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));

        updateDatabase(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state) " +
                        "values (${duckId}, 'yellow', 0.01, 'rubber', 'quack', 'FIXED')");

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
    }

    @Test(description = "Проверка того что уточка не полетела если крылья UNDEFINED")
    @CitrusTest
    public void undefinedFly(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(createVariable("duckId", "citrus:randomNumber(10)"));

        runner.$(doFinally().actions(context ->
                updateDatabase(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));

        updateDatabase(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state) " +
                        "values (${duckId}, 'yellow', 0.01, 'rubber', 'quack', 'UNDEFINED')");

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
    }
}
