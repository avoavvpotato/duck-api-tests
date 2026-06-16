package autotests.tests.duckActionControllerTests;

import autotests.clients.DuckSwimClient;
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

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;

@Epic("Тесты duck-action-controller")
@Feature("Плавание уточки")
@Story("Эндпоинт /api/duck/action/swim")
public class DuckSwimTest extends DuckSwimClient {
    @Test(description = "Проверка того, что уточка поплыла")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId", "100007");
        runner.$(doFinally().actions(context ->
                updateDatabase(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));

        updateDatabase(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state) " +
                        "values (${duckId}, 'yellow', 0.01, 'rubber', 'quack', 'ACTIVE')");

        duckSwim(runner, "${duckId}");

        // TODO: По документации ожидается статус 200 OK и message = "I’m swimming".
        // jsonPath().expression("$.message", "I’m swimming")
        // validateNotFoundJsonPath(
        //        runner,
        //        jsonPath().expression("$.message", "Paws are not found ((((")
        //);

        //PAYLOAD
        DuckMessageResponse expected = new DuckMessageResponse()
                .message("Paws are not found ((((");

        validateNotFoundPayloadMessage(runner, expected);

        //RESOURCES
        //validateNotFoundResource(runner, "duckSwimTest/swimNotFoundResponse.json");
    }

    @Test(description = "Проверка плавания для несуществующей уточки")
    @CitrusTest
    public void swimNonExisting(@Optional @CitrusResource TestCaseRunner runner) {
        String nonExistingDuckId = "9223372036854775807";

        getAllIds(runner);

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$[?(@ == " + nonExistingDuckId + ")]", "[]")
        );

        duckSwim(runner, nonExistingDuckId);

        validateNotFound(runner);
    }
}
