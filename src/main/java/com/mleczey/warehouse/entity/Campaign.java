package com.mleczey.warehouse.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "CAMPAIGN")
public class Campaign extends PanacheEntity implements CampaignSearchResult, Serializable {

  @Column(name = "DATASOURCE")
  private String datasource;

  @Column(name = "CAMPAIGN")
  private String campaign;

  @Column(name = "DAY")
  private LocalDate day;

  @Column(name = "CLICKS")
  private long clicks;

  @Column(name = "IMPRESSIONS")
  private long impressions;

  @Override
  public Set<String> getDatasources() {
    return Set.of(datasource);
  }

  @Override
  public Set<String> getCampaings() {
    return Set.of(campaign);
  }

  @Override
  public Optional<LocalDate> getFromDate() {
    return Optional.ofNullable(day);
  }

  @Override
  public Optional<LocalDate> getToDate() {
    return Optional.ofNullable(day);
  }

  @Override
  public long calculateClicks() {
    return clicks;
  }

  @Override
  public long calculateImpressons() {
    return impressions;
  }

  @Override
  public String toString() {
    return "Campaign{" + "datasource=" + datasource + ", campaign=" + campaign + ", day=" + day + ", clicks=" + clicks + ", impressions=" + impressions + '}';
  }

}
