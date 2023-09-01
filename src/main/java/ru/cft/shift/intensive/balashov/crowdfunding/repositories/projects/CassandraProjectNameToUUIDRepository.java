package ru.cft.shift.intensive.balashov.crowdfunding.repositories.projects;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CassandraProjectNameToUUIDRepository extends CassandraRepository<ProjectNameToUUIDEntity, String> {
}
