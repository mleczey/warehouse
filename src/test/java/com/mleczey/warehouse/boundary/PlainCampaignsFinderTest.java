package com.mleczey.warehouse.boundary;

import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.GroupBy;
import java.util.Set;
import javax.persistence.EntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class PlainCampaignsFinderTest {

  private final EntityManager NOT_RELEVANT = null;

  @Test
  void shouldAcceptNotGroupedByQueries() {
    // given
    final var campaignQuery = setUpEmptyCamapignQuery();
    final var testedObject = new PlainCampaignsFinder(NOT_RELEVANT);

    // when
    final var actual = testedObject.canProcess(campaignQuery);

    // then
    assertThat(actual).isTrue();
  }

  @Test
  void shouldNotAcceptGroupedByQueries() {
    // given
    final var campaignQuery = setUpGroupedCamapignQuery();
    final var testedObject = new PlainCampaignsFinder(NOT_RELEVANT);

    // when
    final var actual = testedObject.canProcess(campaignQuery);

    // then
    assertThat(actual).isFalse();
  }

  private CampaignQuery setUpGroupedCamapignQuery() {
    return CampaignQuery.builder()
        .groupBys(Set.of(GroupBy.CAMPAIGN))
        .build();
  }

  private CampaignQuery setUpEmptyCamapignQuery() {
    return CampaignQuery.builder()
        .build();
  }
}
