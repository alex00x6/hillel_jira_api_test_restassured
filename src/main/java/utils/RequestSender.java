package utils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import inputData.GenerateJSONForJIRA;

import static com.jayway.restassured.RestAssured.given;

public class RequestSender {
    public static String JSESSIONID = null;
    public final static ContentType CONTENT_TYPE = ContentType.JSON;
    public RequestSpecification requestSpecification = null;
    public Response response = null;
    public static String ATLASSIAN_TOKEN = null;
    public static String STUDIO_TOKEN = null;


    public void authenticate() {
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080/";

        GenerateJSONForJIRA generateJSON = new GenerateJSONForJIRA();
        String credentials = generateJSON.login();

        createRequest(credentials)
                .post(apis.ApiUrls.LOGIN.getUri());

        this.JSESSIONID = response.then().extract().path("session.value");
    }

    public void authenticateSecure() {
        RestAssured.baseURI = "https://forapitest.atlassian.net";

        GenerateJSONForJIRA generateJSON = new GenerateJSONForJIRA();
        String credentials = generateJSON.login();

        createRequest(credentials)
                .post(apis.ApiUrls.LOGIN.getUri());

        this.JSESSIONID = response.then().extract().path("session.value");
        this.ATLASSIAN_TOKEN = response.then().extract().cookie("atlassian.xsrf.token");
        this.STUDIO_TOKEN = response.then().extract().cookie("studio.crowd.tokenkey");
        System.out.println(JSESSIONID);
        System.out.println(ATLASSIAN_TOKEN);
        System.out.println(STUDIO_TOKEN);
    }



    public RequestSender createRequest(String body) {
        this.createRequestSpecification()
                .addHeader("Content-Type", CONTENT_TYPE.toString())
                .addHeader("Cookie", "JSESSIONID=" + RequestSender.JSESSIONID)
                .addBody(body);
        return this;
    }

    public RequestSender createRequestSecure(String body){
        this.createRequestSpecification()
                .addHeader("Content-Type", CONTENT_TYPE.toString())
                .addHeader("Cookie", "JSESSIONID="+RequestSender.JSESSIONID)
                .addHeader("Cookie", "atlassian.xsrf.token="+RequestSender.ATLASSIAN_TOKEN)
                .addHeader("Cookie", "studio.crowd.tokenkey="+RequestSender.STUDIO_TOKEN)
                .addBody(body);
        return this;
    }

    public RequestSender createEmptyRequest() {
        this.createRequestSpecification()
                .addHeader("Content-Type", CONTENT_TYPE.toString())
                .addHeader("Cookie", "JSESSIONID=" + RequestSender.JSESSIONID);
        return this;
    }

    public RequestSender createEmptyRequestSecure() {
        this.createRequestSpecification()
                .addHeader("Content-Type", CONTENT_TYPE.toString())
                .addHeader("Cookie", "JSESSIONID="+RequestSender.JSESSIONID)
                .addHeader("Cookie", "atlassian.xsrf.token="+RequestSender.ATLASSIAN_TOKEN)
                .addHeader("Cookie", "studio.crowd.tokenkey="+RequestSender.STUDIO_TOKEN);
        return this;
    }

    public RequestSender createRequestSpecification() {
        requestSpecification = given().
                when();
        return this;
    }

    // этот метод сможет добавлять столько угодно хедеров
    public RequestSender addHeader(String headerName, String headerValue) {
        requestSpecification.header(headerName, headerValue);
        return this;
    }

    public RequestSender addBody(String body) {
        requestSpecification.body(body);
        return this;
    }

    public RequestSender post(String uri) {
        response = requestSpecification.post(uri);
        return this;
    }

    public RequestSender delete(String uri){
        response = requestSpecification.delete(uri);
        return this;
    }

    public RequestSender get(String uri){
        response = requestSpecification.get(uri);
        return this;
    }

    public RequestSender put(String uri) {
        response = requestSpecification.put(uri);
        return this;
    }

    public String extractResponseByPath(String path){
        return response.then().extract().path(path);
    }

    //public String extractResponseByPathToString(String path){ return  response.then().extract().path(path).toString();}

    public String extractAllResponseAsString(){
        return response.then().extract().asString();
    }


}
