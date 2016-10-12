import com.jayway.restassured.http.ContentType;
import inputData.GenerateJSONForJIRA;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RequestGroups;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


public class NewNewTests {

    RequestGroups requestGroups = new RequestGroups();
    GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

    @BeforeTest
    public void beforeTest(){
        requestGroups.authenticate();
    }

    @Test
    public void createIssue(){

        //создаем issue
        requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());

        //проверяем
        assertEquals(RequestGroups.response.statusCode(), 201);
        assertTrue(requestGroups.response.contentType().contains(ContentType.JSON.toString()));

        //удаляем то, что создали
        String issueId = requestGroups.extractResponseByPath("id");
        requestGroups.deleteIssue(issueId);

    }

    @Test
    public void deleteIssue(){
        String issueId;

        //создаем issue
        requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());
        issueId = requestGroups.extractResponseByPath("id");

        //удаляем
        requestGroups.deleteIssue(issueId);
        assertEquals(RequestGroups.response.statusCode(), 204);
    }

    @Test
    public void getIssue(){
        //создаем issue
        requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());
        String issueId = requestGroups.extractResponseByPath("id");

        //сам тест get issue
        requestGroups.getIssue(issueId);
        assertEquals(RequestGroups.response.statusCode(), 201);

        //удаляем то, что насоздавали
        requestGroups.deleteIssue(issueId);
    }


    @Test
    public void addCommentToIssue(){

        //TODO - дописать (желательно, когда заработает JIRA)
        requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());
        String issueId = requestGroups.extractResponseByPath("id");

        String commentId;
    }

    @Test
    public void searchForIssue(){
        RequestGroups requestGroups1 = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA1 = new GenerateJSONForJIRA();

        //requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());
        //String issueId = requestGroups.extractResponseByPath("id");

        requestGroups1.search(generateJSONForJIRA1.search());

        String searchResponse = requestGroups1.response.asString();
        assertEquals(requestGroups1.response.statusCode(), 200);
        assertTrue(requestGroups1.response.contentType().contains(ContentType.JSON.toString()));
        assertTrue(searchResponse.contains("total"));
    }

    @Test
    public void searchForIssueWithParams(){
        //requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());
        //String issueId = requestGroups.extractResponseByPath("id");


        RequestGroups requestGroups1 = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA1 = new GenerateJSONForJIRA();

        requestGroups1.search(generateJSONForJIRA1.search("project = QAAUT"));
        System.out.println(requestGroups1.extractAllResponseAsString());

        String searchResponse = requestGroups1.response.asString();
        assertEquals(requestGroups1.response.statusCode(), 200);
        assertTrue(requestGroups1.response.contentType().contains(ContentType.JSON.toString()));
        assertTrue(searchResponse.contains("total"));
    }


    /*
    @Test
    public void test(){
       String shit =  generateJSONForJIRA.changeTypeOfIssue();
        System.out.println(shit);
    }
    */
}
