package ru.cft.shift.intensive.balashov.crowdfunding.repositories.projects;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CassandraProjectsRepository extends CassandraRepository<ProjectRepositoryEntity, UUID> {
}
