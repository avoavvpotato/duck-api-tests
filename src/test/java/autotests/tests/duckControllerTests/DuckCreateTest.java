package autotests.tests.duckControllerTests;

import autotests.clients.DuckCreateClient;
import autotests.payloads.request.DuckProperties;
import autotests.payloads.response.DuckPropertiesResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckCreateTest extends DuckCreateClient {
    @Test(description = "Проверка создания уточки с material = rubber")
    @CitrusTest
    public void createRubberMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties request = new DuckProperties()
                .color("yellow")
                .height(0.01)
                .material("rubber")
                .sound("quack")
                .wingsState("ACTIVE");

        createDuck(runner, request);

        //PAYLOAD

        //DuckPropertiesResponse expected = new DuckPropertiesResponse()
        //      .id("@isNumber()@")
        //    .color("yellow")
        //    .height(0.01)
        //    .material("rubber")
        //    .sound("quack")
        //    .wingsState("ACTIVE");

        // validateResponsePayload(runner, expected);

        // RESOURCES
        // validateResponseResource(runner, "duckCreateTest/rubberDuckResponse.json");

        // validateResponse(runner, "yellow", 0.01, "rubber", "quack", "ACTIVE");

        // STRING
        validateResponseString(runner,
                "{\n" +
                        "  \"id\": \"@isNumber()@\",\n" +
                        "  \"color\": \"yellow\",\n" +
                        "  \"height\": 0.01,\n" +
                        "  \"material\": \"rubber\",\n" +
                        "  \"sound\": \"quack\",\n" +
                        "  \"wingsState\": \"ACTIVE\"\n" +
                        "}");

        deleteDuck(runner, "${duckId}");
    }

    @Test(description = "Проверка создания уточки с material = wood")
    @CitrusTest
    public void createWoodMaterialString(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties request = new DuckProperties()
                .color("yellow")
                .height(0.01)
                .material("wood")
                .sound("quack")
                .wingsState("ACTIVE");


        createDuck(runner, request);

        //PAYLOAD
        //DuckPropertiesResponse expected = new DuckPropertiesResponse()
        //      .id("@isNumber()@")
        //      .color("yellow")
        //     .height(0.01)
        //      .material("wood")
        //      .sound("quack")
        //      .wingsState("ACTIVE");

        //validateResponsePayload(runner, expected);

        // RESOURCES
        // validateResponseResource(runner, "duckCreateTest/woodDuckResponse.json");

        //validateResponse(runner, "yellow", 0.01, "wood", "quack", "ACTIVE");

        // STRING
        validateResponseString(runner,
                "{\n" +
                        "  \"id\": \"@isNumber()@\",\n" +
                        "  \"color\": \"yellow\",\n" +
                        "  \"height\": 0.01,\n" +
                        "  \"material\": \"wood\",\n" +
                        "  \"sound\": \"quack\",\n" +
                        "  \"wingsState\": \"ACTIVE\"\n" +
                        "}");

        deleteDuck(runner, "${duckId}");
    }
}
