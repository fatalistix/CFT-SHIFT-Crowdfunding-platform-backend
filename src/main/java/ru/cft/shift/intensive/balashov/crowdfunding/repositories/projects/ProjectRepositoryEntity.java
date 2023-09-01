package ru.cft.shift.intensive.balashov.crowdfunding.repositories.projects;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import ru.cft.shift.intensive.balashov.crowdfunding.enums.ProjectCategory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table("Projects")
public record ProjectRepositoryEntity(@PrimaryKey UUID id, String author, String projectName, String description, Long requiredAmount, Long collectedAmount, LocalDate donationDeadline, String videoLink, Set<Transaction> transactions, ProjectCategory category) {
    public ProjectRepositoryEntity(String author, String projectName, String description, Long requiredAmount, LocalDate donationDeadLine, String videoLink, ProjectCategory category) {
        this(UUID.randomUUID(), author, projectName, description, requiredAmount, 0L, donationDeadLine, videoLink, new HashSet<>(), category);
    }

    public ProjectRepositoryEntity(UUID uuid, String author, String projectName, String description, Long requiredAmount, LocalDate donationDeadLine, String videoLink, ProjectCategory category) {
        this(uuid, author, projectName, description, requiredAmount, 0L, donationDeadLine, videoLink, new HashSet<>(), category);
    }
}