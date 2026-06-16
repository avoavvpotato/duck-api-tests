package autotests.tests.duckActionControllerTests;

import autotests.clients.DuckQuackClient;
import autotests.payloads.response.DuckSoundResponse;
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
@Feature("Кряканье уточки")
@Story("Эндпоинт /api/duck/action/quack")
public class DuckQuackTest extends DuckQuackClient {

    // INSERT INTO duck (id, color, material, height, sound, wings_state)
    // VALUES
    //     (10003, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
    //     (10004, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE');

    @Test(description = "Проверка кряканья уточки с корректным нечётным id")
    @CitrusTest
    public void quackWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId", "100009");
        runner.$(doFinally().actions(context ->
                updateDatabase(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));

        updateDatabase(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state) " +
                        "values (${duckId}, 'yellow', 0.01, 'rubber', 'quack', 'ACTIVE')");

        duckQuack(runner, "${duckId}", "3", "2");

        //PAYLOAD
        DuckSoundResponse expected = new DuckSoundResponse()
                .sound("quack-quack-quack, quack-quack-quack");

        validateResponsePayloadMessage(runner, expected);

        //validateResponseJsonPath(
        //      runner,
        // TODO: По документации ожидается "quack-quack, quack-quack, quack-quack". rep - кол повторений quack sound
        // jsonPath().expression("$.sound", "quack-quack, quack-quack, quack-quack")
        //      jsonPath().expression("$.sound", "quack-quack-quack, quack-quack-quack")
        //);

        // RESOURCES
        //validateResponseResourceMessage(runner, "duckQuackTest/quackOddResponse.json");
    }

    @Test(description = "Проверка кряканья уточки с корректным чётным id")
    @CitrusTest
    public void quackWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId", "100010");
        runner.$(doFinally().actions(context ->
                updateDatabase(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));

        updateDatabase(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state) " +
                        "values (${duckId}, 'yellow', 0.01, 'rubber', 'quack', 'ACTIVE')");

        duckQuack(runner, "${duckId}", "3", "2");

        //PAYLOAD
        DuckSoundResponse expected = new DuckSoundResponse()
                .sound("moo-moo-moo, moo-moo-moo");

        validateResponsePayloadMessage(runner, expected);

        //validateResponseJsonPath(
        //      runner,
        // TODO: По документации ожидается "quack-quack, quack-quack, quack-quack".
        // jsonPath().expression("$.sound", "quack-quack, quack-quack, quack-quack")
        //    jsonPath().expression("$.sound", "moo-moo-moo, moo-moo-moo")
        //);

        //RESOURCES
        //validateResponseResourceMessage(runner, "duckQuackTest/quackEvenResponse.json");
    }
}
