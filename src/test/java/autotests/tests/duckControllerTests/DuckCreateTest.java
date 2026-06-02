package autotests.tests.duckControllerTests;

import autotests.clients.DuckCreateClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckCreateTest extends DuckCreateClient {
    @Test(description = "Проверка создания уточки с material = rubber")
    @CitrusTest
    public void createRubberMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "ACTIVE");

        validateResponse(runner, "yellow", 0.01, "rubber", "quack", "ACTIVE");
        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка создания уточки с material = wood")
    @CitrusTest
    public void createWoodMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.01, "wood", "quack", "ACTIVE");

        validateResponse(runner, "yellow", 0.01, "wood", "quack", "ACTIVE");
        deleteDuck(runner, "${duckId}");
    }
}
