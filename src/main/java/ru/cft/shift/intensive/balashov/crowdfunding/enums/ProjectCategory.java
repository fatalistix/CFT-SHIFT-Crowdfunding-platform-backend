package ru.cft.shift.intensive.balashov.crowdfunding.enums;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import org.springframework.web.bind.annotation.RequestParam;

@UserDefinedType
public enum ProjectCategory {
    GAMES,
    SCIENCE,
    BUSINESS,
    MOVIE,
    CHARITY
}
