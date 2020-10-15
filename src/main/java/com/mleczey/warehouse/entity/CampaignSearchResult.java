package com.mleczey.warehouse.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface CampaignSearchResult {

  Set<String> getDatasources();

  Set<String> getCampaings();

  Optional<LocalDate> getFromDate();

  Optional<LocalDate> getToDate();

  long calculateClicks();

  long calculateImpressons();

  default BigDecimal calculateClickThroughRate() {
    final var impressions = calculateImpressons();
    return 0L == impressions
        ? BigDecimal.ZERO
        : BigDecimal.valueOf(calculateClicks())
            .divide(BigDecimal.valueOf(impressions), 4, RoundingMode.HALF_DOWN);
  }
}
