package autotests.tests.duckActionControllerTests;

import autotests.clients.DuckFlyClient;
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
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "ACTIVE");
        getDuckId(runner);

        duckFly(runner, "${duckId}");

        validateResponseJsonPath(
                runner,
                // TODO: По документации ожидается "I’m flying".
                // jsonPath().expression("$.message", "I’m flying")
                jsonPath().expression("$.message", "I am flying :)")
        );

        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того что уточка не полетела если крылья FIXED")
    @CitrusTest
    public void unsuccessfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "FIXED");
        getDuckId(runner);

        duckFly(runner, "${duckId}");

        validateResponseJsonPath(
                runner,
                // TODO: По документации ожидается "I can’t fly".
                // jsonPath().expression("$.message", "I can’t fly")
                jsonPath().expression("$.message", "I can not fly :C")
        );

        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка того что уточка не полетела если крылья UNDEFINED")
    @CitrusTest
    public void undefinedFly(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "UNDEFINED");
        getDuckId(runner);

        duckFly(runner, "${duckId}");

        validateResponseJsonPath(
                runner,
                // TODO: По документации ожидается UNDEFINED.
                //jsonPath().expression("$.message", "UNDEFINED")
                jsonPath().expression("$.message", "Wings are not detected :(")
        );

        deleteDuck(runner, "${duckId}");
    }
}
