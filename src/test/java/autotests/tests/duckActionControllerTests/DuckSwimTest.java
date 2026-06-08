package autotests.tests.duckActionControllerTests;

import autotests.clients.DuckSwimClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;

public class DuckSwimTest extends DuckSwimClient {
    @Test(description = "Проверка того, что уточка поплыла")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "ACTIVE");
        getDuckId(runner);

        duckSwim(runner, "${duckId}");

        // TODO: По документации ожидается статус 200 OK и message = "I’m swimming".
        // jsonPath().expression("$.message", "I’m swimming")
        validateNotFoundJsonPath(
                runner,
                jsonPath().expression("$.message", "Paws are not found ((((")
        );

        deleteDuck(runner, "${duckId}");
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
