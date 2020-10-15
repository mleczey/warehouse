package com.mleczey.warehouse.entity;

import java.time.LocalDate;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class CampaignQueryTest {

  @Test
  void shouldNotExecuteWhenNoMetrics() {
    // given
    final var testedObject = setUpEmptyQuery();

    // when & then
    testedObject.withMetrics(notRelevant -> fail());
  }

  @Test
  void shouldExecuteWhenMetrics() {
    // given
    final var testedObject = CampaignQuery.builder()
        .metrics(Set.of(Metric.CLICKS))
        .build();

    // when & then
    testedObject.withMetrics(metrics -> assertThat(metrics).contains(Metric.CLICKS));
  }

  @Test
  void shouldNotExecuteWhenNoGroupBys() {
    // given
    final var testedObject = setUpEmptyQuery();

    // when & then
    testedObject.withGroupBys(notRelevant -> fail());
  }

  @Test
  void shouldExecuteWhenGroupBys() {
    // given
    final var testedObject = CampaignQuery.builder()
        .groupBys(Set.of(GroupBy.CAMPAIGN))
        .build();

    // when & then
    testedObject.withGroupBys(groupBys -> assertThat(groupBys).contains(GroupBy.CAMPAIGN));
  }

  @Test
  void shouldNotExecuteWhenNoDatasources() {
    // given
    final var testedObject = setUpEmptyQuery();

    // when & then
    testedObject.withDatasources(notRelevant -> fail());
  }

  @Test
  void shouldExecuteWhenDatasources() {
    // given
    final var datasource = "datasource";

    final var testedObject = CampaignQuery.builder()
        .datasources(Set.of(datasource))
        .build();

    // when & then
    testedObject.withDatasources(datasources -> assertThat(datasources).contains(datasource));
  }

  @Test
  void shouldNotExecuteWhenNoCampaigns() {
    // given
    final var testedObject = setUpEmptyQuery();

    // when & then
    testedObject.withCampaigns(notRelevant -> fail());
  }

  @Test
  void shouldExecuteWhenCampaigns() {
    // given
    final var campaign = "campaign";

    final var testedObject = CampaignQuery.builder()
        .campaigns(Set.of(campaign))
        .build();

    // when & then
    testedObject.withCampaigns(campaigns -> assertThat(campaigns).contains(campaign));
  }

  @Test
  void shouldNotExecuteWhenNoFromDate() {
    // given
    final var testedObject = setUpEmptyQuery();

    // when & then
    testedObject.withFromDate(notRelevant -> fail());
  }

  @Test
  void shouldExecuteWhenFromDate() {
    // given
    final var fromDate = LocalDate.now();

    final var testedObject = CampaignQuery.builder()
        .fromDate(fromDate)
        .build();

    // when & then
    testedObject.withFromDate(date -> assertThat(date).isEqualTo(fromDate));
  }

  @Test
  void shouldNotExecuteWhenNoToDate() {
    // given
    final var testedObject = setUpEmptyQuery();

    // when & then
    testedObject.withToDate(notRelevant -> fail());
  }

  @Test
  void shouldExecuteWhenToDate() {
    // given
    final var toDate = LocalDate.now();

    final var testedObject = CampaignQuery.builder()
        .toDate(toDate)
        .build();

    // when & then
    testedObject.withToDate(date -> assertThat(date).isEqualTo(toDate));
  }

  private CampaignQuery setUpEmptyQuery() {
    return CampaignQuery.builder().build();
  }
}
