package utils;

import apis.ApiUrls;

/**
 * Created by Storm on 11.10.2016.
 */
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

    public void addCommentToIssue(String issue, String comment){
        //TODO
    }
}
