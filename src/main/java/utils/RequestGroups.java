package utils;

import apis.ApiUrls;

public class RequestGroups {


    public RequestSender createIssue(String body){
        RequestSender requestSender = new RequestSender();
        requestSender.createRequest(body).post(ApiUrls.ISSUE.getUri());
        return requestSender;
    }

    public RequestSender deleteIssue(String issue){
        RequestSender requestSender = new RequestSender();
        requestSender.createEmptyRequest().delete(ApiUrls.ISSUE.getUri(issue));
        return requestSender;
    }

    public RequestSender getIssue(String issue){
        RequestSender requestSender = new RequestSender();
        requestSender.createEmptyRequest().get(ApiUrls.ISSUE.getUri(issue));
        return requestSender;
    }

    public RequestSender search(String body){
        RequestSender requestSender = new RequestSender();
        requestSender.createRequest(body).post(ApiUrls.SEARCH.getUri());
        return requestSender;
    }

    public RequestSender addCommentToIssue(String body, String issue){
        RequestSender requestSender = new RequestSender();
        requestSender.createRequest(body).post(ApiUrls.ISSUE.getUri(issue+"/comment"));
        return requestSender;
    }

    public RequestSender authenticate(){
        RequestSender requestSender = new RequestSender();
        requestSender.authenticate();
        return requestSender;
    }


    /*
    public RequestSender createIssueSecure(String body){
        RequestSender requestSender = new RequestSender();
        //requestSender.createRequestSecure(body).post(ApiUrls.ISSUE.getUri());
        return requestSender;
    }

    public RequestSender deleteIssueSecure(String issue){
        RequestSender requestSender = new RequestSender();
        requestSender.createEmptyRequestSecure().delete(ApiUrls.ISSUE.getUri(issue));
        return requestSender;
    }

    public RequestSender getIssueSecure(String issue){
        RequestSender requestSender = new RequestSender();
        requestSender.createEmptyRequestSecure().get(ApiUrls.ISSUE.getUri(issue));
        return requestSender;
    }

    public RequestSender searchSecure(String body){
        RequestSender requestSender = new RequestSender();
        //requestSender.createRequestSecure(body).post(ApiUrls.SEARCH.getUri());
        return requestSender;
    }


    public RequestSender addCommentToIssueSecure(String body, String issue){
        RequestSender requestSender = new RequestSender();
        requestSender.createRequestSecure(body).post(ApiUrls.ISSUE.getUri(issue+"/comment"));
        return requestSender;
    }

    public RequestSender authenticateSecure(){
        RequestSender requestSender = new RequestSender();
        requestSender.authenticateSecure();
        return requestSender;
    }
*/

}
