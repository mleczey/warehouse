package com.mleczey.warehouse.entity;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class InsightTest {

  private static final String CAMPAIGN = "campaign";

  private static final String DATASOURCE_A = "datasource a";

  private static final String DATASOURCE_B = "datasource b";

  private static final LocalDate FROM = LocalDate.of(2000, Month.JANUARY, 1);

  private static final String METRIC_A = "metric-a";

  private static final String METRIC_B = "metric-b";

  private static final LocalDate TO = LocalDate.of(2000, Month.DECEMBER, 31);

  private static final String VALUE_A = "value a";

  private static final String VALUE_B = "value a";

  @Test
  void shouldCreateInsightFromCampaignSearchResultAndMetrics() {
    // given
    final var searchResult = setUpCampaignSearchResult();
    final var metrics = setUpMetrics();

    // when
    final var actual = Insight.from(searchResult, metrics);

    // then
    assertThat(actual.getDatasources()).containsExactlyInAnyOrder(DATASOURCE_A, DATASOURCE_B);
    assertThat(actual.getCampaigns()).containsExactlyInAnyOrder(CAMPAIGN);
    assertThat(actual.getFromDate()).isEqualTo(FROM);
    assertThat(actual.getToDate()).isEqualTo(TO);
    assertThat(actual.getMetrics()).containsExactlyInAnyOrderEntriesOf(Map.of(METRIC_A, VALUE_A, METRIC_B, VALUE_B));
  }

  @Test
  void shouldThrowExceptionWhenCampaignSearchResultnsNull() {
    // when
    Executable actual = () -> Insight.from(null, null);

    // then
    final var throwable = assertThrows(NullPointerException.class, actual);
  }

  @Test
  void shouldThrowExceptionWhenMetricsAreNull() {
    // given
    final var searchResult = setUpCampaignSearchResult();

    // when
    Executable actual = () -> Insight.from(searchResult, null);

    // then
    assertThrows(NullPointerException.class, actual);
  }

  private CampaignSearchResult setUpCampaignSearchResult() {
    final var searchResult = mock(CampaignSearchResult.class);
    given(searchResult.getDatasources()).willReturn(Set.of(DATASOURCE_A, DATASOURCE_B));
    given(searchResult.getCampaings()).willReturn(Set.of(CAMPAIGN));
    given(searchResult.getFromDate()).willReturn(Optional.of(FROM));
    given(searchResult.getToDate()).willReturn(Optional.of(TO));
    return searchResult;
  }

  private Map<String, Object> setUpMetrics() {
    return Map.of(METRIC_A, VALUE_A, METRIC_B, VALUE_B);
  }
}
