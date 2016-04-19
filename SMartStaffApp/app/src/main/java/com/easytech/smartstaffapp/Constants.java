package com.easytech.smartstaffapp;

/**
 * Created by Bana on 7/4/16.
 */
public class Constants {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Customer = "customerKey";
    public static final String loggedIn = "LoggedIn";
    public static final String itemList = "itemList";
    public static final String item = "itemInfo";
    public static final String pickList = "pickList";
    public static final String pick = "pickListInfo";
    public static final String delivery = "deliveryOrderInfo";
    public static final String purchase = "purchaseOrderInfo";

    public static final String success = "success";

    public static final String dateFormat = "dd/MM/yyyy";

    private static final String ipAddress = "192.168.0.100";

    public static final String reportString = "http://" + ipAddress + ":8080/SMart-war/store/PerformReportManagement/reportView.jsp";
    public static final String urlString = "http://" + ipAddress + ":8080/SMart-war/mobile/";

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    public static final int REQUEST_READ_CONTACTS = 0;
    public static final int RC_BARCODE_CAPTURE = 9001;

}
