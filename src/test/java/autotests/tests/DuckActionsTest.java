package autotests.tests;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;

public class DuckActionsTest extends DuckActionsClient {
    /*
    INSERT INTO duck (id, color, material, height, sound, wings_state)
    VALUES
        (10001, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
        (10002, 'yellow', 'wood', 0.01, 'quack', 'ACTIVE'),
        (10003, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
        (10004, 'yellow', 'rubber', 0.01, 'quack', 'FIXED'),
        (10005, 'yellow', 'rubber', 0.01, 'quack', 'UNDEFINED'),
        (10006, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
        (10007, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
        (10008, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
        (10009, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
        (10010, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
        (10011, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE');
    */

    // GET /api/duck/action/properties
    @Test(description = "Проверка получения свойств уточки с material = rubber и нечетным ID")
    @CitrusTest
    public void propertiesRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        duckProperties(runner, "10001");

        validateResponseJsonPath(
                runner,
                jsonPath()
                        .expression("$.color", "yellow")
                        .expression("$.height", "0.01")
                        .expression("$.material", "rubber")
                        .expression("$.sound", "quack")
                        .expression("$.wingsState", "ACTIVE")
        );
    }

    @Test(description = "Проверка получения свойств уточки с material = wood и четным ID")
    @CitrusTest
    public void propertiesWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        duckProperties(runner, "10002");

        validateResponseJsonPath(
                runner,
                jsonPath()
                        .expression("$.color", "yellow")
                        .expression("$.height", "0.01")
                        .expression("$.material", "wood")
                        .expression("$.sound", "quack")
                        .expression("$.wingsState", "ACTIVE")
        );
    }

    // GET /api/duck/action/fly
    @Test(description = "Проверка того что уточка полетела если крылья ACTIVE")
    @CitrusTest
    public void successfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        duckFly(runner, "10003");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "I’m flying")
        );
    }

    @Test(description = "Проверка того что уточка не полетела если крылья FIXED")
    @CitrusTest
    public void unsuccessfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        duckFly(runner, "10004");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "I can’t fly")
        );
    }

    @Test(description = "Проверка того что уточка не полетела если крылья UNDEFINED")
    @CitrusTest
    public void undefinedFly(@Optional @CitrusResource TestCaseRunner runner) {
        duckFly(runner, "10005");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "Undefined")
        );
    }

    // GET /api/duck/action/swim
    @Test(description = "Проверка того, что уточка поплыла")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        duckSwim(runner, "10006");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "I’m swimming")
        );
    }

    @Test(description = "Проверка плавания для несуществующей уточки")
    @CitrusTest
    public void swimNonExisting(@Optional @CitrusResource TestCaseRunner runner) {
        duckSwim(runner, "99999");

        validateResponseStatus(runner, HttpStatus.NOT_FOUND);
    }

    // GET /api/duck/action/quack
    @Test(description = "Проверка кряканья уточки с корректным нечётным id")
    @CitrusTest
    public void quackWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        duckQuack(runner, "10007", "3", "2");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.sound", "quack-quack, quack-quack, quack-quack")
        );
    }

    @Test(description = "Проверка кряканья уточки с корректным чётным id")
    @CitrusTest
    public void quackWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        duckQuack(runner, "10008", "3", "2");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.sound", "quack-quack, quack-quack, quack-quack")
        );
    }

    // POST /api/duck/create
    @Test(description = "Проверка создания уточки с material = rubber")
    @CitrusTest
    public void createRubberMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "ACTIVE");

        validateResponseJsonPath(
                runner,
                jsonPath()
                        .expression("$.color", "yellow")
                        .expression("$.height", "0.01")
                        .expression("$.material", "rubber")
                        .expression("$.sound", "quack")
                        .expression("$.wingsState", "ACTIVE")
        );
    }

    @Test(description = "Проверка создания уточки с material = wood")
    @CitrusTest
    public void createWoodMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.01, "wood", "quack", "ACTIVE");

        validateResponseJsonPath(
                runner,
                jsonPath()
                        .expression("$.color", "yellow")
                        .expression("$.height", "0.01")
                        .expression("$.material", "wood")
                        .expression("$.sound", "quack")
                        .expression("$.wingsState", "ACTIVE")
        );
    }

    // DELETE /api/duck/delete
    @Test(description = "Проверка что уточка удаляется")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        deleteDuck(runner, "10009");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "Duck is deleted")
        );
    }

    // PUT /api/duck/update
    @Test(description = "Проверка изменения цвета и высоты утки")
    @CitrusTest
    public void updateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        updateDuck(runner, "10010", "red", 0.02, "rubber", "quack", "ACTIVE");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "Duck with id = 10010 is updated")
        );
    }

    @Test(description = "Проверка изменения цвета и звука утки")
    @CitrusTest
    public void updateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        updateDuck(runner, "10011", "white", 0.01, "rubber", "xru-xru", "ACTIVE");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "Duck with id = 10011 is updated")
        );
    }
}
