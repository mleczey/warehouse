package com.mleczey.warehouse.control;

import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.CampaignSearchResult;
import com.mleczey.warehouse.entity.Metric;
import static com.mleczey.warehouse.entity.Metric.CLICKS;
import static com.mleczey.warehouse.entity.Metric.CLICK_THROUGH_RATE;
import static com.mleczey.warehouse.entity.Metric.IMPRESSIONS;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class MetricsPickerTest {

  private static final long CLICKS_VALUE = 10L;

  private static final long IMPRESSIONS_VALUE = 20L;

  private static final BigDecimal CLICK_THROUGH_RATE_VALUE = BigDecimal.ONE;

  @Test
  void shouldPreserveAllMetricsIfNoneProvidedInQuery() {
    // given
    final var campaignQuery = setUpEmptyCampaignQuery();
    final var searchResult = setUpCampaignSearchResult();
    final var testedObject = new MetricsPicker(campaignQuery);

    // when
    final var actual = testedObject.pickFrom(searchResult);

    // then
    assertThat(actual).containsExactlyInAnyOrderEntriesOf(
        Map.of(
            CLICKS.name(), CLICKS_VALUE,
            CLICK_THROUGH_RATE.name(), CLICK_THROUGH_RATE_VALUE,
            IMPRESSIONS.name(), IMPRESSIONS_VALUE));
  }

  @MethodSource("metricsPickerProvider")
  @ParameterizedTest
  void shouldPreserveOnlyMetricsProvidedInQuery(final CampaignQuery campaignQuery, final Map<String, Object> expected) {
    // given
    final var searchResult = setUpCampaignSearchResult();
    final var testedObject = new MetricsPicker(campaignQuery);

    // when
    final var actual = testedObject.pickFrom(searchResult);

    // then
    assertThat(actual).containsExactlyInAnyOrderEntriesOf(expected);
  }

  static Stream<Arguments> metricsPickerProvider() {
    return Stream.of(
        arguments(
            setUpCampaignQueryWith(CLICKS),
            Map.of(CLICKS.name(), CLICKS_VALUE)),
        arguments(
            setUpCampaignQueryWith(CLICK_THROUGH_RATE),
            Map.of(CLICK_THROUGH_RATE.name(), CLICK_THROUGH_RATE_VALUE)),
        arguments(
            setUpCampaignQueryWith(IMPRESSIONS),
            Map.of(IMPRESSIONS.name(), IMPRESSIONS_VALUE)),
        arguments(
            setUpCampaignQueryWith(CLICKS, CLICK_THROUGH_RATE),
            Map.of(CLICKS.name(), CLICKS_VALUE, CLICK_THROUGH_RATE.name(), CLICK_THROUGH_RATE_VALUE)),
        arguments(
            setUpCampaignQueryWith(CLICKS, IMPRESSIONS),
            Map.of(CLICKS.name(), CLICKS_VALUE, IMPRESSIONS.name(), IMPRESSIONS_VALUE)),
        arguments(
            setUpCampaignQueryWith(CLICK_THROUGH_RATE, IMPRESSIONS),
            Map.of(CLICK_THROUGH_RATE.name(), CLICK_THROUGH_RATE_VALUE, IMPRESSIONS.name(), IMPRESSIONS_VALUE)),
        arguments(
            setUpCampaignQueryWith(CLICKS, CLICK_THROUGH_RATE, IMPRESSIONS),
            Map.of(CLICKS.name(), CLICKS_VALUE, CLICK_THROUGH_RATE.name(), CLICK_THROUGH_RATE_VALUE, IMPRESSIONS.name(), IMPRESSIONS_VALUE)));
  }

  private CampaignQuery setUpEmptyCampaignQuery() {
    return new CampaignQuery();
  }

  private static CampaignQuery setUpCampaignQueryWith(final Metric... metrics) {
    return CampaignQuery.builder()
        .metrics(Set.of(metrics))
        .build();
  }

  private CampaignSearchResult setUpCampaignSearchResult() {
    final var searchResult = mock(CampaignSearchResult.class);
    given(searchResult.calculateClicks()).willReturn(CLICKS_VALUE);
    given(searchResult.calculateImpressons()).willReturn(IMPRESSIONS_VALUE);
    given(searchResult.calculateClickThroughRate()).willReturn(CLICK_THROUGH_RATE_VALUE);
    return searchResult;
  }
}
