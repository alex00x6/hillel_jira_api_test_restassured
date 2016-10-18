import com.jayway.restassured.http.ContentType;
import inputData.GenerateJSONForJIRA;
import org.junit.experimental.categories.Category;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RequestGroups;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


public class TestJiraSecure {

    //RequestGroups requestGroups = new RequestGroups();


    @BeforeTest(groups = {"Issue", "Search", "Comment"})
    public void beforeTest(){
        RequestGroups requestGroups = new RequestGroups();
        //проверяем, какой поток
        long id = Thread.currentThread().getId();
        System.out.println("BeforeTest. Thread id is: " + id);
        //логинимся
        requestGroups.authenticateSecure();
    }


    @Test(groups = {"Issue"})
    public void createIssueDeleteIssue(){
        //проверяем, какой поток
        long id = Thread.currentThread().getId();
        System.out.println("createIssueDeleteIssue. Thread id is: " + id);

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

    @Test(groups = {"Issue"})
    public void createIssue(){
        //проверяем, какой поток
        long id = Thread.currentThread().getId();
        System.out.println("createIssue. Thread id is: " + id);

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

    @Test(groups = {"Issue"})
    public void deleteIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        //проверяем, какой поток
        long id = Thread.currentThread().getId();
        System.out.println("deleteIssue. Thread id is: " + id);

        //создаем issue
        requestGroups.createIssueSecure(generateJSONForJIRA.createSampleIssue());
        String issueId = requestGroups.extractResponseByPath("id");

        //удаляем issue и проверяем статускод
        requestGroups.deleteIssueSecure(issueId);
        assertEquals(requestGroups.response.statusCode(), 204);
    }

    @Test(groups = {"Search"}, priority = 3)
    public void search(){
        //проверяем, какой поток
        long id = Thread.currentThread().getId();
        System.out.println("Search. Thread id is: " + id);

        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        requestGroups.searchSecure(generateJSONForJIRA.search());
        System.out.println(requestGroups.response.asString());

    }

    @Test(groups = {"Issue"})
    public void getIssue(){
        //проверяем, какой поток
        long id = Thread.currentThread().getId();
        System.out.println("getIssue. Thread id is: " + id);

        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();


        requestGroups.createIssueSecure(generateJSONForJIRA.createSampleIssue());
        String issueId = requestGroups.extractResponseByPath("id");

        //тест и проверка
        requestGroups.getIssueSecure(issueId);
        System.out.println(requestGroups.response.asString());
        assertEquals(requestGroups.response.statusCode(), 200);
        assertTrue(requestGroups.response.contentType().contains(ContentType.JSON.toString()));

        requestGroups.deleteIssueSecure(issueId);
    }

    @Test(groups = {"Comment"})
    public void addCommentToIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        //проверяем, какой поток
        long id = Thread.currentThread().getId();
        System.out.println("addCommentToIssue. Thread id is: " + id);

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
