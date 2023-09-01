package ru.cft.shift.intensive.balashov.crowdfunding.repositories.promos;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "Promo")
public record PromoRepositoryEntity(@PrimaryKey String code, Long money) {
}
