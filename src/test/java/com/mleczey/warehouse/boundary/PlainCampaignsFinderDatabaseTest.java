package com.mleczey.warehouse.boundary;

import com.mleczey.warehouse.MySqlTestResource;
import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.CampaignSearchResult;
import com.mleczey.warehouse.entity.GroupBy;
import com.mleczey.warehouse.entity.Metric;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(MySqlTestResource.class)
class PlainCampaignsFinderDatabaseTest {

  private static final LocalDate FIRST_OF_JANUARY = LocalDate.of(2019, Month.JANUARY, 1);

  private static final LocalDate SECOND_OF_JANUARY = LocalDate.of(2019, Month.JANUARY, 2);

  private static final int MAX_NUMBER_OF_RESULTS = 23197;

  @Inject
  PlainCampaignsFinder testedObject;

  @Test
  void shouldReturnAllEntitiesWithoutFilter() {
    // given
    final var query = CampaignQuery.builder()
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(MAX_NUMBER_OF_RESULTS);
  }

  @Test
  void shouldReturnSomeEntiresWithFromDateFilter() {
    // given
    final var fromDate = LocalDate.of(2020, Month.FEBRUARY, 13);

    final var query = CampaignQuery.builder()
        .fromDate(fromDate)
        .build();

    final var expected = Optional.of(fromDate.plusDays(1L));

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(31);
    assertThat(actual).extracting(CampaignSearchResult::getFromDate)
        .allMatch(date -> date.equals(expected));
  }

  @Test
  void shouldReturnSomeEntiresWtihToDateFilter() {
    // given
    final var query = CampaignQuery.builder()
        .toDate(FIRST_OF_JANUARY)
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(52);
    assertThat(actual).extracting(CampaignSearchResult::getFromDate)
        .allMatch(date -> date.equals(Optional.of(FIRST_OF_JANUARY)));
  }

  @Test
  void shouldReturnSomeEntiresWithFromAndToDateFilters() {
    // given
    final var query = CampaignQuery.builder()
        .fromDate(FIRST_OF_JANUARY)
        .toDate(SECOND_OF_JANUARY)
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(56);
    assertThat(actual).extracting(CampaignSearchResult::getFromDate)
        .allMatch(date -> date.equals(Optional.of(SECOND_OF_JANUARY)));
  }

  @Test
  void shouldReturnSomeEntiresWithAdditionalDatasourceFilter() {
    // given
    final var datasource = "Google Ads";

    final var query = CampaignQuery.builder()
        .datasources(Set.of(datasource))
        .fromDate(FIRST_OF_JANUARY)
        .toDate(SECOND_OF_JANUARY)
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(4);
    assertThat(actual).extracting(CampaignSearchResult::getDatasources)
        .allMatch(set -> containsOnly(set, datasource));
  }

  @Test
  void shouldReturnSomeEntiresWithAdditionalDatasourcesFilter() {
    // given
    final var datasources = Set.of("Google Ads", "Twitter Ads");

    final var query = CampaignQuery.builder()
        .datasources(datasources)
        .fromDate(FIRST_OF_JANUARY)
        .toDate(SECOND_OF_JANUARY)
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(37);
    assertThat(actual).extracting(CampaignSearchResult::getDatasources)
        .allMatch(set -> containsOnly(set, datasources));
  }

  @Test
  void shouldReturnSomeEntiresWithAdditionalCampaignFilter() {
    // given
    final var campaign = "Nothilfe";

    final var query = CampaignQuery.builder()
        .datasources(Set.of("Google Ads", "Twitter Ads"))
        .campaigns(Set.of(campaign))
        .fromDate(FIRST_OF_JANUARY)
        .toDate(SECOND_OF_JANUARY)
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(1);
    assertThat(actual).extracting(CampaignSearchResult::getCampaings)
        .allMatch(set -> containsOnly(set, campaign));
  }

  @Test
  void shouldReturnSomeEntiresWithGroupingAndMetricsFilteringNotAffectingResult() {
    // given
    final var query = CampaignQuery.builder()
        .metrics(Set.of(Metric.CLICKS))
        .groupBys(Set.of(GroupBy.CAMPAIGN))
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(MAX_NUMBER_OF_RESULTS);
  }

  private boolean containsOnly(final Set<String> set, final String expected) {
    final var copy = new HashSet<>(set);
    copy.remove(expected);
    return copy.isEmpty();
  }

  private boolean containsOnly(final Set<String> set, final Set<String> expected) {
    final var copy = new HashSet<>(set);
    copy.removeAll(expected);
    return copy.isEmpty();
  }

}
