package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckCreateClient extends DuckClient {
    //Payload
    @Step("Валидация ответа create через Payload")
    @Description("Валидация полученного ответа с Payload")
    public void validateResponsePayload(TestCaseRunner runner, Object expectedPayload) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper()))
                        .extract(fromBody().expression("$.id", "duckId"))
        );
    }

    //String
    @Step("Валидация ответа create через строку")
    @Description("Валидация полученного ответа строкой")
    public void validateResponseString(TestCaseRunner runner, String expectedBody) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(expectedBody)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
    }
}
