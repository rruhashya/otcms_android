package com.hajepierre.otcms.db;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeanpierre on 4/30/18.
 */

public class Complaint
{
    private int id;

    private String nationalId;

    private String issueCategory;

    private int issue;

    private String issueName;

    private String border;

    private String comments;

    private int borderId;

    public Complaint()
    {

    }

    public Complaint(String nationalId, String issueCategory, int issue, String border, String comment)
    {

        this.nationalId = nationalId;
        this.issueCategory = issueCategory;
        this.issue = issue;
        this.border = border;
        this.comments = comment;
    }

    public int getId()
    {

        return id;
    }

    public void setId(int id)
    {

        this.id = id;
    }

    public String getNationalId()
    {

        return nationalId;
    }

    public void setNationalId(String nationalId)
    {

        this.nationalId = nationalId;
    }

    public String getIssueCategory()
    {

        return issueCategory;
    }

    public void setIssueCategory(String issueCategory)
    {

        this.issueCategory = issueCategory;
    }

    public int getIssue()
    {

        return issue;
    }

    public void setIssue(int issue)
    {

        this.issue = issue;
    }

    public String getBorder()
    {

        return border;
    }

    public void setBorder(String border)
    {

        this.border = border;
    }

    public String getComments()
    {

        return comments;
    }

    public void setComments(String comment)
    {

        this.comments = comment;
    }

    public int getBorderId()
    {

        return borderId;
    }

    public void setBorderId(int borderId)
    {

        this.borderId = borderId;
    }

    @Override
    public String toString()
    {

        return "Complaint{" + "id=" + id + ", nationalId='" + nationalId + '\'' + ", issueCategory='" + issueCategory + '\'' + ", issue=" + issue + ", issueName='" + issueName + '\'' + ", border='" + border + '\'' + ", comments='" + comments + '\'' + ", borderId=" + borderId + '}';
    }

    public JSONObject toJson()
    {

        try
        {
            JSONObject json = new JSONObject();

            json.put("tradid", nationalId);
            json.put("issueid", issue);
            json.put("bordid", borderId);
            json.put("tradecomment", comments);
            json.put("catName", issueCategory);
            json.put("issuName", issueName);
            json.put("bname", border);
            return json;
        }

        catch (Exception ex)
        {
            return null;
        }
    }

    public String getIssueName()
    {

        return issueName;
    }

    public void setIssueName(String issueName)
    {

        this.issueName = issueName;
    }

    //    private String getIssueName(String id)
//    {
//
//        try
//        {
//            Issue issue = dbAdapter.findIssuesById(Integer.parseInt(id));
//            if (issue != null)
//            {
//                return issue.getName();
//            }
//        }
//        catch (Exception e)
//        {
//        }
//        return id+"";
//    }



//    private int getBorderName(String name)
//    {
//
//        try
//        {
//            Border border = new Border();
//            if (border != null)
//            {
//                return border.getId();
//            }
//        }
//        catch (Exception e)
//        {
//        }
//        return 1;
//    }
}
