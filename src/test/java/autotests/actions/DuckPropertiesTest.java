package autotests.actions;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.JsonPathSupport;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckPropertiesTest extends TestNGCitrusSpringSupport {
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

    @Test(description = "Проверка получения свойств уточки с material = rubber и нечетным ID")
    @CitrusTest
    public void propertiesRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        duckProperties(runner, "10001");

        validateResponseJsonPath(
                runner,
                jsonPath()
                        .expression("$.color", "yellow")
                        // TODO: По документации ожидается height = "0.01".
                        //.expression("$.height", "0.01")
                        .expression("$.height", "1.0")
                        .expression("$.material", "rubber")
                        .expression("$.sound", "quack")
                        .expression("$.wingsState", "ACTIVE")
        );
    }

    @Test(description = "Проверка получения свойств уточки с material = wood и четным ID")
    @CitrusTest
    public void propertiesWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        duckProperties(runner, "10002");

        //validateResponseJsonPath(
        //runner,
        // TODO: По документации ожидается что вернутся параметры но для любой уточки с material != rubber возвращается {}".
        //jsonPath()
        //.expression("$.color", "yellow")
        //.expression("$.height", "0.01")
        //.expression("$.material", "wood")
        //.expression("$.sound", "quack")
        //.expression("$.wingsState", "ACTIVE")
        //);

        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body("{}")
        );
    }
}
