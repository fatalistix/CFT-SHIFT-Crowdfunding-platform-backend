package ru.cft.shift.intensive.balashov.crowdfunding.repositories.projects;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("NameToUUID")
public record ProjectNameToUUIDEntity(@PrimaryKey String name, UUID uuid) {
}
