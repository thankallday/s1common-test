package com.scholarone.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.scholarone.ScholarOneException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AESEncryption.class})
@PowerMockIgnore({"javax.crypto.*" })
public class EncryptParameterTest
{
  private String salt = "01234";
      
  private StringBuffer data1;

  private StringBuffer data2;

  private StringBuffer data3;

  private StringBuffer data4;

  private StringBuffer data5;

  private StringBuffer data6;

  private StringBuffer data7;

  private StringBuffer data8;

  private StringBuffer data9;

  private StringBuffer data10;

  private StringBuffer data11;

  private StringBuffer data12;

  private StringBuffer data13;

  private StringBuffer data14;

  private StringBuffer data15;

  private StringBuffer data16;
  
  private StringBuffer data17;
  
  private StringBuffer data18;
  
  private StringBuffer data19;
  
  private StringBuffer data20;

  private StringBuffer data21;
  
  private String result1;

  private String result2;

  private String result3;

  private String result4;

  private String result5;

  private String result6;

  private String result7;

  private String result8;

  private String result9;

  private String result10;

  private String result11;

  private String result12;

  private String result13;

  private String result14;

  private String result15;

  private String result16;
  
  private String result17;
  
  private String result18;
  
  private String result19;
  
  private String result20;

  private String result21;
  
