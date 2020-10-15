package com.mleczey.warehouse.control;

import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.CampaignSearchResult;
import com.mleczey.warehouse.entity.Metric;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import static java.util.stream.Collectors.toMap;

class MetricsPicker {

  private static final Map<Metric, Function<CampaignSearchResult, Object>> MAPPINGS = new EnumMap<>(Metric.class);

  static {
    MAPPINGS.put(Metric.CLICKS, CampaignSearchResult::calculateClicks);
    MAPPINGS.put(Metric.CLICK_THROUGH_RATE, CampaignSearchResult::calculateClickThroughRate);
    MAPPINGS.put(Metric.IMPRESSIONS, CampaignSearchResult::calculateImpressons);
  }

  private final Map<Metric, Function<CampaignSearchResult, Object>> activeMetrics;

  MetricsPicker(final CampaignQuery campaignQuery) {
    activeMetrics = new EnumMap<>(MAPPINGS);

    campaignQuery.withMetrics(metrics -> activeMetrics.keySet().retainAll(metrics));
  }

  Map<String, Object> pickFrom(final CampaignSearchResult result) {
    return activeMetrics.keySet().stream()
        .collect(toMap(Metric::toString, metric -> activeMetrics.get(metric).apply(result)));
  }
}
