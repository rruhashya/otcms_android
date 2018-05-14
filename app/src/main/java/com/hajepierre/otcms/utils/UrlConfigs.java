package com.hajepierre.otcms.utils;

/**
 * Created by jeanpierre on 5/12/18.
 */

public class UrlConfigs
{
    public final static String BASE_URL = "http://197.243.19.149/AndroPub2/Service2.asmx/";

    public final static String BORDERS_URL = BASE_URL + "getBordersList";

    public final static String ISSUES_URL = BASE_URL + "getIssueList";

    public final static String CATEGORIES_URL = BASE_URL + "getIssuCatList";

    public final static String CHECK_TRADER_URL = BASE_URL + "IsTraderRegistered";

    public final static String SAVE_TRADER_URL = BASE_URL + "AddNewTrader";

    public final static String SAVE_COMPLAINT_URL = BASE_URL + "AddNewComplaint";
}
