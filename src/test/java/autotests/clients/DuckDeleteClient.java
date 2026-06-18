package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;

import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

public class DuckDeleteClient extends DuckClient {
    @Step("Эндпоинт для удаления уточки из базы")
    public void deleteDuck(TestCaseRunner runner, String id) {
        sendDeleteRequestWithQueryParameter(runner, duckService, "/api/duck/delete", "id", id);
    }

    @Step("Проверка что уточка удалена из базы")
    public void validateDuckDeletedFromDatabase(TestCaseRunner runner, String id) {
        String sqlQuery = "SELECT COUNT(*) as cnt FROM DUCK WHERE ID = " + id;
        runner.$(query(testDb)
                .statement(sqlQuery)
                .validate("cnt", "0"));
    }
}
