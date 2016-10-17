import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import inputData.GenerateJSONForJIRA;
import inputData.PropertiesInput;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import apis.ApiUrls;
import utils.RequestSender;

import java.util.HashMap;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class MyTest {


    private String cookie_jsession;
    private String issue_key;
    private String created_issue;
    private String created_comment;
    private String login;
    private String password_right;
    private String password_wrong;

    PropertiesInput properties = new PropertiesInput();
    GenerateJSONForJIRA generateJSON = new GenerateJSONForJIRA();

    public void Login(){

        utils.RequestSender requestSender = new RequestSender();

        requestSender.authenticate();

        String sessionId = requestSender.extractResponseByPath("session.value");

        assertNotNull(sessionId);
        System.out.println(sessionId);


        /*
        Response response = given()
                .contentType("application/json")
                .body(generateJSON.login())
                .post(ApiUrls.LOGIN.getUri());

        cookie_jsession = "JSESSIONID="+response.getCookie("JSESSIONID");
        int status = response.getStatusCode();

        System.out.println("HTTP Status: "+status);
        System.out.println(cookie_jsession);

        */

        //return status;
    }

    @BeforeTest
     public void preConditions(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua";
        RestAssured.port = 8080;

        HashMap<String, String> content = properties.readProperties();

        issue_key = content.get("issue_key");

        login = content.get("login");
        password_right = content.get("password_right");
        password_wrong = content.get("password_wrong");

        //login(login, password_right);

    }


   // @Test
    //public void LoginPositive(){
   //     int status = login(login, password_right);
   //     assertTrue(status==200);
   // }

  // @Test
  //  public void LoginNegative(){
  //      int status = login(login, password_wrong);
 //       assertFalse(status==200);
  //  }

    @Test
    public void CreateIssue(){

            created_issue = given()
                    .contentType("application/json")
                    .cookie(cookie_jsession)
                    .body(generateJSON.createSampleIssue())
                    .log().all()
                    .post(ApiUrls.ISSUE.getUri())
                    .then().log().all().assertThat().statusCode(201).extract().path("key");
        System.out.println("created issue: "+created_issue);

    }

    @Test
    public void DeleteIssue(){
        if(created_issue==null){
            CreateIssue();
        }
        given()
                .contentType("application/json")
                .cookie(cookie_jsession)
                .delete("/rest/api/2/issue/"+created_issue)
                .then().log().all()
                .assertThat()
                .statusCode(204);
        System.out.println("issue "+created_issue+" was deleted");

    }

    @Test
    public void GetIssue(){

        given()
                .contentType("application/json")
                .cookie(cookie_jsession).log().all()
                .get(ApiUrls.ISSUE.getUri(issue_key))
                .then().log().all()
                .assertThat()
                .statusCode(200);

    }

    @Test
    public void AddCommentToIssue(){


        created_comment = given()
                .contentType("application/json")
                .cookie(cookie_jsession)
                .body(generateJSON.addCommentToIssue())
                .post("/rest/api/2/issue/"+issue_key+"/comment")
                .then()
                .assertThat()
                .statusCode(201).extract().path("id");
        System.out.println("Comment "+created_comment+" created to issue "+issue_key);
    }

    @Test
    public void DeleteCommentFromIssue(){
        System.out.println("============= Delete comment =============");
        if(created_comment==null)
            AddCommentToIssue();

        given()
                .contentType("application/json")
                .cookie(cookie_jsession)
                .delete("/rest/api/2/issue/"+issue_key+"/comment/"+created_comment)
                .then()
                .assertThat()
                .statusCode(204);
        System.out.println("comment "+created_comment+" to issue "+" was deleted");
    }

    @Test
     public void ChangeTypeOfIssue(){
        given()
                .contentType("application/json")
                .cookie(cookie_jsession)
                .body(generateJSON.changeTypeOfIssue())
                .put(ApiUrls.ISSUE.getUri(issue_key))
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    public void ChangeSummaryOfIssue(){
        given().contentType("application/json").cookie(cookie_jsession)
                .body(generateJSON.changeSummaryOfIssue())
                .put(ApiUrls.ISSUE.getUri(issue_key)).then().statusCode(204);
    }

    @Test
    public void SearchForIssue(){
        Response response = given()
                .contentType("application/json")
                .cookie(cookie_jsession)
                .body(generateJSON.search())
                .log().all()
                .post(ApiUrls.SEARCH.getUri());

        System.out.println(response.asString());

        assertTrue(response.getStatusCode()==200);
        assertTrue(response.getBody().asString().contains("total"));

    }

    //@Test
    /*
    public void deleteIssuesByIteration(){
        List<String> issues = properties.readListOfIssuesToDelete();
        for(int i = 0; i<issues.size(); i++){
        given()
                .contentType("application/json")
                .cookie(cookie_jsession)
                .delete("/rest/api/2/issue/"+issues.get(i))
                .then().log().all()
                .assertThat()
                .statusCode(204);
        System.out.println("issue "+issues.get(i)+" was deleted");
        }


    }
    */

    @AfterTest
     public void afterTest(){
        if (created_issue!=null)
            System.out.println("created issue: "+created_issue);
        if (created_comment!=null)
            System.out.println("created comment id: "+created_comment+" to issue: "+issue_key);

    }

}
