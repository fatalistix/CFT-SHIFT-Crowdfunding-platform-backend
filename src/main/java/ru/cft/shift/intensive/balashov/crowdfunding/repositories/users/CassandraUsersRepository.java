package ru.cft.shift.intensive.balashov.crowdfunding.repositories.users;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CassandraUsersRepository extends CassandraRepository<UserRepositoryEntity, String> {
    boolean existsByLoginAndPassword(String login, String password);
}
