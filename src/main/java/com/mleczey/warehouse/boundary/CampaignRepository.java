package com.mleczey.warehouse.boundary;

import com.mleczey.warehouse.control.CampaignDataAccess;
import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.CampaignSearchResult;
import java.util.Collection;
import java.util.Collections;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
class CampaignRepository implements CampaignDataAccess {

  @Any
  @Inject
  Instance<CampaingsFinder> campaingsFinders;

  @Override
  public Collection<CampaignSearchResult> find(final CampaignQuery campaignQuery) {
    return campaingsFinders.stream()
        .filter(finder -> finder.canProcess(campaignQuery))
        .findFirst()
        .map(finder -> finder.findUsing(campaignQuery))
        .orElseGet(Collections::emptySet);
  }
}
