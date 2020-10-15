package com.mleczey.warehouse.entity;

import java.math.BigDecimal;
import static java.math.BigDecimal.ZERO;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CampaignTest {

  @MethodSource("calculateClickThroughRateProvider")
  @ParameterizedTest
  void shouldCalculateClickThroughRate(final long clicks, final long impressions, final BigDecimal expected) {
    // given
    final var testedObject = Campaign.builder()
        .clicks(clicks)
        .impressions(impressions)
        .build();

    // when
    final var actual = testedObject.calculateClickThroughRate();

    // then
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> calculateClickThroughRateProvider() {
    return Stream.of(
        arguments(0, 0, ZERO),
        arguments(10, 0, ZERO),
        arguments(10, 10, new BigDecimal("1.0000")),
        arguments(1, 50, new BigDecimal("0.0200")));
  }
}
