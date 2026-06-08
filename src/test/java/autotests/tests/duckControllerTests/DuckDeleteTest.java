package autotests.tests.duckControllerTests;

import autotests.clients.DuckDeleteClient;
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
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "ACTIVE");
        getDuckId(runner);

        deleteDuck(runner, "${duckId}");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "Duck is deleted")
        );
    }
}
