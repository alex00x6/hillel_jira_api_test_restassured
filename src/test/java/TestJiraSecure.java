import com.jayway.restassured.http.ContentType;
import inputData.GenerateJSONForJIRA;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RequestGroups;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


public class TestJiraSecure {
    RequestGroups requestGroups = new RequestGroups();

    @BeforeTest
    public void beforeTest(){
        //логинимся
        requestGroups.authenticateSecured();
    }

    @Test
    public void createIssueDeleteIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        //создаем issue
        requestGroups.createIssueSecure(generateJSONForJIRA.createSampleIssue());

        //проверяем
        System.out.println(requestGroups.response.asString());
        assertEquals(requestGroups.response.statusCode(), 201);
        assertTrue(requestGroups.response.contentType().contains(ContentType.JSON.toString()));

        //берем id того, что мы создали
        String issueId = requestGroups.extractResponseByPath("id");
        System.out.println(issueId);

        //удаляем то, что создали
        requestGroups.deleteIssueSecure(issueId);
        assertEquals(requestGroups.response.statusCode(), 204);
    }

    @Test
    public void createIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        //создаем issue
        requestGroups.createIssueSecure(generateJSONForJIRA.createSampleIssue());

        //проверяем
        System.out.println(requestGroups.response.asString());
        assertEquals(requestGroups.response.statusCode(), 201);
        assertTrue(requestGroups.response.contentType().contains(ContentType.JSON.toString()));

        //берем id того, что мы создали и удаляем
        String issueId = requestGroups.extractResponseByPath("id");
        requestGroups.deleteIssueSecure(issueId);
    }

    @Test
    public void deleteIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        //создаем issue
        requestGroups.createIssueSecure(generateJSONForJIRA.createSampleIssue());
        String issueId = requestGroups.extractResponseByPath("id");

        //удаляем issue и проверяем статускод
        requestGroups.deleteIssueSecure(issueId);
        assertEquals(requestGroups.response.statusCode(), 204);
    }

    @Test
    public void search(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        requestGroups.searchSecure(generateJSONForJIRA.search());
        System.out.println(requestGroups.response.asString());

    }

    @Test
    public void getIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        requestGroups.createIssueSecure(generateJSONForJIRA.createSampleIssue());
        String issueId = requestGroups.extractResponseByPath("id");

        //тест и проверка
        requestGroups.getIssueSecure(issueId);
        System.out.println(requestGroups.response.asString());
        assertEquals(requestGroups.response.statusCode(), 200);
        assertTrue(requestGroups.response.contentType().contains(ContentType.JSON.toString()));
    }

    @Test
    public void addCommentToIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        //создаем issue, получаем его id
        requestGroups.createIssueSecure(generateJSONForJIRA.createSampleIssue());
        String issueId = requestGroups.extractResponseByPath("id");

        //добавляем комент
        requestGroups.addCommentToIssueSecure(generateJSONForJIRA.addCommentToIssue(), issueId);

        //проверяем, всё ли на месте
        assertEquals(requestGroups.response.statusCode(), 201);
        assertTrue(requestGroups.response.contentType().contains(ContentType.JSON.toString()));
        //ну это прям уж совсем чтоб 100%
        System.out.println(requestGroups.extractResponseByPath("id"));
        System.out.println(requestGroups.extractAllResponseAsString());

        //удаляем созданную issue вместе с коментом
        requestGroups.deleteIssueSecure(issueId);
    }

}
