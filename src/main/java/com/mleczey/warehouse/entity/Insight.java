package com.mleczey.warehouse.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.Value;

@Value
public class Insight {

  private final Set<String> datasources;

  private final Set<String> campaigns;

  private final LocalDate fromDate;

  private final LocalDate toDate;

  private final Map<String, Object> metrics;

  private Insight(final CampaignSearchResult searchResult, final Map<String, Object> metrics) {
    datasources = new HashSet<>(searchResult.getDatasources());
    campaigns = new HashSet<>(searchResult.getCampaings());
    fromDate = searchResult.getFromDate().orElse(null);
    toDate = searchResult.getToDate().orElse(null);
    this.metrics = new HashMap<>(metrics);
  }

  public static Insight from(final CampaignSearchResult searchResult, final Map<String, Object> metrics) {
    return new Insight(Objects.requireNonNull(searchResult), Objects.requireNonNull(metrics));
  }
}
