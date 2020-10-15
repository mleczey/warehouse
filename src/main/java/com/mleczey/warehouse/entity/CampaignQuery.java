package com.mleczey.warehouse.entity;

import java.time.LocalDate;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.ws.rs.QueryParam;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

@Builder
public class CampaignQuery {

  @Parameter(description = "Filters metrics that should be returned. No value means all metrics.")
  @QueryParam("metrics")
  private final Set<Metric> metrics;

  @Parameter(description = "List of elements, that can be grouped by. No value means no grouping.")
  @QueryParam("groupBys")
  private final Set<GroupBy> groupBys;

  @Parameter(description = "List of datasources that should be taken into consideration. No value means all datasources.")
  @QueryParam("datasources")
  private final Set<String> datasources;

  @Parameter(description = "List of campaigns that should be taken into consideration. No value means all campaigns.")
  @QueryParam("campaigns")
  private final Set<String> campaigns;

  @Parameter(description = "Left side of date range, exclusive.", example = "2020-12-31")
  @QueryParam("fromDate")
  private final LocalDate fromDate;

  @Parameter(description = "Right side of date range, inclusive.", example = "2020-12-31")
  @QueryParam("toDate")
  private final LocalDate toDate;

  public CampaignQuery() {
    this(emptySet(), emptySet(), emptySet(), emptySet(), null, null);
  }

  public CampaignQuery(final Set<Metric> metrics, final Set<GroupBy> groupBys, final Set<String> datasources, final Set<String> campaigns, final LocalDate fromDate, final LocalDate toDate) {
    this.metrics = null == metrics || metrics.isEmpty() ? emptySet() : EnumSet.copyOf(metrics);
    this.groupBys = null == groupBys || groupBys.isEmpty() ? emptySet() : EnumSet.copyOf(groupBys);
    this.datasources = null == datasources ? emptySet() : new HashSet<>(datasources);
    this.campaigns = null == campaigns ? emptySet() : new HashSet<>(campaigns);
    this.fromDate = fromDate;
    this.toDate = toDate;
  }

  public boolean hasGroupBys() {
    return !groupBys.isEmpty();
  }

  public boolean hasGroupBy(final GroupBy groupBy) {
    return groupBys.contains(groupBy);
  }

  public Stream<GroupBy> getGroupBys() {
    return groupBys.stream();
  }

  public void withMetrics(final Consumer<Set<Metric>> consumer) {
    if (!metrics.isEmpty()) {
      consumer.accept(unmodifiableSet(metrics));
    }
  }

  public void withGroupBys(final Consumer<Set<GroupBy>> consumer) {
    if (!groupBys.isEmpty()) {
      consumer.accept(unmodifiableSet(groupBys));
    }
  }

  public void withDatasources(final Consumer<Set<String>> consumer) {
    if (!datasources.isEmpty()) {
      consumer.accept(unmodifiableSet(datasources));
    }
  }

  public void withCampaigns(final Consumer<Set<String>> consumer) {
    if (!campaigns.isEmpty()) {
      consumer.accept(unmodifiableSet(campaigns));
    }
  }

  public void withFromDate(final Consumer<LocalDate> consumer) {
    if (null != fromDate) {
      consumer.accept(fromDate);
    }
  }

  public void withToDate(final Consumer<LocalDate> consumer) {
    if (null != toDate) {
      consumer.accept(toDate);
    }
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 79 * hash + Objects.hashCode(this.metrics);
    hash = 79 * hash + Objects.hashCode(this.groupBys);
    hash = 79 * hash + Objects.hashCode(this.datasources);
    hash = 79 * hash + Objects.hashCode(this.campaigns);
    hash = 79 * hash + Objects.hashCode(this.fromDate);
    hash = 79 * hash + Objects.hashCode(this.toDate);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CampaignQuery other = (CampaignQuery) obj;
    if (!Objects.equals(this.metrics, other.metrics)) {
      return false;
    }
    if (!Objects.equals(this.groupBys, other.groupBys)) {
      return false;
    }
    if (!Objects.equals(this.datasources, other.datasources)) {
      return false;
    }
    if (!Objects.equals(this.campaigns, other.campaigns)) {
      return false;
    }
    if (!Objects.equals(this.fromDate, other.fromDate)) {
      return false;
    }
    if (!Objects.equals(this.toDate, other.toDate)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CampaignQuery{" + "metrics=" + metrics + ", groupBys=" + groupBys + ", datasources=" + datasources + ", campaigns=" + campaigns + ", fromDate=" + fromDate + ", toDate=" + toDate + '}';
  }
}
