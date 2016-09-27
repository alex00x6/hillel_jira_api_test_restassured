import com.jayway.restassured.RestAssured;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static com.jayway.restassured.RestAssured.given;

public class example_MyIssue {

    String sessionId;

    @BeforeTest
    public void PreCons(){
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
    }

    @Test
    public void login(){

        String body = "{\n" +
                "    \"username\": \"a.a.piluck2\",\n" +
                "    \"password\": \"1234\"\n" +
                "}";

        sessionId = given().
                contentType("application/json").
                body(body).
                when().
                post("/rest/auth/1/session").
                then().
                extract().path("session.value");
        System.out.println(sessionId);
    }

    @Test
    public void createIssue(){
        given().contentType("application/json").cookie("JSESSIONID="+sessionId).body("{\n" +
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
                "}\n")
                .post("/rest/api/2/issue").then().statusCode(201);

    }
}