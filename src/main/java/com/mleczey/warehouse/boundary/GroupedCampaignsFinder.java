package com.mleczey.warehouse.boundary;

import com.mleczey.database.tables.Campaign;
import com.mleczey.warehouse.entity.CampaignQuery;
import com.mleczey.warehouse.entity.CampaignSearchResult;
import com.mleczey.warehouse.entity.GroupBy;
import com.mleczey.warehouse.entity.GroupedCampaignSearchResult;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import org.jooq.Condition;
import org.jooq.GroupField;
import org.jooq.SQLDialect;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.impl.DSL;

@ApplicationScoped
class GroupedCampaignsFinder implements CampaingsFinder {

  private static final Map<GroupBy, GroupField> GROUP_BY_MAPPINGS = new EnumMap<>(GroupBy.class);

  private static final String CAMPAIGN_GROUP = "CAMPAIGN_GROUP";

  private static final String CLICKS_SUM = "CLICKS_SUM";

  private static final String DATASOURCE_GROUP = "DATASOURCE_GROUP";

  private static final String IMPRESSIONS_SUM = "IMPRESSIONS_SUM";

  private static final String MIN_DAY = "MIN_DAY";

  private static final String MAX_DAY = "MAX_DAY";

  /**
   * Number higher because of hash map load factor.
   */
  private static final int MAX_NUMBER_OF_CONDITIONS = 8;

  private static final String SEPARATOR = ",";

  private static final Pattern SEPRATOR_PATTERN = Pattern.compile(",");

  static {
    GROUP_BY_MAPPINGS.put(GroupBy.CAMPAIGN, Campaign.CAMPAIGN.CAMPAIGN_);
    GROUP_BY_MAPPINGS.put(GroupBy.DATASOURCE, Campaign.CAMPAIGN.DATASOURCE);
    GROUP_BY_MAPPINGS.put(GroupBy.DAY, Campaign.CAMPAIGN.DAY);
  }

  private final EntityManager em;

  @Inject
  GroupedCampaignsFinder(final EntityManager em) {
    this.em = em;
  }

  @Override
  public boolean canProcess(final CampaignQuery campaignQuery) {
    return campaignQuery.hasGroupBys();
  }

  @Override
  public Collection<CampaignSearchResult> findUsing(final CampaignQuery campaignQuery) {
    final var context = DSL.using(SQLDialect.MYSQL);

    final var query = context.select(prepareFields(campaignQuery))
        .from(Campaign.CAMPAIGN)
        .where(prepareConditions(campaignQuery))
        .groupBy(prepareGroupBy(campaignQuery));

    final var result = em.createNativeQuery(query.getSQL(), Tuple.class);
    bindValues(query, result);
    @SuppressWarnings("unchecked")
    final List<Tuple> list = result.getResultList();
    return Collections.<CampaignSearchResult>unmodifiableList(map(list));
  }

  private Set<SelectFieldOrAsterisk> prepareFields(final CampaignQuery campaignQuery) {
    final var fields = new HashSet<SelectFieldOrAsterisk>();
    if (campaignQuery.hasGroupBy(GroupBy.DATASOURCE)) {
      fields.add(Campaign.CAMPAIGN.DATASOURCE.as(DATASOURCE_GROUP));
    } else {
      fields.add(DSL.groupConcatDistinct(Campaign.CAMPAIGN.DATASOURCE).as(DATASOURCE_GROUP));
    }

    if (campaignQuery.hasGroupBy(GroupBy.CAMPAIGN)) {
      fields.add(Campaign.CAMPAIGN.CAMPAIGN_.as(CAMPAIGN_GROUP));
    } else {
      fields.add(DSL.groupConcatDistinct(Campaign.CAMPAIGN.CAMPAIGN_).as(CAMPAIGN_GROUP));
    }

    if (campaignQuery.hasGroupBy(GroupBy.DAY)) {
      fields.add(Campaign.CAMPAIGN.DAY.as(MIN_DAY));
      fields.add(Campaign.CAMPAIGN.DAY.as(MAX_DAY));
    } else {
      fields.add(DSL.min(Campaign.CAMPAIGN.DAY).as(MIN_DAY));
      fields.add(DSL.max(Campaign.CAMPAIGN.DAY).as(MAX_DAY));
    }

    fields.add(DSL.sum(Campaign.CAMPAIGN.CLICKS).as(CLICKS_SUM));
    fields.add(DSL.sum(Campaign.CAMPAIGN.IMPRESSIONS).as(IMPRESSIONS_SUM));
    return fields;
  }

  private Set<Condition> prepareConditions(final CampaignQuery campaignQuery) {
    final var conditions = new HashSet<Condition>(MAX_NUMBER_OF_CONDITIONS);
    campaignQuery.withDatasources(datasources -> conditions.add(Campaign.CAMPAIGN.DATASOURCE.in(datasources)));
    campaignQuery.withCampaigns(campaigns -> conditions.add(Campaign.CAMPAIGN.CAMPAIGN_.in(campaigns)));
    campaignQuery.withFromDate(fromDate -> conditions.add(Campaign.CAMPAIGN.DAY.greaterThan(fromDate)));
    campaignQuery.withToDate(toDate -> conditions.add(Campaign.CAMPAIGN.DAY.lessOrEqual(toDate)));
    return conditions;
  }

  private Set<GroupField> prepareGroupBy(final CampaignQuery campaignQuery) {
    return campaignQuery.getGroupBys()
        .map(GROUP_BY_MAPPINGS::get)
        .collect(toSet());
  }

  private void bindValues(final org.jooq.Query query, final Query result) {
    List<Object> values = query.getBindValues();
    for (int i = 0; i < values.size(); i++) {
      result.setParameter(i + 1, values.get(i));
    }
  }

  private List<GroupedCampaignSearchResult> map(final List<Tuple> tuples) {
    return tuples.stream()
        .map(this::map)
        .collect(toList());
  }

  private GroupedCampaignSearchResult map(final Tuple tuple) {
    return GroupedCampaignSearchResult.builder()
        .datasources(split(tuple.get(DATASOURCE_GROUP, String.class)))
        .campaigns(split((tuple.get(CAMPAIGN_GROUP, String.class))))
        .fromDate(tuple.get(MIN_DAY, Date.class).toLocalDate())
        .toDate(tuple.get(MAX_DAY, Date.class).toLocalDate())
        .clicks(tuple.get(CLICKS_SUM, BigDecimal.class).longValue())
        .impressions(tuple.get(IMPRESSIONS_SUM, BigDecimal.class).longValue())
        .build();
  }

  private Set<String> split(final String s) {
    return SEPRATOR_PATTERN.compile(",")
        .splitAsStream(s)
        .collect(toSet());
  }
}
