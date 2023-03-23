package com.api.automation.conversation.listconversationTests;

import static com.api.automation.constants.conversation.CreateConversationConstants.HREF;
import static io.restassured.http.Method.GET;

import com.api.automation.helpers.conversation.CommonConversationHelper;
import com.api.automation.helpers.conversation.ListConversationApiHelper;
import com.api.automation.pojos.response.conversation.ListConversationResponse;
import com.api.automation.utils.ApiRequestBuilder;
import com.api.automation.utils.RestAssuredUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Slf4j
@Listeners({io.qameta.allure.testng.AllureTestNg.class})
@Epic("API Automation")
@Feature("Conversation Api")
@Story("List Conversation Positive testCases")
public class ListConversationPositiveTests {

  private String jwtToken;
  public CommonConversationHelper commonConversationHelper;

  @BeforeClass(alwaysRun = true)
  public void setup() {
    jwtToken = CommonConversationHelper.generateJwtToken();
  }

  @Test(description = "PC-List conversation with no filter")
  public void listConversationNoFilter() {
    SoftAssert softAssert = new SoftAssert();
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 200, jwtToken, null);

    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ListConversationResponse response = listConversationResponse.as(ListConversationResponse.class);
    assertPositiveValidation(response, 10, 0);
  }

  @Test(description = "PC-List conversation with startDate filter")
  public void listConversationFilterByStartDate() {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    String date = commonConversationHelper.generateDate(2023, 1, 1);
    queryParams.put("date_start", date);
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 200, jwtToken, queryParams);
    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ListConversationResponse response = listConversationResponse.as(ListConversationResponse.class);
    assertPositiveValidation(response, 10, 0);
  }

  @Test(description = "PC-List conversation with endDate filter")
  public void listConversationFilterByEndDate() {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    String date = commonConversationHelper.generateDate(2023, 1, 1);
    queryParams.put("date_end", date);
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 200, jwtToken, queryParams);
    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ListConversationResponse response = listConversationResponse.as(ListConversationResponse.class);
    assertPositiveValidation(response, 10, 0);
  }

  @DataProvider(name = "listConversationPageValidDataProvider")
  public Object[][] listConversationPageValidDataProvider() {
    return new Object[][] {{1}, {100}, {99}};
  }

  @Test(
      description = "PC-List conversation with pageSize filter valid",
      dataProvider = "listConversationPageValidDataProvider")
  public void listConversationFilterByPageSizeValid(Object pageSize) {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();

    queryParams.put("page_size", pageSize);
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 200, jwtToken, queryParams);
    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ListConversationResponse response = listConversationResponse.as(ListConversationResponse.class);
    assertPositiveValidation(response, pageSize, 0);
  }

  @DataProvider(name = "listConversationFilterRecordValid")
  public Object[][] listConversationFilterRecordValid() {
    return new Object[][] {{0}, {1}, {100}, {10000}};
  }

  @Test(
      description = "PC-List conversation with record Index filter valid",
      dataProvider = "listConversationFilterRecordValid")
  public void listConversationFilterByRecordIndexValid(Object recordIndex) {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("record_index", recordIndex);
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 200, jwtToken, queryParams);

    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ListConversationResponse response = listConversationResponse.as(ListConversationResponse.class);
    assertPositiveValidation(response, 10, recordIndex);
  }

  @DataProvider(name = "listConversationFilterByOrderValid")
  public Object[][] listConversationFilterByOrderValid() {
    return new Object[][] {{"asc"}, {"ASC"}, {"desc"}, {"DESC"}};
  }

  @Test(
      description = "PC-List conversation with order filter valid",
      dataProvider = "listConversationFilterByOrderValid")
  public void listConversationFilterByOrderValid(String order) {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("order", order);
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 200, jwtToken, queryParams);
    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ListConversationResponse response = listConversationResponse.as(ListConversationResponse.class);
    assertPositiveValidation(response, 10, 0);
  }

  @Test(description = "PC-List conversation with all valid filter")
  public void listConversationFilterByAll() {
    SoftAssert softAssert = new SoftAssert();
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("date_end", commonConversationHelper.generateDate(2023, 1, 1));
    queryParams.put("date_start", commonConversationHelper.generateDate(2024, 1, 1));
    queryParams.put("page_size", 10);
    queryParams.put("record_index", 10);
    queryParams.put("order", "ASC");
    ApiRequestBuilder listConversationBuilder =
        ListConversationApiHelper.listConversationBuilder(GET, 200, jwtToken, queryParams);
    Response listConversationResponse = RestAssuredUtils.processApiRequest(listConversationBuilder);
    ListConversationResponse response = listConversationResponse.as(ListConversationResponse.class);
    assertPositiveValidation(
        response, queryParams.get("page_size"), queryParams.get("record_index"));
  }

  public void assertPositiveValidation(
      ListConversationResponse response, Object pageSize, Object recordIndex) {
    SoftAssert softAssert = new SoftAssert();

    softAssert.assertNotNull(response.getCount());
    softAssert.assertEquals(response.getPage_size(), pageSize);
    softAssert.assertEquals(response.getRecord_index(), recordIndex);
    softAssert.assertEquals(response.get_links().getSelf().getHref(), HREF.replaceAll("/$", ""));
    softAssert.assertNotNull(response.get_embedded().getConversations());
    softAssert.assertAll();
  }
}
