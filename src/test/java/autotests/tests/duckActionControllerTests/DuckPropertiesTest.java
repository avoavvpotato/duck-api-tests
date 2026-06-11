package autotests.tests.duckActionControllerTests;

import autotests.clients.DuckPropertiesClient;

import autotests.payloads.response.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;

@Epic("Тесты duck-action-controller")
@Feature("Свойства уточки")
@Story("Эндпоинт /api/duck/action/properties")
public class DuckPropertiesTest extends DuckPropertiesClient {
    DuckPropertiesResponse properties1 = new DuckPropertiesResponse()
            .color("yellow")
            .height(1.0)
            .material("rubber")
            .sound("quack")
            .wingsState("ACTIVE");

    DuckPropertiesResponse properties2 = new DuckPropertiesResponse()
            .color("red")
            .height(2.0)
            .material("rubber")
            .sound("qra")
            .wingsState("FIXED");

    DuckPropertiesResponse properties3 = new DuckPropertiesResponse()
            .color("yellow")
            .height(50.0)
            .material("rubber")
            .sound("xru")
            .wingsState("ACTIVE");

    @Test(dataProvider = "ducksProperties", description = "Параметризированная проверка свойств уточки")
    @CitrusTest
    @CitrusParameters({"duckId", "color", "height", "material", "sound", "wingsState", "expected", "isEmpty", "runner"})
    public void propertiesRubberDuck(String duckId,
                                     String color,
                                     String height,
                                     String material,
                                     String sound,
                                     String wingsState,
                                     DuckPropertiesResponse expected,
                                     Boolean isEmpty,
                                     @Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId", duckId);
        runner.$(doFinally().actions(context ->
                updateDatabase(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));

        updateDatabase(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state) " +
                        "values (${duckId}, '" + color + "', " + height + ", '" + material + "', '" + sound + "', '" + wingsState + "')");

        validateDataInDatabase(runner, "${duckId}",
                color, height, material, sound, wingsState);

        duckProperties(runner, "${duckId}");

        //PAYLOAD
        if (isEmpty) {
            validateEmptyJsonResponse(runner);
        } else {
            validateResponsePayloadMessage(runner, expected);
        }

        //validateResponseJsonPath(
        //runner,
        //jsonPath()
        //.expression("$.color", "yellow")
        // TODO: По документации ожидается height = "0.01".
        //.expression("$.height", "0.01")
        //.expression("$.height", "1.0")
        //.expression("$.material", "rubber")
        //.expression("$.sound", "quack")
        //.expression("$.wingsState", "ACTIVE")
        //);

        //RESOURCES
        //validateResponseResourceMessage(runner, "duckPropertiesTest/rubberPropertiesResponse.json");
    }

    @DataProvider(name = "ducksProperties")
    public Object[][] ducksPropertiesProvider() {
        return new Object[][]{
                // 1. rubber + нечётный ID
                {"100011", "yellow", "0.01", "rubber", "quack", "ACTIVE", properties1, false, null},
                // 2. wood + чётный ID - {}
                {"100012", "yellow", "0.01", "wood", "quack", "ACTIVE", null, true, null},
                // 3. rubber
                {"100013", "red", "0.02", "rubber", "qra", "FIXED", properties2, false, null},
                // 4. wood + чётный ID - {}
                {"100014", "green", "0.05", "wood", "quack", "UNDEFINED", null, true, null},
                // 5. rubber
                {"100015", "yellow", "0.5", "rubber", "xru", "ACTIVE", properties3, false, null},
        };
    }
}
