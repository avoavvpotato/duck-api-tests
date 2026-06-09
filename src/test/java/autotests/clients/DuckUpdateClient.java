package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckUpdateClient extends DuckClient {
    @Step("Эндпоинт для изменения уточки в базе")
    public void updateDuck(TestCaseRunner runner,
                           String id,
                           String color,
                           double height,
                           String material,
                           String sound,
                           String wingsState) {
        runner.$(
                http()
                        .client(duckService)
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
}
