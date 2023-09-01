package ru.cft.shift.intensive.balashov.crowdfunding.repositories.projects;

import org.springframework.data.cassandra.repository.CassandraRepository;
import ru.cft.shift.intensive.balashov.crowdfunding.enums.ProjectCategory;

public interface CassandraProjectCategoryToUUIDSet extends CassandraRepository<ProjectCategoryToUUIDSetEntity, ProjectCategory> {
}
