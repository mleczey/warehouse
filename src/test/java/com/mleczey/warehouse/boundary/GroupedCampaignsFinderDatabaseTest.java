package com.mleczey.warehouse.boundary;

import com.mleczey.warehouse.MySqlTestResource;
import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.CampaignSearchResult;
import com.mleczey.warehouse.entity.GroupBy;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(MySqlTestResource.class)
class GroupedCampaignsFinderDatabaseTest {

  private static final int MAX_NUMBER_OF_RESULTS = 23197;

  @Inject
  GroupedCampaignsFinder testedObject;

  @Test
  void shouldGroupByDatasource() {
    // given
    final var query = CampaignQuery.builder()
        .groupBys(Set.of(GroupBy.DATASOURCE))
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(3);
    assertThat(actual).extracting(CampaignSearchResult::getCampaings)
        .allMatch(campaigns -> 1 < campaigns.size());

    final var allDatasources = actual.stream()
        .flatMap(result -> result.getDatasources().stream())
        .collect(toList());
    assertThat(allDatasources.size()).isEqualTo(actual.size());

  }

  @Test
  void shouldGroupByCampaign() {
    // given
    final var query = CampaignQuery.builder()
        .groupBys(Set.of(GroupBy.CAMPAIGN))
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(104);

    final var allCampaigns = actual.stream()
        .flatMap(result -> result.getCampaings().stream())
        .collect(toList());
    assertThat(allCampaigns.size()).isEqualTo(actual.size());
  }

  @Test
  void shouldGroupByDay() {
    // given
    final var query = CampaignQuery.builder()
        .groupBys(Set.of(GroupBy.DAY))
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(410);
  }

  @Test
  void shouldGroupByDatasourceAndCampaign() {
    // given
    final var query = CampaignQuery.builder()
        .groupBys(Set.of(GroupBy.DATASOURCE, GroupBy.CAMPAIGN))
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(185);
    assertThat(actual).extracting(CampaignSearchResult::getDatasources)
        .allMatch(datasources -> 1 == datasources.size());
    assertThat(actual).extracting(CampaignSearchResult::getCampaings)
        .allMatch(campaigns -> 1 == campaigns.size());
  }

  @Test
  void shouldGroupByDatasourceAndDay() {
    // given
    final var query = CampaignQuery.builder()
        .groupBys(Set.of(GroupBy.DATASOURCE, GroupBy.DAY))
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(1230);
    assertThat(actual).extracting(CampaignSearchResult::getDatasources)
        .allMatch(datasources -> 1 == datasources.size());
  }

  @Test
  void shouldGroupByCampaignAndDay() {
    // given
    final var query = CampaignQuery.builder()
        .groupBys(Set.of(GroupBy.CAMPAIGN, GroupBy.DAY))
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(13818);
    assertThat(actual).extracting(CampaignSearchResult::getCampaings)
        .allMatch(campaigns -> 1 == campaigns.size());
  }

  @Test
  void shouldGroupByCampaignAndDatasourceAndDay() {
    // given
    final var query = CampaignQuery.builder()
        .groupBys(Set.of(GroupBy.DATASOURCE, GroupBy.CAMPAIGN, GroupBy.DAY))
        .build();

    // when
    final var actual = testedObject.findUsing(query);

    // then
    assertThat(actual).hasSize(MAX_NUMBER_OF_RESULTS);
  }
}