  @Before
  public void setUp() throws Exception
  {
    PowerMockito.whenNew(Key.class).withNoArguments().thenReturn(new KeyMock());

    data1 = new StringBuffer();
    data1
        .append("http://mc-stage.manuscriptcentral.com/qared?"
            + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
            + "CURRENT_QUEUE_TYPE=TYPE_TASK_QUEUE&"
            + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
            + "CURRENT_QUEUE_VALUE=14925&PRE_ACTION=JUMP_TO_FIRST_MANUSCRIPT&NEXT_PAGE=MANUSCRIPT_DETAILS&CURRENT_PAGE=DASHBOARD&MS_LIST_TO_DISPLAY5809=14925&CURRENT_ROLE_ID=5809&CURRENT_USER_ID=1&DOCUMENT_HASHCODE=&"
            + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
            + "CONFIG_ID=1392&CURRENT_GROUP_NAME=&CURRENT_GROUP_NAME_ID=&PAGE_NAME=DASHBOARD");

    data2 = new StringBuffer();
    data2
        .append("http://mc-stage.manuscriptcentral.com/qared?"
            + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
            + "CURRENT_QUEUE_TYPE=TYPE_TASK_QUEUE&"
            + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
            + "CURRENT_QUEUE_VALUE=14925&PRE_ACTION=JUMP_TO_FIRST_MANUSCRIPT&NEXT_PAGE=MANUSCRIPT_DETAILS&CURRENT_PAGE=DASHBOARD&MS_LIST_TO_DISPLAY5809=14925&CURRENT_ROLE_ID=5809&CURRENT_USER_ID=1&DOCUMENT_HASHCODE=&"
            + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
            + "CONFIG_ID=1392&CURRENT_GROUP_NAME=&CURRENT_GROUP_NAME_ID=&PAGE_NAME=DASHBOARD");

    data4 = new StringBuffer();
    data4.append("<input type = \"hidden\" name =\"PORTAL\" value = \"null\">");
    data4.append("<input type = \"hidden\" name =\"S1LOGIN\" value = \"null\">");
    data4.append("<input type = \"hidden\" name =\"NEXT_PAGE\" value = \"\">");
    data4.append("<input type = \"hidden\" name =\"NEXT_PAGE_FOR_REDIRECT\" value = \"\">");
    data4.append("<input type = \"hidden\" name =\"" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
        + "CURRENT_PAGE\" value=\"MANUSCRIPT_DETAILS\">");
    data4.append("<input type = \"hidden\" name =\"PRE_ACTION\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"XIK_POSTACT\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
        + "CURRENT_ROLE_ID\" value=\"5809\">");
    data4.append("<input type = \"hidden\" name =\"SWITCH_CONFIG_ID\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"CHANGE_PORTAL_CONFIG_ID_FOR_LOGIN_PAGE\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"SWITCH_CONFIG_ID_PREV\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"NEW_USER_CONFIG_ID\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"PROXY_TO_PAGE_NAME\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"PROXY_TO_ROLE_ID\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"PROXY_TO_DOCUMENT_ID\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"PROXY_TO_DOCUMENT_TASK_ID\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"PAGE_DIRTY_FL\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"PAGE_LOADED_FLAG\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"DISPLAY\" value=\"\">");
    data4.append("<input type = \"hidden\" name =\"" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
        + "DOCUMENT_HASHCODE\" value=\"1392837486\">");
    data4.append("<input type = \"hidden\" name =\"CONFIG_ID\" value=\"1392\">");

    data5 = new StringBuffer();
    data5.append("<input type = 'hidden' name ='PORTAL' value = 'null'>");
    data5.append("<input type = 'hidden' name ='S1LOGIN' value = 'null'>");
    data5.append("<input type = 'hidden' name ='NEXT_PAGE' value = ''>");
    data5.append("<input type = 'hidden' name ='NEXT_PAGE_FOR_REDIRECT' value = ''>");
    data5.append("<input type = 'hidden' name ='" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
        + "CURRENT_PAGE' value='MANUSCRIPT_DETAILS'>");
    data5.append("<input type = 'hidden' name ='PRE_ACTION' value=''>");
    data5.append("<input type = 'hidden' name ='XIK_POSTACT' value=''>");
    data5.append("<input type = 'hidden' name ='" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
        + "CURRENT_ROLE_ID' value='5809'>");
    data5.append("<input type = 'hidden' name ='SWITCH_CONFIG_ID' value=''>");
    data5.append("<input type = 'hidden' name ='CHANGE_PORTAL_CONFIG_ID_FOR_LOGIN_PAGE' value=''>");
    data5.append("<input type = 'hidden' name ='SWITCH_CONFIG_ID_PREV' value=''>");
    data5.append("<input type = 'hidden' name ='NEW_USER_CONFIG_ID' value=''>");
    data5.append("<input type = 'hidden' name ='PROXY_TO_PAGE_NAME' value=''>");
    data5.append("<input type = 'hidden' name ='PROXY_TO_ROLE_ID' value=''>");
    data5.append("<input type = 'hidden' name ='PROXY_TO_DOCUMENT_ID' value=''>");
    data5.append("<input type = 'hidden' name ='PROXY_TO_DOCUMENT_TASK_ID' value=''>");
    data5.append("<input type = 'hidden' name ='PAGE_DIRTY_FL' value=''>");
    data5.append("<input type = 'hidden' name ='PAGE_LOADED_FLAG' value=''>");
    data5.append("<input type = 'hidden' name ='DISPLAY' value=''>");
    data5.append("<input type = 'hidden' name ='" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
        + "SANITY_CHECK_DOCUMENT_ID' value='2888079'>");
    data5.append("<input type = 'hidden' name ='" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
        + "DOCUMENT_HASHCODE' value='1392837486'>");
    data5.append("<input type = 'hidden' name ='CONFIG_ID' value='1392'>");

    data6 = new StringBuffer();
    data6.append("<li><a href=\"javascript:setDataAndNextPage('PRE_ACTION','" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
        + "PERSON_EDIT_ACCOUNT','EDIT_ACCOUNT_1')\">E-Mail / Name</a></li>");
    data6
        .append("<li><a href=\"javascript:setDataAndNextPage('PRE_ACTION','PERSON_EDIT_ACCOUNT','EDIT_ACCOUNT_2')\">Address</a></li>");
    data6.append("<li><a href=\"javascript:setDataAndNextPage('PRE_ACTION','" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
        + "PERSON_EDIT_ACCOUNT','EDIT_ACCOUNT_3')\">User ID & Password</a>");
    data6.append("setDataAndNextPage('PRE_ACTION','" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
        + "PERFORM_HEADER_QUICK_SEARCH','MANUSCRIPT_DETAILS');");
    data6
        .append("<TD nowrap valign=middle align=left><IMG SRC=\"/images/en_US/icons/no_previous_mss.gif\" BORDER=0  ALT=\"Previous Document\"></TD>");
    data6
        .append("<TD nowrap valign=middle CLASS=\"pagecontents\" align=middle><P class=\"footer\">&nbsp;1 / 2&nbsp;</p></TD>");
    data6
        .append("<TD nowrap valign=middle align=left><A HREF=\"javascript:setField('MANUSCRIPT_DETAILS_OVERRIDE_TASK_TAG','');setField('MANUSCRIPT_DETAILS_SHOW_TAB_LIST','T121956182');setField('"
            + "XIK_TAGACT','VIEW_MANUSCRIPT_DETAILS'); setField('CLEAR_SEARCH','Y'); setDataAndNextPage('"
            + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
            + "NEXT_PREV_DOCUMENT_ID','15866723','MANUSCRIPT_DETAILS');\"><IMG SRC=\"/images/en_US/icons/next_mss.gif\" BORDER=0 ALT=\"Next Document\"></a></TD>");

    data7 = new StringBuffer();
    data7.append("document.forms[0]." + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX + "PRE_ACTION.value='SAVE_MS_DETAILS';");
    data7.append("onclick=\"javascript:document.forms[0]." + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX
        + "ATTRIBUTE_SEARCH_PREFIX.value='newRP'\"");

    data8 = new StringBuffer();
    data8.append("<TD CLASS=\"tablelightcolor\">");
    data8
        .append("<input type=\"hidden\" name=\"" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX + "ROLE_ID1\" value=\"23190\">");
    data8.append("<SELECT CLASS=pagecontents NAME=\"" + EncryptionUtility.ENCRYPT_PARAMETER_PREFIX + "ROLE_TYPE_ID1\">");
    data8.append("<OPTION  VALUE=\"5\">Admin</OPTION>");
    data8.append("<OPTION SELECTED VALUE=\"1\">Author</OPTION>");
    data8.append("<OPTION  VALUE=\"11\">Client Configuration</OPTION>");
    data8.append("<OPTION  VALUE=\"3\">Editor</OPTION>");
    data8.append("<OPTION  VALUE=\"4\">Editor In Chief</OPTION>");
    data8.append("<OPTION  VALUE=\"8\">Production Editor</OPTION>");
    data8.append("<OPTION  VALUE=\"2\">Reviewer</OPTION>");
    data8.append("<OPTION  VALUE=\"7\">Support</OPTION>");
    data8.append("<OPTION  VALUE=\"6\">SysAdmin</OPTION>");
    data8.append("<OPTION  VALUE=\"9\">SysAdmin Read Only</OPTION>");
    data8.append("</SELECT>");
    data8.append("</TD>");
    data8.append("<TD CLASS=\"tablelightcolor\">");
    data8.append("<input type=\"hidden\" name=\"ROLE_ID1\" value=\"23190\">");
    data8.append("<SELECT CLASS=pagecontents NAME=\"ROLE_TYPE_ID1\">");
    data8.append("<OPTION  VALUE=\"5\">Admin</OPTION>");
    data8.append("<OPTION SELECTED VALUE=\"1\">Author</OPTION>");
    data8.append("<OPTION  VALUE=\"11\">Client Configuration</OPTION>");
    data8.append("<OPTION  VALUE=\"3\">Editor</OPTION>");
    data8.append("<OPTION  VALUE=\"4\">Editor In Chief</OPTION>");
    data8.append("<OPTION  VALUE=\"8\">Production Editor</OPTION>");
    data8.append("<OPTION  VALUE=\"2\">Reviewer</OPTION>");
    data8.append("<OPTION  VALUE=\"7\">Support</OPTION>");
    data8.append("<OPTION  VALUE=\"6\">SysAdmin</OPTION>");
    data8.append("<OPTION  VALUE=\"9\">SysAdmin Read Only</OPTION>");
    data8.append("</SELECT>");
    data8.append("</TD>");

    data9 = new StringBuffer();
    data9.append("<TD>");
    data9.append("<SELECT CLASS='pagecontents' SIZE=1 NAME='XIK_CONTACT_AUTHOR_ID'>");
    data9.append("<OPTION value='331680'  SELECTED >Dobrijevic, Davor </option>");
    data9.append("</SELECT>");
    data9.append("</TD>");

    data10 = new StringBuffer();
    data10
        .append("&XIK_TASK_REC_DEC_ID=' + trd5 + '&TRANSFER_TO=' + transferTo + '&DECISION_EMAIL_FORCE_REFRESH_FL=' + forceRefresh + '&XIK_EMAIL_TEMPLATE_ID=");

    data11 = new StringBuffer();
    data11
        .append("<select class=\"pagecontents\" name=\"XIK_PLAGIARISM_DOC_FILE_ID\" id=\"XIK_PLAGIARISM_DOC_FILE_ID\">");
    data11.append("<option value=\"-1\">Select...</option>");
    data11.append("</select>");

    data12 = new StringBuffer();
    data12.append("<td><select class=\"footer\" name=\"XIK_NEW_CONFIG_ID\">");
    data12.append("<option value=\"-1\">Select...</option>");
    data12.append("    <option value = \"50\">japr: Journal of A...</option>");
    data12.append("</select></td>");

    data13 = new StringBuffer();
    data13.append("<input type=\"HIDDEN\" name=\"XIK_ZIP_ATTRIBUTE_TYPE_ID1000\" value=\"9539\">");

    data14 = new StringBuffer();
    data14.append("<td valign=top><input type=radio name=\"32637_XIK_CATEGORY_ID_295\" checked value=\"0\"></td>");

    data15 = new StringBuffer();
    data15
        .append("<input type=text name=\"XIK_ASSIGNMENT_IDx90656624x0\" class=pagecontents value=\"28389\" size=10 maxlength=10>");

    data16 = new StringBuffer();
    data16
        .append("<a href=\"#\" onclick=\"javascript:openOrcidWindow('http://sandbox.orcid.org/oauth/authorize?response_type=code&amp;client_id=0000-0002-4923-9632&amp;scope=%2Fauthenticate%20%2Forcid-bio%2Fread-limited%20%2Forcid-bio%2Fexternal-identifiers%2Fcreate&amp;redirect_uri=https%3A%2F%2Fqa1-test3.manuscriptcentral.com%2Fqa-auto-h0h%3FXIK_TAGACT%3DGET_ORCID_AUTH','orcidwindow'); return false;\">here</a>");

    data17 = new StringBuffer();
    data17.append("'http://qa1-test3.manuscriptcentral.com/LongRequest/qared?XIK_TAGACT=DOWNLOAD_FILE_BY_NAME&amp;XIK_DOCU_ID=17235306&amp;FILE_TO_DOWNLOAD=17235306_File000000_356160111.html-withlinks.htm&amp;FILE_KEY=-1329165952&amp;FILE_NAME_KEY=-1948735973&amp;DOWNLOAD=TRUE&amp;FILE_TYPE=DOCUMENT_BLINDED&amp;XIK_CUR_ROLE_ID=5805'<br>");
    data17.append("<a href='' onClick='javascript: window.open(\"acs-qared?NEXT_PAGE=PROOF_PANZOOM_IMAGE_POPUP&FILE_TO_DOWNLOAD=1641883_File000000_25839494.jpg&XIK_DOCU_ID=1641883&WIDTH=1600&HEIGHT=1152&XIK_CUR_ROLE_ID=65&XIK_CUR_USER_ID=0&XIK_CNFID=8\",\"the_inspector\",\"width=790,height=600,toolbar=no,location=no,directories=no,menubar=no,resizable=yes,scrollbars=yes\"); return false; '>ScholarOne Image Spy</a>");

    data18 = new StringBuffer();
    data18.append("http://qa1-test3.manuscriptcentral.com/LongRequest/qared?XIK_TAGACT=DOWNLOAD_FILE_BY_NAME&amp;XIK_DOCU_ID=17235306&amp;FILE_TO_DOWNLOAD=17235306_File000000_356160111.html-withlinks.htm&amp;FILE_KEY=-1329165952&amp;FILE_NAME_KEY=-1948735973&amp;DOWNLOAD=TRUE&amp;FILE_TYPE=DOCUMENT_BLINDED&amp;XIK_CUR_ROLE_ID=5805");

    data19 = new StringBuffer();
    data19.append("<li><a href=\"javascript:setField('XIK_SWITCH_CONFIG_ID','279');");
    
    data20 = new StringBuffer();
    data20.append("href=\"javascript:popWindow('http://worker4.scholarone.net:8000/LongRequest/prod4-qared?XIK_TAGACT=DOWNLOAD_FILE_BY_NAME&XIK_DOCU_ID=218872&FILE_TO_DOWNLOAD=review_details_linked.html&FILE_KEY=-2029505841&FILE_NAME_KEY=1693573439&DOWNLOAD=TRUE&FILE_TYPE=DOCUMENT&DOCUMENT_HASHCODE=397682070&XIK_SANITY_CHECK_DOCU_ID=218634&XIK_CUR_ROLE_ID=2','reviewer_view_details', 750, 550)\">View Review Details</a>");
    
    data21 = new StringBuffer();
    data21.append("<a href=\"http://worker1.scholarone.net:8000/LongRequest/sa-scielo?XIK_TAGACT=DOWNLOAD_FILE_BY_NAME&amp;XIK_DOCU_ID=561600&amp;FILE_TO_DOWNLOAD=sheet001.htm-withlinks.htm&amp;FILE_KEY=-1939211461&amp;FILE_NAME_KEY=-1025498525&amp;DOWNLOAD=TRUE&amp;FILE_TYPE=DOCUMENT\" target=\"frSheet\"><font face=\"Arial\" color=\"#000000\">Sheet1</font></a>");
    
    result1 = "http://mc-stage.manuscriptcentral.com/qared?XIK_CURRENT_QUEUE_TYPE=xik_DsPpTuKwiJUBVYGUQxMjdVsYwFmTtQ9VgfcXx6qXnPWD&XIK_CURRENT_QUEUE_VALUE=xik_A7vG8GM9NgwJUb8J3bZSJqU5tUNjFr9xME1tmMDCbF4h&PRE_ACTION=JUMP_TO_FIRST_MANUSCRIPT&NEXT_PAGE=MANUSCRIPT_DETAILS&CURRENT_PAGE=DASHBOARD&MS_LIST_TO_DISPLAY5809=14925&CURRENT_ROLE_ID=5809&CURRENT_USER_ID=1&DOCUMENT_HASHCODE=&XIK_CONFIG_ID=xik_EDfRHYQz4bLupvZ7tPfaKr7JADmjBFqUYwAhiPrui6Wm&CURRENT_GROUP_NAME=&CURRENT_GROUP_NAME_ID=&PAGE_NAME=DASHBOARD";
    result2 = "http://mc-stage.manuscriptcentral.com/qared?XIK_CURRENT_QUEUE_TYPE=xik_DsPpTuKwiJUBVYGUQxMjdVsYwFmTtQ9VgfcXx6qXnPWD&XIK_CURRENT_QUEUE_VALUE=xik_A7vG8GM9NgwJUb8J3bZSJqU5tUNjFr9xME1tmMDCbF4h&PRE_ACTION=JUMP_TO_FIRST_MANUSCRIPT&NEXT_PAGE=MANUSCRIPT_DETAILS&CURRENT_PAGE=DASHBOARD&MS_LIST_TO_DISPLAY5809=14925&CURRENT_ROLE_ID=5809&CURRENT_USER_ID=1&DOCUMENT_HASHCODE=&XIK_CONFIG_ID=xik_EDfRHYQz4bLupvZ7tPfaKr7JADmjBFqUYwAhiPrui6Wm&CURRENT_GROUP_NAME=&CURRENT_GROUP_NAME_ID=&PAGE_NAME=DASHBOARD";
    result3 = "http://qa1-test3.manuscriptcentral.com/LongRequest/qared?PARAMS=xik_mJcBBDbwVZxw9qq32C8EsQ18V2s7M26QP1V6MbyQWuhFurkKEDZ7jsxmh1oZCCEmgy8Hfav4uxDNrB7CQM16bVcg9oAWVYF3jkiybWGBjMQKCvtnBtrNRVdZEeLkUw82GYibcaw4yqpEWy8rb6mTAKbw5DLtJb26rKDktuA1RV2G42bD8sQCCwN5jDXDc6mDbyFBe9jrYfHPTpzjN4y7gNHd2t7d8fPqBqNAh8gj1fkw6Xaz7eusYQ4JXHdpPqmNLBx5zc";
    result4 = "<input type = \"hidden\" name =\"PORTAL\" value = \"null\"><input type = \"hidden\" name =\"S1LOGIN\" value = \"null\"><input type = \"hidden\" name =\"NEXT_PAGE\" value = \"\"><input type = \"hidden\" name =\"NEXT_PAGE_FOR_REDIRECT\" value = \"\"><input type = \"hidden\" name =\"XIK_CURRENT_PAGE\" value=\"xik_HPaxJAYmshc8AE7Fy55NXx7adfJ9Xhzo6HULcQ9FGEE2\"><input type = \"hidden\" name =\"PRE_ACTION\" value=\"\"><input type = \"hidden\" name =\"XIK_POSTACT\" value=\"\"><input type = \"hidden\" name =\"XIK_CURRENT_ROLE_ID\" value=\"xik_F1pHfUJDkwFSKEWdwVTg7tz8SNJru6CdRgJYNADRnsWM\"><input type = \"hidden\" name =\"SWITCH_CONFIG_ID\" value=\"\"><input type = \"hidden\" name =\"CHANGE_PORTAL_CONFIG_ID_FOR_LOGIN_PAGE\" value=\"\"><input type = \"hidden\" name =\"SWITCH_CONFIG_ID_PREV\" value=\"\"><input type = \"hidden\" name =\"NEW_USER_CONFIG_ID\" value=\"\"><input type = \"hidden\" name =\"PROXY_TO_PAGE_NAME\" value=\"\"><input type = \"hidden\" name =\"PROXY_TO_ROLE_ID\" value=\"\"><input type = \"hidden\" name =\"PROXY_TO_DOCUMENT_ID\" value=\"\"><input type = \"hidden\" name =\"PROXY_TO_DOCUMENT_TASK_ID\" value=\"\"><input type = \"hidden\" name =\"PAGE_DIRTY_FL\" value=\"\"><input type = \"hidden\" name =\"PAGE_LOADED_FLAG\" value=\"\"><input type = \"hidden\" name =\"DISPLAY\" value=\"\"><input type = \"hidden\" name =\"XIK_DOCUMENT_HASHCODE\" value=\"xik_EJQ78uEX46h9wk4CyGihQyf7v1htSi1H5Pz5k8t3NL6y\"><input type = \"hidden\" name =\"CONFIG_ID\" value=\"1392\">";
    result5 = "<input type = 'hidden' name ='PORTAL' value = 'null'><input type = 'hidden' name ='S1LOGIN' value = 'null'><input type = 'hidden' name ='NEXT_PAGE' value = ''><input type = 'hidden' name ='NEXT_PAGE_FOR_REDIRECT' value = ''><input type = 'hidden' name ='XIK_CURRENT_PAGE' value='xik_HPaxJAYmshc8AE7Fy55NXx7adfJ9Xhzo6HULcQ9FGEE2'><input type = 'hidden' name ='PRE_ACTION' value=''><input type = 'hidden' name ='XIK_POSTACT' value=''><input type = 'hidden' name ='XIK_CURRENT_ROLE_ID' value='xik_F1pHfUJDkwFSKEWdwVTg7tz8SNJru6CdRgJYNADRnsWM'><input type = 'hidden' name ='SWITCH_CONFIG_ID' value=''><input type = 'hidden' name ='CHANGE_PORTAL_CONFIG_ID_FOR_LOGIN_PAGE' value=''><input type = 'hidden' name ='SWITCH_CONFIG_ID_PREV' value=''><input type = 'hidden' name ='NEW_USER_CONFIG_ID' value=''><input type = 'hidden' name ='PROXY_TO_PAGE_NAME' value=''><input type = 'hidden' name ='PROXY_TO_ROLE_ID' value=''><input type = 'hidden' name ='PROXY_TO_DOCUMENT_ID' value=''><input type = 'hidden' name ='PROXY_TO_DOCUMENT_TASK_ID' value=''><input type = 'hidden' name ='PAGE_DIRTY_FL' value=''><input type = 'hidden' name ='PAGE_LOADED_FLAG' value=''><input type = 'hidden' name ='DISPLAY' value=''><input type = 'hidden' name ='XIK_SANITY_CHECK_DOCUMENT_ID' value='xik_9DRmz9mLxSZPpWiocC1kF3eaz6dLBR4s2J1hXGzzH9mW'><input type = 'hidden' name ='XIK_DOCUMENT_HASHCODE' value='xik_EJQ78uEX46h9wk4CyGihQyf7v1htSi1H5Pz5k8t3NL6y'><input type = 'hidden' name ='CONFIG_ID' value='1392'>";
    result6 = "<li><a href=\"javascript:setDataAndNextPage('PRE_ACTION','XIK_PERSON_EDIT_ACCOUNT','xik_9z6w9bfzHXzfoyJe6A7oeQ9nBrw5jtCWkeMe6Gm9bV1b')\">E-Mail / Name</a></li><li><a href=\"javascript:setDataAndNextPage('PRE_ACTION','PERSON_EDIT_ACCOUNT','EDIT_ACCOUNT_2')\">Address</a></li><li><a href=\"javascript:setDataAndNextPage('PRE_ACTION','XIK_PERSON_EDIT_ACCOUNT','xik_9z6w9bfzHXzfoyJe6A7oePzMYcSvmxrRvHHTbHDXWfeX')\">User ID & Password</a>setDataAndNextPage('PRE_ACTION','XIK_PERFORM_HEADER_QUICK_SEARCH','xik_HPaxJAYmshc8AE7Fy55NXx7adfJ9Xhzo6HULcQ9FGEE2');<TD nowrap valign=middle align=left><IMG SRC=\"/images/en_US/icons/no_previous_mss.gif\" BORDER=0  ALT=\"Previous Document\"></TD><TD nowrap valign=middle CLASS=\"pagecontents\" align=middle><P class=\"footer\">&nbsp;1 / 2&nbsp;</p></TD><TD nowrap valign=middle align=left><A HREF=\"javascript:setField('MANUSCRIPT_DETAILS_OVERRIDE_TASK_TAG','');setField('MANUSCRIPT_DETAILS_SHOW_TAB_LIST','T121956182');setField('XIK_TAGACT','xik_9nRxMepUf35V8aW3ZArQDkGLTof6kr1jw7mBtMXWfEc7HfhHCeZ1PjbCfyf6gbrJJA'); setField('CLEAR_SEARCH','Y'); setDataAndNextPage('XIK_NEXT_PREV_DOCUMENT_ID','xik_7yoXuPPH5PCKZiT8T92GsQETinB5bjUUXz2xyJkjQXCx','MANUSCRIPT_DETAILS');\"><IMG SRC=\"/images/en_US/icons/next_mss.gif\" BORDER=0 ALT=\"Next Document\"></a></TD>";
    result7 = "document.forms[0].XIK_PRE_ACTION.value='xik_JDc9t1JcFAzZPY9wX3q6ZUkuXi4TEusMnF5BvWewCXUL';onclick=\"javascript:document.forms[0].XIK_ATTRIBUTE_SEARCH_PREFIX.value='xik_8ieM6gz4AHk9bVdTCGfeidzfQfVWuSfEiFKDwbnZmXbX'\"";
    result8 = "<TD CLASS=\"tablelightcolor\"><input type=\"hidden\" name=\"XIK_ROLE_ID1\" value=\"xik_BLevm85QqeX3Q4ZtJyEyitaoeRnr9AAYncVtScZuUf3U\"><SELECT CLASS=pagecontents NAME=\"XIK_ROLE_TYPE_ID1\"><OPTION  VALUE=\"xik_DdPndTfkYtsYzwt7RSxSki\">Admin</OPTION><OPTION SELECTED VALUE=\"xik_5MfKNWzh2ANVNE7NWMisfM\">Author</OPTION><OPTION  VALUE=\"xik_FC3gFRcMuyahDZvCdV8Ti8\">Client Configuration</OPTION><OPTION  VALUE=\"xik_BeVedPk2xjknwioajpzw5v\">Editor</OPTION><OPTION  VALUE=\"xik_SCG4sbgWsX9fGJMAa25Q2M\">Editor In Chief</OPTION><OPTION  VALUE=\"xik_NsoGGZz7EdEJK3V9cd2CXA\">Production Editor</OPTION><OPTION  VALUE=\"xik_EnxpBZAZ1ExN4uLqScXAt1\">Reviewer</OPTION><OPTION  VALUE=\"xik_UCZyVtnTnyPSRFYdmVsLga\">Support</OPTION><OPTION  VALUE=\"xik_DXaL66P6rLZcHXuXY7doZu\">SysAdmin</OPTION><OPTION  VALUE=\"xik_B6eKjx4YzdDBc2xDTN4caD\">SysAdmin Read Only</OPTION></SELECT></TD><TD CLASS=\"tablelightcolor\"><input type=\"hidden\" name=\"ROLE_ID1\" value=\"23190\"><SELECT CLASS=pagecontents NAME=\"ROLE_TYPE_ID1\"><OPTION  VALUE=\"5\">Admin</OPTION><OPTION SELECTED VALUE=\"1\">Author</OPTION><OPTION  VALUE=\"11\">Client Configuration</OPTION><OPTION  VALUE=\"3\">Editor</OPTION><OPTION  VALUE=\"4\">Editor In Chief</OPTION><OPTION  VALUE=\"8\">Production Editor</OPTION><OPTION  VALUE=\"2\">Reviewer</OPTION><OPTION  VALUE=\"7\">Support</OPTION><OPTION  VALUE=\"6\">SysAdmin</OPTION><OPTION  VALUE=\"9\">SysAdmin Read Only</OPTION></SELECT></TD>";
    result9 = "<TD><SELECT CLASS='pagecontents' SIZE=1 NAME='XIK_CONTACT_AUTHOR_ID'><OPTION value='xik_5NHKyUmidtyphRNRzz5S2sz3MS2cJXtZzetitAZ5c1g5'  SELECTED >Dobrijevic, Davor </option></SELECT></TD>";
    result10 = "&XIK_TASK_REC_DEC_ID=' + trd5 + '&TRANSFER_TO=' + transferTo + '&DECISION_EMAIL_FORCE_REFRESH_FL=' + forceRefresh + '&XIK_EMAIL_TEMPLATE_ID=";
    result11 = "<select class=\"pagecontents\" name=\"XIK_PLAGIARISM_DOC_FILE_ID\" id=\"XIK_PLAGIARISM_DOC_FILE_ID\"><option value=\"xik_BVSGF9RYPLBRtGLUhxEu3G\">Select...</option></select>";
    result12 = "<td><select class=\"footer\" name=\"XIK_NEW_CONFIG_ID\"><option value=\"xik_BVSGF9RYPLBRtGLUhxEu3G\">Select...</option>    <option value = \"xik_T5QeVKvYxX3cHaJiM61dQB\">japr: Journal of A...</option></select></td>";
    result13 = "<input type=\"HIDDEN\" name=\"XIK_ZIP_ATTRIBUTE_TYPE_ID1000\" value=\"xik_9HuEKcexojVCn9Bcnab5ECvTBM2vXgQacz8LPwXwvNGD\">";
    result14 = "<td valign=top><input type=radio name=\"32637_XIK_CATEGORY_ID_295\" checked value=\"xik_X4aavc7aAqcs9CCcpPK9A8\"></td>";
    result15 = "<input type=text name=\"XIK_ASSIGNMENT_IDx90656624x0\" class=pagecontents value=\"xik_6UUzTWMsdA4y7f6813geLTNnt4eAhK8YtQXQFrntMP3e\" size=10 maxlength=10>";
    result16 = "<a href=\"#\" onclick=\"javascript:openOrcidWindow('http://sandbox.orcid.org/oauth/authorize?response_type=code&amp;client_id=0000-0002-4923-9632&amp;scope=%2Fauthenticate%20%2Forcid-bio%2Fread-limited%20%2Forcid-bio%2Fexternal-identifiers%2Fcreate&amp;redirect_uri=https%3A%2F%2Fqa1-test3.manuscriptcentral.com%2Fqa-auto-h0h%3FXIK_TAGACT%3Dxik_2PLo6JURkeMK9XzLonPVxxcPSHY4dzpB8cYqxzN9cnou','orcidwindow'); return false;\">here</a>";
    result17 = "'http://qa1-test3.manuscriptcentral.com/LongRequest/qared?PARAMS=xik_mJcBBDbwVZxw9qq32C8EsQ18V2s7M26QP1V6MbyQWuhFurkKEDZ7jsxmh1oZCCEmgy8Hfav4uxDNrB7CQM16bVcg9oAWVYF3jkiybWGBjMQKCvtnBtrNRVdZEeLkUw82GYibcaw4yqpEWy8rb6mTAKbw5DLtJb26rKDktuA1RV2G42bD8sQCCwN5jDXDc6mDbyFBe9jrYfHPTpzjN4y7gNHd2t7d8fPqBqNAh8gj1fkw6Xaz7eusYQ4JXHdpPqmNLBx5zc'<br><a href='' onClick='javascript: window.open(\"acs-qared?PARAMS=xik_5E3HkJTig5hQsnoBVoeYkvYV7eCavtkqWrkN1fDAVZWkNQUzJdiNAoKoHYBTZLvSPAymD1bp7S217ya7QnmC5AYtXGnvrxDvTXdc2LzxPERxTYqCJc43crFWJcY75AFqDm74pseve6sQy5qcodvXYJ9on8MTzdtsuJgC8SCCMs5AM8xz4t7fTFxSD7kY6G1GrbmWDtKScWxcEbV1238gUAPx3Zk\",\"the_inspector\",\"width=790,height=600,toolbar=no,location=no,directories=no,menubar=no,resizable=yes,scrollbars=yes\"); return false; '>ScholarOne Image Spy</a>";
    result18 = "http://qa1-test3.manuscriptcentral.com/LongRequest/qared?PARAMS=xik_mJcBBDbwVZxw9qq32C8EsQ18V2s7M26QP1V6MbyQWuhFurkKEDZ7jsxmh1oZCCEmgy8Hfav4uxDNrB7CQM16bVcg9oAWVYF3jkiybWGBjMQKCvtnBtrNRVdZEeLkUw82GYibcaw4yqpEWy8rb6mTAKbw5DLtJb26rKDktuA1RV2G42bD8sQCCwN5jDXDc6mDbyFBe9jrYfHPTpzjN4y7gNHd2t7d8fPqBqNAh8gj1fkw6Xaz7eusYQ4JXHdpPqmNLBx5zc";
    result19 = "<li><a href=\"javascript:setField('XIK_SWITCH_CONFIG_ID','xik_CRFKvBQS7wrjMsBnEL8gkkhrkqyrDTqLesFDxwGCJM2u');";
    result20 = "href=\"javascript:popWindow('http://worker4.scholarone.net:8000/LongRequest/prod4-qared?DOWNLOAD=TRUE&PARAMS=xik_2MD6fj7ShYZ44P8GqZHFHz2NKURxFRNTgXVNWuqp79M7BrWKsVWt4qbv2J1T2cZCbwC9MYqkS8vT8dhG145TCDA1aTwqqA7nwkFtzPZfKZYCTDcCrPEtyrKLZib3ruYULdtVXCEXwayZfY2t8937AaMXn5KsnyBbq8KS1XKD5fYTxZ6Us9CDtosArbmQKqP3BK6HVF6ySE332Qyb5ZvRqLa8b63AXAZGzVsnNb99BkGy5WXWExdiiGezvwYNQmaozTz3pLi','reviewer_view_details', 750, 550)\">View Review Details</a>";
    result21 = "<a href=\"http://worker1.scholarone.net:8000/LongRequest/sa-scielo?XIK_TAGACT=xik_5pFnNM9rU1KrTfkDxMKJwv1DmPEcQkWwnMAuwLN6ueiK2XnCU6J9kRSpPt1nKHhCvC&amp;XIK_DOCU_ID=561600&amp;FILE_TO_DOWNLOAD=sheet001.htm-withlinks.htm&amp;FILE_KEY=-1939211461&amp;FILE_NAME_KEY=-1025498525&amp;DOWNLOAD=TRUE&amp;FILE_TYPE=DOCUMENT\" target=\"frSheet\"><font face=\"Arial\" color=\"#000000\">Sheet1</font></a>";
  }

