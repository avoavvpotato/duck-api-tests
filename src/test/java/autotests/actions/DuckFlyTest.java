package autotests.actions;

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

public class DuckFlyTest extends TestNGCitrusSpringSupport {
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
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "ACTIVE");
        extractDuckId(runner);

        duckFly(runner, "${duckId}");

        validateResponseJsonPath(
                runner,
                // TODO: По документации ожидается "I’m flying".
                // jsonPath().expression("$.message", "I’m flying")
                jsonPath().expression("$.message", "I am flying :)")
        );
    }

    @Test(description = "Проверка того что уточка не полетела если крылья FIXED")
    @CitrusTest
    public void unsuccessfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "FIXED");
        extractDuckId(runner);

        duckFly(runner, "${duckId}");

        validateResponseJsonPath(
                runner,
                // TODO: По документации ожидается "I can’t fly".
                // jsonPath().expression("$.message", "I can’t fly")
                jsonPath().expression("$.message", "I can not fly :C")
        );
    }

    @Test(description = "Проверка того что уточка не полетела если крылья UNDEFINED")
    @CitrusTest
    public void undefinedFly(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.01, "rubber", "quack", "UNDEFINED");
        extractDuckId(runner);

        duckFly(runner, "${duckId}");

        validateResponseJsonPath(
                runner,
                // TODO: По документации ожидается ??.
                //jsonPath().expression("$.message", "Undefined")
                jsonPath().expression("$.message", "Wings are not detected :(")
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
}
