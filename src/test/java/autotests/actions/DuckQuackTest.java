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

public class DuckQuackTest extends TestNGCitrusSpringSupport {
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

    // INSERT INTO duck (id, color, material, height, sound, wings_state)
    // VALUES
    //     (10003, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE'),
    //     (10004, 'yellow', 'rubber', 0.01, 'quack', 'ACTIVE');

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
        duckQuack(runner, "10003", "3", "2");

        validateResponseJsonPath(
                runner,
                // TODO: По документации ожидается "quack-quack, quack-quack, quack-quack". rep - кол повторений quack sound
                // jsonPath().expression("$.sound", "quack-quack, quack-quack, quack-quack")
                jsonPath().expression("$.sound", "quack-quack-quack, quack-quack-quack")
        );
    }

    @Test(description = "Проверка кряканья утки с корректным чётным id")
    @CitrusTest
    public void quackWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        duckQuack(runner, "10004", "3", "2");

        validateResponseJsonPath(
                runner,
                // TODO: По документации ожидается "quack-quack, quack-quack, quack-quack".
                // jsonPath().expression("$.sound", "quack-quack, quack-quack, quack-quack")
                jsonPath().expression("$.sound", "moo-moo-moo, moo-moo-moo")
        );
    }
}