  @Test
  public void testEncryptSpecialParameters1() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data1.toString());
    System.out.println("1. " + data1.toString());
    System.out.println("1. " + newData);
    System.out.println("1. " + result1);
    Assert.assertTrue(newData.equals(result1));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("1. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters2() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data2.toString());
    System.out.println("2. " + data2.toString());
    System.out.println("2. " + newData);
    System.out.println("2. " + result2);
    Assert.assertTrue(newData.equals(result2));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("2. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }
  
  @Test
  public void testEncryptSpecialParameters4() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data4.toString());
    System.out.println("4. " + data4.toString());
    System.out.println("4. " + newData);
    System.out.println("4. " + result4);
    Assert.assertTrue(newData.equals(result4));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("4. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters5() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data5.toString());
    System.out.println("5. " + data5.toString());
    System.out.println("5. " + newData);
    System.out.println("5. " + result5);
    Assert.assertTrue(newData.equals(result5));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("5. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters6() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data6.toString());
    System.out.println("6. " + data6.toString());
    System.out.println("6. " + newData);
    System.out.println("6. " + result6);
    Assert.assertTrue(newData.equals(result6));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("6. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters7() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data7.toString());
    System.out.println("7. " + data7.toString());
    System.out.println("7. " + newData);
    System.out.println("7. " + result7);
    Assert.assertTrue(newData.equals(result7));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("7. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters8() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data8.toString());
    System.out.println("8. " + data8.toString());
    System.out.println("8. " + newData);
    System.out.println("8. " + result8);
    Assert.assertTrue(newData.equals(result8));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("8. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters9() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data9.toString());
    System.out.println("9. " + data9.toString());
    System.out.println("9. " + newData);
    System.out.println("9. " + result9);
    Assert.assertTrue(newData.equals(result9));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("9. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters10() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data10.toString());
    System.out.println("10. " + data10.toString());
    System.out.println("10. " + newData);
    System.out.println("10. " + result10);
    Assert.assertTrue(newData.equals(result10));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("10. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters11() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data11.toString());
    System.out.println("11. " + data11.toString());
    System.out.println("11. " + newData);
    System.out.println("11. " + result11);
    Assert.assertTrue(newData.equals(result11));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("11. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters12() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data12.toString());
    System.out.println("12. " + data12.toString());
    System.out.println("12. " + newData);
    System.out.println("12. " + result12);
    Assert.assertTrue(newData.equals(result12));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("12. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters13() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data13.toString());
    System.out.println("13. " + data13.toString());
    System.out.println("13. " + newData);
    System.out.println("13. " + result13);
    Assert.assertTrue(newData.equals(result13));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("13. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters14() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data14.toString());
    System.out.println("14. " + data14.toString());
    System.out.println("14. " + newData);
    System.out.println("14. " + result14);
    Assert.assertTrue(newData.equals(result14));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("14. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters15() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data15.toString());
    System.out.println("15. " + data15.toString());
    System.out.println("15. " + newData);
    System.out.println("15. " + result15);
    Assert.assertTrue(newData.equals(result15));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("15. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }

  @Test
  public void testEncryptSpecialParameters16() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data16.toString());
    System.out.println("16. " + data16.toString());
    System.out.println("16. " + newData);
    System.out.println("16. " + result16);
    Assert.assertTrue(newData.equals(result16));

    String newData1 = EncryptionUtility.searchAndEncrypt(salt, newData.toString());
    System.out.println("16. " + newData1);
    Assert.assertTrue(newData.contentEquals(newData1));
  }
  
  @Test
  public void testEncryptSpecialParameters17() throws ScholarOneException
  {
    String newData = EncryptionUtility.encryptEmbeddedURLs(data17.toString(), salt);
    System.out.println("17. " + data17.toString());
    System.out.println("17. " + newData);
    System.out.println("17. " + result17);
    Assert.assertTrue(newData.equals(result17));
  }
  
  @Test
  public void testEncryptSpecialParameters18() throws ScholarOneException
  {
    String newData = EncryptionUtility.encryptURL(data18.toString(), salt);
    System.out.println("18. " + data18.toString());
    System.out.println("18. " + newData);
    System.out.println("18. " + result18);
    Assert.assertTrue(newData.equals(result18));
  }
  
  @Test
  public void testEncryptSpecialParameters19() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data19.toString());
    System.out.println("19. " + data19.toString());
    System.out.println("19. " + newData);
    System.out.println("19. " + result19);
    Assert.assertTrue(newData.equals(result19));
  }
  
  @Test
  public void testEncryptSpecialParameters20() throws ScholarOneException
  {
    String newData = EncryptionUtility.encryptEmbeddedURLs(data20.toString(), salt);
    System.out.println("20. " + data20.toString());
    System.out.println("20. " + newData);
    System.out.println("20. " + result20);
    Assert.assertTrue(newData.equals(result20));
  }
  
  @Test
  public void testEncryptSpecialParameters21() throws ScholarOneException
  {
    String newData = EncryptionUtility.searchAndEncrypt(salt, data21.toString());
    System.out.println("21. " + data21.toString());
    System.out.println("21. " + newData);
    System.out.println("21. " + result21);
    Assert.assertTrue(newData.equals(result21));
  }
}