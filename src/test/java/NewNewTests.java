import apis.ApiUrls;
import com.jayway.restassured.http.ContentType;
import inputData.GenerateJSONForJIRA;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.RequestGroups;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


public class NewNewTests {


    @BeforeTest
    public void beforeTest(){
        RequestGroups requestGroups = new RequestGroups();

        requestGroups.authenticate();

        System.out.println(requestGroups.response.asString());
        System.out.println(requestGroups.JSESSIONID);

        //TODO - вызывать по экземпляру, или сделать статическим и вызывать напрямую?
    }

    @Test
    public void createIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        //создаем issue
        requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());

        //проверяем
        assertEquals(requestGroups.response.statusCode(), 201);
        assertTrue(requestGroups.response.contentType().contains(ContentType.JSON.toString()));

        //удаляем то, что создали
        String issueId = requestGroups.extractResponseByPath("id");
        System.out.println(issueId);
        //requestGroups.deleteIssue(issueId);

    }


    @Test
    public void deleteIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        String issueId;

        //создаем issue
        requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());
        issueId = requestGroups.extractResponseByPath("id");
        System.out.println(requestGroups.extractAllResponseAsString());

        //удаляем
        requestGroups.deleteIssue(ApiUrls.ISSUE.getUri(issueId));
        System.out.println(requestGroups.extractAllResponseAsString());
        assertEquals(requestGroups.response.statusCode(), 204);
    }

    @Test
    public void getIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        //создаем issue
        requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());
        String issueId = requestGroups.extractResponseByPath("id");

        //сам тест get issue
        requestGroups.getIssue(issueId);
        assertEquals(requestGroups.response.statusCode(), 201);

        //удаляем то, что насоздавали
        requestGroups.deleteIssue(issueId);
    }


    @Test
    public void addCommentToIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        //TODO - дописать (желательно, когда заработает JIRA)
        requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());
        String issueId = requestGroups.extractResponseByPath("id");

        requestGroups.addCommentToIssue(generateJSONForJIRA.addCommentToIssue(), issueId);


    }

    @Test
    public void searchForIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        //requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());
        //String issueId = requestGroups.extractResponseByPath("id");

        requestGroups.search(generateJSONForJIRA.search());

        String searchResponse = requestGroups.response.asString();
        assertEquals(requestGroups.response.statusCode(), 200);
        assertTrue(requestGroups.response.contentType().contains(ContentType.JSON.toString()));
        assertTrue(searchResponse.contains("total"));
        assertTrue(requestGroups.response.getBody().asString().contains("summary"));

        System.out.println(requestGroups.response.asString());
    }

    @Test
    public void searchForIssueWithParams(){
        //requestGroups.createIssue(generateJSONForJIRA.createSampleIssue());
        //String issueId = requestGroups.extractResponseByPath("id");


        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        requestGroups.search(generateJSONForJIRA.search("project = QAAUT"));
        System.out.println(requestGroups.extractAllResponseAsString());

        String searchResponse = requestGroups.response.asString();
        assertEquals(requestGroups.response.statusCode(), 200);
        assertTrue(requestGroups.response.contentType().contains(ContentType.JSON.toString()));
        assertTrue(searchResponse.contains("total"));
    }

    @Test
    public void createForTestApiIssue(){
        RequestGroups requestGroups = new RequestGroups();
        GenerateJSONForJIRA generateJSONForJIRA = new GenerateJSONForJIRA();

        requestGroups.authenticate();
        System.out.println(requestGroups.extractAllResponseAsString());

        requestGroups.createIssue(generateJSONForJIRA.createForapiTestIssue());

        System.out.println(requestGroups.response.asString());


    }

}
