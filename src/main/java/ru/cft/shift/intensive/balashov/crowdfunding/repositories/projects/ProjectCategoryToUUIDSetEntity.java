package ru.cft.shift.intensive.balashov.crowdfunding.repositories.projects;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import ru.cft.shift.intensive.balashov.crowdfunding.enums.ProjectCategory;

import java.util.List;
import java.util.UUID;

@Table("ProjectTypeToUUIDSet")
public record ProjectCategoryToUUIDSetEntity(@PrimaryKey ProjectCategory category, List<UUID> uuid) {
}
