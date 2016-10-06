import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class MyTest {


    private String cookie_jsession, issue_key, comment_id, comment_text, created_issue, created_comment;
    private String login, password_right, password_wrong, issue_type, summary_text;

    PropertiesInput properties = new PropertiesInput();

    int Login(String login, String password){

        Response response = given()
                .contentType("application/json")
                .body("{\n" +
                        "    \"username\": \""+login+"\",\n" +
                        "    \"password\": \""+password+"\"\n" +
                        "}")
                .post("/rest/auth/1/session");

        cookie_jsession = "JSESSIONID="+response.getCookie("JSESSIONID");
        int status = response.getStatusCode();

        System.out.println("HTTP Status: "+status);
        System.out.println(cookie_jsession);

        return status;
    }

    @BeforeTest
     public void preConditions(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua";
        RestAssured.port = 8080;

        HashMap<String, String> content = properties.readProperties();

        issue_key = content.get("issue_key");
        comment_id = content.get("comment_id");
        issue_type = content.get("issue_type");
        comment_text = content.get("comment_text");
        summary_text = content.get("summary_text");

        login = content.get("login");
        password_right = content.get("password_right");
        password_wrong = content.get("password_wrong");

        Login(login, password_right);

    }


    @Test
    public void LoginPositive(){
        int status = Login(login, password_right);
        assertTrue(status==200);
    }

    @Test
    public void LoginNegative(){
        int status = Login(login, password_wrong);
        assertFalse(status==200);
    }

    @Test
    public void CreateIssue(){

        String createIssueBody = "{\n" +
                "\n" +
                "\t\"fields\": {\n" +
                "\t\t\"project\": {\n" +
                "\t\t\t\"id\": \"10315\"\n" +
                "\t\t},\n" +
                "\t\t\"summary\": \"creating test issue via restassured\",\n" +
                "\t\t\"issuetype\": {\n" +
                "\t\t\t\"id\": \"10004\"\n" +
                "\t\t},\n" +
                "\t\t\"assignee\": {\n" +
                "\t\t\t\"name\": \"alex00x6\"\n" +
                "\t\t},\n" +
                "\t\t\"reporter\": {\n" +
                "\t\t\t\"name\": \"alex00x6\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n";

            created_issue = given()
                    .contentType("application/json")
                    .cookie(cookie_jsession)
                    .body(createIssueBody)
                    .post("/rest/api/2/issue")
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
                .get("/rest/api/2/issue/"+issue_key)
                .then().log().all()
                .assertThat()
                .statusCode(200);

    }

    @Test
    public void AddCommentToIssue(){
        System.out.println("============== Add comment ===============");
        created_comment = given()
                .contentType("application/json")
                .cookie(cookie_jsession)
                .body("{ \"body\": \""+comment_text+"\"}")
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
                .body("{\"fields\": \t{\"issuetype\": {\"id\": \""+issue_type+"\"}}}")
                .put("/rest/api/2/issue/"+issue_key)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    public void ChangeSummaryOfIssue(){
        given().contentType("application/json").cookie(cookie_jsession).body("{\n" +
                "\t\"update\":{\n" +
                "\t\t\"summary\":[{\"set\":\""+summary_text+"\"}]\n" +
                "\t}\n" +
                "}\n").put("/rest/api/2/issue/"+issue_key).then().statusCode(204);
    }

    @Test
    public void SearchForIssue(){
        Response response = given().contentType("application/json").cookie(cookie_jsession).body("{\n" +
                "    \"jql\": \"project = QAAUT\",\n" +
                "    \"startAt\": 0,\n" +
                "    \"maxResults\": 15,\n" +
                "    \"fields\": [\n" +
                "        \"summary\",\n" +
                "        \"status\",\n" +
                "        \"assignee\"\n" +
                "    ]\n" +
                "}").log().all().post("/rest/api/2/search");

        assertTrue(response.getStatusCode()==200);
        assertTrue(response.getBody().asString().contains("total"));

    }

    //@Test
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

    @AfterTest
     public void afterTest(){
        if (created_issue!=null)
            System.out.println("created issue: "+created_issue);
        if (created_comment!=null)
            System.out.println("created comment id: "+created_comment+" to issue: "+issue_key);

    }

}
