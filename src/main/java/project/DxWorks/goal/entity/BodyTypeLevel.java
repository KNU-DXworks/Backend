package project.DxWorks.goal.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BodyTypeLevel {
        BELOW_STANDARD("표준이하"), // 표준이하
        STANDARD("표준"), //표준
        ABOVE_STANDARD("표준이상");   //표준이상

    private final String description;

    BodyTypeLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    //문자열을 enum으로 변환
    @JsonCreator
    public static BodyTypeLevel fromString(String value) {
        for (BodyTypeLevel level : BodyTypeLevel.values()) {
            if (level.description.equals(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

}
