package ru.cft.shift.intensive.balashov.crowdfunding.enums;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType
public enum UserRole {
    USER,
    ADMIN
}
