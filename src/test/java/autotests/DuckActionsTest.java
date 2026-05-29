package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.JsonPathSupport;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckActionsTest extends TestNGCitrusSpringSupport {
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
    //INSERT INTO duck (id, color, material, height, sound, wings_state)
    //VALUES
    //    (10001, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
    //    (10002, 'yellow', 'wood', 0.01, 'quack', 'ACTIVE'),
    public void validateResponseJsonPath(TestCaseRunner runner,
                                         JsonPathSupport body) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .validate(body)
        );
    }

    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/properties")
                        .queryParam("id", id)
        );
    }

    // Я пробовала делать с первого id но потом этап создания уточки не проходил потому что не было автоинкримента в базе данных  BIGINT(19) NOT NULL
    @Test(description = "Проверка получения свойств уточки с material = wood и нечетным ID")
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

    @Test(description = "Проверка получения свойств уточки с material = rubber и четным ID")
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
    // INSERT INTO duck (id, color, material, height, sound, wings_state)
    //VALUES
    //    (10003, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
    //    (10004, 'yellow', 'rubber', 0.01, 'quack', 'FIXED'),
    //    (10005, 'yellow', 'rubber', 0.01, 'quack', 'UNDEFINED');

    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/fly")
                        .queryParam("id", id)
        );
    }

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

    // Тут не совсем понятно как делать проверку, потому что уточка просто не должна
    // была создаваться и наверное эту проверку мы бы сделали на этапе создания. Нет требований для этого случая
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
    // INSERT INTO duck (id, color, material, height, sound, wings_state)
    // VALUES (10006, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE');
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id));
    }

    @Test(description = "Проверка того, что уточка поплыла")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        duckSwim(runner, "10006");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "I’m swimming")
        );
    }

    @Test(description = "Проверка плавания для несуществующей утки")
    @CitrusTest
    public void swimNonExisting(@Optional @CitrusResource TestCaseRunner runner) {
        duckSwim(runner, "99999");

        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
        );
    }

    // INSERT INTO duck (id, color, material, height, sound, wings_state)
    // VALUES
    //     (10007, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
    //     (10008, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE');

    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/quack")
                        .queryParam("id", id)
                        .queryParam("repetitionCount", repetitionCount)
                        .queryParam("soundCount", soundCount)
        );
    }

    @Test(description = "Проверка кряканья утки с корректным нечётным id")
    @CitrusTest
    public void quackWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        duckQuack(runner, "10007", "3", "2");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.sound", "quack-quack, quack-quack, quack-quack")
        );
    }

    @Test(description = "Проверка кряканья утки с корректным чётным id")
    @CitrusTest
    public void quackWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        duckQuack(runner, "10008", "3", "2");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.sound", "quack-quack, quack-quack, quack-quack")
        );
    }

    // POST /api/duck/create
    // Не знаю как правильнее в базе данных нет ID BIGINT(19) NOT NULL автоинкримента
    // Я оставила свободными с 1 id чтобы там было свободно, но как более правильно не понятно
    public void createDuck(TestCaseRunner runner,
                           String color,
                           double height,
                           String material,
                           String sound,
                           String wingsState) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n" +
                                "\"color\": \"" + color + "\",\n" +
                                "\"height\": " + height + ",\n" +
                                "\"material\": \"" + material + "\",\n" +
                                "\"sound\": \"" + sound + "\",\n" +
                                "\"wingsState\": \"" + wingsState + "\"\n" +
                                "}")
        );
    }

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

    // INSERT INTO duck (id, color, material, height, sound, wings_state)
    // VALUES (10009, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE');
    public void deleteDuck(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", id)
        );
    }

    @Test(description = "Проверка что уточка удаляется")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        deleteDuck(runner, "10009");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "Duck is deleted")
        );
    }

    // INSERT INTO duck (id, color, material, height, sound, wings_state)
    // VALUES
    //     (10010, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
    //     (10011, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE');
    public void updateDuck(TestCaseRunner runner,
                           String id,
                           String color,
                           double height,
                           String material,
                           String sound,
                           String wingsState) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .put("/api/duck/update")
                        .queryParam("id", id)
                        .queryParam("color", color)
                        .queryParam("height", String.valueOf(height))
                        .queryParam("material", material)
                        .queryParam("sound", sound)
                        .queryParam("wingsState", wingsState)
        );
    }

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
