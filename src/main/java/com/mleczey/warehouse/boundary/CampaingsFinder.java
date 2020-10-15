package com.mleczey.warehouse.boundary;

import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.CampaignSearchResult;
import java.util.Collection;

interface CampaingsFinder {

  boolean canProcess(final CampaignQuery campaignQuery);

  Collection<CampaignSearchResult> findUsing(final CampaignQuery campaignQuery);
}
