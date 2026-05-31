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
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckUpdateTest extends TestNGCitrusSpringSupport {
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

    public void extractDuckId(TestCaseRunner runner) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
    }

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
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "ACTIVE");
        extractDuckId(runner);

        updateDuck(runner, "${duckId}", "red", 0.02, "rubber", "quack", "ACTIVE");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "Duck with id = ${duckId} is updated")
        );
    }

    @Test(description = "Проверка изменения цвета и звука утки")
    @CitrusTest
    public void updateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "ACTIVE");
        extractDuckId(runner);

        updateDuck(runner, "${duckId}", "white", 0.01, "rubber", "xru-xru", "ACTIVE");

        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.message", "Duck with id = ${duckId} is updated")
        );
    }
}
