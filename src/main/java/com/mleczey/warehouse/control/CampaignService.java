package com.mleczey.warehouse.control;

import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.Insights;

public interface CampaignService {

  Insights findInsights(final CampaignQuery query);
}
