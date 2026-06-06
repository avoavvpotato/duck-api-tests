package autotests.tests.duckActionControllerTests;

import autotests.clients.DuckQuackClient;
import autotests.payloads.response.DuckSoundResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;

public class DuckQuackTest extends DuckQuackClient {

    // INSERT INTO duck (id, color, material, height, sound, wings_state)
    // VALUES
    //     (10003, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
    //     (10004, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE');

    @Test(description = "Проверка кряканья уточки с корректным нечётным id")
    @CitrusTest
    public void quackWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        duckQuack(runner, "10003", "3", "2");

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
        duckQuack(runner, "10004", "3", "2");

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
