package autotests.management;

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

public class DuckCreateTest extends TestNGCitrusSpringSupport {
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
}
