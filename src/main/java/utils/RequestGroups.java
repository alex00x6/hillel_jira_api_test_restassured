package utils;

import apis.ApiUrls;

public class RequestGroups extends RequestSender{

    RequestSender requestSender = new RequestSender();

    public void createIssue(String body){
        requestSender.createRequest(body).post(ApiUrls.ISSUE.getUri());
    }

    public void deleteIssue(String issue){
        requestSender.createEmptyRequest().delete(ApiUrls.ISSUE.getUri(issue));
    }

    public void getIssue(String issue){
        requestSender.createEmptyRequest().get(ApiUrls.ISSUE.getUri(issue));
    }

    public void search(String body){
        requestSender.createRequest(body).post(ApiUrls.SEARCH.getUri());
    }

    public void createIssueSecure(String body){
        requestSender.createRequestSecure(body).post(ApiUrls.ISSUE.getUri());
    }

    public void deleteIssueSecure(String issue){
        requestSender.createEmptyRequestSecure().delete(ApiUrls.ISSUE.getUri(issue));
    }

    public void getIssueSecure(String issue){
        requestSender.createEmptyRequestSecure().get(ApiUrls.ISSUE.getUri(issue));
    }

    public void searchSecure(String body){
        requestSender.createRequestSecure(body).post(ApiUrls.SEARCH.getUri());
    }

    public void addCommentToIssue(String body, String issue){
        //TODO - проверить, не проверялось
        requestSender.createRequest(body).post(ApiUrls.ISSUE.getUri(issue+"/comment"));
    }

    public void addCommentToIssueSecure(String body, String issue){
        requestSender.createRequestSecure(body).post(ApiUrls.ISSUE.getUri(issue+"/comment"));
    }
}
