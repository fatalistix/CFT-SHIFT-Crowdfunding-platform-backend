package ru.cft.shift.intensive.balashov.crowdfunding.repositories.projects;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType
public record Transaction(String srcLogin, String dstProject, long value) {
}
