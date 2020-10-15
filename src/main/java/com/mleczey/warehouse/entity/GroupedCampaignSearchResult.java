package com.mleczey.warehouse.entity;

import java.time.LocalDate;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Builder
@ToString
@Value
public class GroupedCampaignSearchResult implements CampaignSearchResult {

  @Builder.Default
  private final Set<String> datasources = emptySet();

  @Builder.Default
  private final Set<String> campaigns = emptySet();

  private final LocalDate fromDate;

  private final LocalDate toDate;

  private final long clicks;

  private final long impressions;

  @Override
  public Set<String> getDatasources() {
    return unmodifiableSet(datasources);
  }

  @Override
  public Set<String> getCampaings() {
    return unmodifiableSet(campaigns);
  }

  @Override
  public Optional<LocalDate> getFromDate() {
    return Optional.ofNullable(fromDate);
  }

  @Override
  public Optional<LocalDate> getToDate() {
    return Optional.ofNullable(toDate);
  }

  @Override
  public long calculateClicks() {
    return clicks;
  }

  @Override
  public long calculateImpressons() {
    return impressions;
  }
}
