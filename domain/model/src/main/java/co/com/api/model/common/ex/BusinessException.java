package co.com.api.model.common.ex;

import java.util.function.Supplier;

public class BusinessException extends ApplicationException {

    public enum Type {

        ERROR_GETTING_All_cyclist("Error getting cyclist in MongoDB."),
        TEAMS_NOT_FOUND("Teams not found"),
        CYCLIST_WITH_CYCLIST_NUMBER_MAYOR_THAN_3("There are cyclist with a cyclist number mayor than 3 digits."),
        DUPLICATE_CYCLIST_NUMBER("there cannot exist cyclists with the same number"),
        INCOMPLETE_CYCLIST_INFORMATION("Incomplete cyclist information"),
        INCOMPLETE_TEAM_INFORMATION("Incomplete team information"),
        NO_CYCLISTS_TEAM_NUMBER("There are no cyclists with that team number"),
        ERROR_CYCLIST("An error occurred while creating the record due to ID duplicates."),
        TEAM_CODE_EXCEPTION("A team only can have a team number with 3 characters"),
        DUPLICATE_TEAM_NUMBER("there cannot exist teams with the same number"),
        DUPLICATE_TEAM_NAME("there cannot exist teams with the same name"),
        CYCLIST_LIST("A team only can have 8 cyclist"),
        CYCLIST_LIST_CYCLIST_NUMBER_DUPLICATE("A team can only 8 cyclist with different number"),
        CYCLIST_LIST_CYCLIST_DISTINCT_TEAM_NUMBER("There are cyclist with a different team number"),
        CYCLIST_LIST_WITH_EMPTY_FIELDS("There are cyclist with empty fields"),

        CYCLIST_WITH_EMPTY_FIELDS("The cyclist has empty fields"),
        TEAM_NOT_FOUND("Team not found"),
        TEAM_NOT_FOUND_BY_TEAM_CODE("Team hasn't been found with team code: "),
        TEAM_MAX_CYCLISTS("The team already has 8 cyclist"),
        CYCLIST_WITH_DIFFERENT_TEAM_CODE("The cyclist has a different team code"),
        CYCLIST_WITH_SAME_CYCLIST_NUMBER("the team has a cyclist with the same code as the cyclist you are trying to save");


        private final String message;

        public String getMessage() {
            return message;
        }

        public BusinessException build(String errorInfo) {
            return new BusinessException(this, errorInfo);
        }

        public Supplier<Throwable> defer() {
            return () -> new BusinessException(this, "");
        }

        Type(String message) {
            this.message = message;
        }

    }

    private final Type type;

    public BusinessException(Type type, String exceptionInfo) {
        super(type.message + " " + exceptionInfo);
        this.type = type;
    }

    @Override
    public String getCode() {
        return type.name();
    }

    public Type getType() {
        return type;
    }


}

