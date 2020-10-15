package com.mleczey.warehouse.control;

import com.mleczey.warehouse.entity.CampaignQuery;
import java.util.Collection;
import com.mleczey.warehouse.entity.CampaignSearchResult;

public interface CampaignDataAccess {

  Collection<CampaignSearchResult> find(final CampaignQuery campaignQuery);
}
