package ru.cft.shift.intensive.balashov.crowdfunding.controller.jsons.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.cft.shift.intensive.balashov.crowdfunding.enums.ProjectCategory;

public record CreateProjectInComingJson(@JsonProperty("project_name") String projectName,
                                        String description,
                                        @JsonProperty("required_amount") Long requiredAmount,
                                        @JsonProperty("donation_deadline") String donationDeadline,
                                        @JsonProperty("video_link") String videoLink,
                                        ProjectCategory category) {

}
