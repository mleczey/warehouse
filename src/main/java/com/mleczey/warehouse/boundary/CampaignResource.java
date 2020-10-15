package com.mleczey.warehouse.boundary;

import com.mleczey.warehouse.control.CampaignService;
import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.Insights;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

@ApplicationScoped
@Path("/campaigns")
public class CampaignResource {

  private static final Logger logger = Logger.getLogger(CampaignResource.class);

  private final CampaignService campaignService;

  @Inject
  CampaignResource(CampaignService campaignService) {
    this.campaignService = campaignService;
  }

  @APIResponse(responseCode = "200", description = "When request is valid, even though no data may meet the requirements (filters).")
  @APIResponse(responseCode = "404", description = "When request is invalid, for example not existing metric is provided.")
  @GET
  @Operation(
      summary = "Get information about insights.",
      description = "Retrieves information about insights for given datasources, campaigns and date range.")
  @Produces(MediaType.APPLICATION_JSON)
  public Insights getInsights(@BeanParam final CampaignQuery query) {
    logger.infov("Looking for insights {0}", query);
    return campaignService.findInsights(query);
  }
}
