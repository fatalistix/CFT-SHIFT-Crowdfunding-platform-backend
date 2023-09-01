package ru.cft.shift.intensive.balashov.crowdfunding.repositories.promos;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CassandraPromosRepository extends CassandraRepository<PromoRepositoryEntity, String> {
}
