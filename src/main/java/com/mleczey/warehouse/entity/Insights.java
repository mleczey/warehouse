package com.mleczey.warehouse.entity;

import java.util.Collection;
import static java.util.Collections.unmodifiableCollection;
import java.util.HashSet;
import lombok.Value;

@Value
public class Insights {

  private final Collection<Insight> insights;

  public Insights() {
    insights = new HashSet<>();
  }

  public Insights(final Collection<Insight> insights) {
    this();
    this.insights.addAll(insights);
  }

  public Collection<Insight> getInsights() {
    return unmodifiableCollection(insights);
  }

  public Insights add(final Insight insight) {
    insights.add(insight);
    return this;
  }

  public Insights addAll(final Insights other) {
    insights.addAll(other.getInsights());
    return this;
  }
}
