package com.mleczey.warehouse.boundary;

import com.mleczey.warehouse.control.CampaignService;
import com.mleczey.warehouse.entity.Campaign;
import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.GroupBy;
import com.mleczey.warehouse.entity.Insight;
import com.mleczey.warehouse.entity.Insights;
import com.mleczey.warehouse.entity.Metric;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import static io.restassured.RestAssured.given;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Set;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.BDDMockito;

@QuarkusTest
class CampaignResourceTest {

  private static final String CAMPAIGN = "campaign";

  private static final long CLICKS = 10L;

  private static final String DATASOURCE = "datasource";

  private static final LocalDate DAY = LocalDate.of(2000, Month.MARCH, 3);

  private static final long IMPRESSIONS = 10L;

  private static final String RESOURCE_ENDPOINT = "/campaigns";

  @InjectMock
  CampaignService campaignService;

  @Test
  void shouldReturnEmptyArrayOfInsightsIfNothingFound() {
    // given
    BDDMockito.given(campaignService.findInsights(any()))
        .willReturn(new Insights());

    // when & then
    given()
        .when().get(RESOURCE_ENDPOINT)
        .then()
        .statusCode(200)
        .body("insights.size()", equalTo(0));
  }

  @Test
  void shouldReturnBadRequestWhenWrongParameter() {
    // given
    BDDMockito.given(campaignService.findInsights(any()))
        .willReturn(new Insights());

    // when & then
    given()
        .param("metrics", "no-metric")
        .when().get(RESOURCE_ENDPOINT)
        .then()
        .statusCode(404);
  }

  @Test
  void shouldReturnResultsIfSomethingFound() {
    // given
    BDDMockito.given(campaignService.findInsights(setUpEmptyCampaignQuery()))
        .willReturn(new Insights(Set.of(setUpInsight())));

    // when & then
    given()
        .when().get(RESOURCE_ENDPOINT)
        .then()
        .statusCode(200)
        .body("insights.size()", equalTo(1))
        .body("insights[0].datasources", contains(DATASOURCE))
        .body("insights[0].campaigns", contains(CAMPAIGN))
        .body("insights[0].fromDate", equalTo(DAY.toString()))
        .body("insights[0].toDate", equalTo(DAY.toString()))
        .body("insights[0].metrics.clicks", equalTo(Math.toIntExact(CLICKS)))
        .body("insights[0].metrics.impressions", equalTo(Math.toIntExact(IMPRESSIONS)));
  }

  @Test
  void shouldReturnResultIfFiltersMatch() {
    // given
    BDDMockito.given(campaignService.findInsights(setUpCampaignQuery()))
        .willReturn(new Insights(Set.of(setUpInsight())));

    // when & then
    given()
        .param("metrics", Metric.CLICKS.name(), Metric.IMPRESSIONS.name())
        .param("datasources", DATASOURCE)
        .param("campaigns", CAMPAIGN)
        .param("fromDate", DAY.minusDays(1L).toString())
        .param("toDate", DAY.toString())
        .param("groupBys", GroupBy.DATASOURCE.name())
        .when().get(RESOURCE_ENDPOINT)
        .then()
        .statusCode(200)
        .body("insights.size()", equalTo(1))
        .body("insights[0].datasources", contains(DATASOURCE))
        .body("insights[0].campaigns", contains(CAMPAIGN))
        .body("insights[0].fromDate", equalTo(DAY.toString()))
        .body("insights[0].toDate", equalTo(DAY.toString()))
        .body("insights[0].metrics.clicks", equalTo(Math.toIntExact(CLICKS)))
        .body("insights[0].metrics.impressions", equalTo(Math.toIntExact(IMPRESSIONS)));
  }

  @Test
  void shouldReturnResultIfMoreThanOneResultMatches() {
    // given
    BDDMockito.given(campaignService.findInsights(setUpEmptyCampaignQuery()))
        .willReturn(new Insights(Set.of(setUpInsight("datasource-1"), setUpInsight("datasource-2"), setUpInsight("datasource-3"))));

    // when & then
    given()
        .when().get(RESOURCE_ENDPOINT)
        .then()
        .statusCode(200)
        .body("insights.size()", equalTo(3));
  }

  private CampaignQuery setUpEmptyCampaignQuery() {
    return new CampaignQuery();
  }

  private CampaignQuery setUpCampaignQuery() {
    return CampaignQuery.builder()
        .metrics(Set.of(Metric.CLICKS, Metric.IMPRESSIONS))
        .datasources(Set.of(DATASOURCE))
        .campaigns(Set.of(CAMPAIGN))
        .fromDate(DAY.minusDays(1L))
        .toDate(DAY)
        .groupBys(Set.of(GroupBy.DATASOURCE))
        .build();
  }

  private Insight setUpInsight() {
    return setUpInsight(DATASOURCE);
  }

  private Insight setUpInsight(final String datasource) {
    final var campaign = Campaign.builder()
        .datasource(datasource)
        .campaign(CAMPAIGN)
        .day(DAY)
        .clicks(CLICKS)
        .impressions(IMPRESSIONS)
        .build();
    final Map<String, Object> metrics = Map.of("clicks", CLICKS, "impressions", IMPRESSIONS);

    return Insight.from(campaign, metrics);
  }
}
