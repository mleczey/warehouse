package com.mleczey.warehouse.control;

import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.Insight;
import com.mleczey.warehouse.entity.Insights;
import io.quarkus.cache.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Timed;

@ApplicationScoped
class CampaignFacade implements CampaignService {

  private final CampaignDataAccess campaignDataAccess;

  @Inject
  CampaignFacade(final CampaignDataAccess campaignDataAccess) {
    this.campaignDataAccess = campaignDataAccess;
  }

  /**
   * No eviction strategy is required - data is read only.
   *
   * @param query - information about filters and grouping
   * @return insights
   */
  @CacheResult(cacheName = "insights-cache")
  @Override
  @Timed(name = "find-campaigns-timed", unit = MetricUnits.MILLISECONDS)
  @Transactional
  public Insights findInsights(final CampaignQuery query) {
    final var metricsPicker = new MetricsPicker(query);
    return campaignDataAccess.find(query)
        .stream()
        .map(searchResult -> Insight.from(searchResult, metricsPicker.pickFrom(searchResult)))
        .reduce(new Insights(), Insights::add, Insights::addAll);
  }
}
