package com.mleczey.warehouse.boundary;

import com.mleczey.warehouse.entity.Campaign;
import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.CampaignSearchResult;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;

@ApplicationScoped
class PlainCampaignsFinder implements CampaingsFinder {

  /**
   * Number higher because of hash map load factor.
   */
  private static final int MAX_NUMBER_OF_PREDICATES = 8;

  private final EntityManager em;

  @Inject
  PlainCampaignsFinder(final EntityManager em) {
    this.em = em;
  }

  @Override
  public boolean canProcess(final CampaignQuery campaignQuery) {
    return !campaignQuery.hasGroupBys();
  }

  @Override
  public Collection<CampaignSearchResult> findUsing(final CampaignQuery campaignQuery) {
    final var builder = em.getCriteriaBuilder();
    final var query = builder.createQuery(Campaign.class);
    final var campaign = query.from(Campaign.class);
    final var predicates = new HashSet<Predicate>(MAX_NUMBER_OF_PREDICATES);

    campaignQuery.withDatasources(datasources -> predicates.add(campaign.get("datasource").in(datasources)));
    campaignQuery.withCampaigns(campaigns -> predicates.add(campaign.get("campaign").in(campaigns)));
    campaignQuery.withFromDate(fromDate -> predicates.add(builder.greaterThan(campaign.get("day"), fromDate)));
    campaignQuery.withToDate(toDate -> predicates.add(builder.lessThanOrEqualTo(campaign.get("day"), toDate)));

    query.select(campaign)
        .where(predicates.toArray(new Predicate[predicates.size()]));

    return Collections.<CampaignSearchResult>unmodifiableList(em.createQuery(query).getResultList());
  }
}
