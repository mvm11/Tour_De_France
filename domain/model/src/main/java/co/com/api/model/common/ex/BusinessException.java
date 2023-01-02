package co.com.api.model.common.ex;

import java.util.function.Supplier;

public class BusinessException extends ApplicationException {

    public enum Type {

        ERROR_GETTING_All_cyclist("Error getting cyclist in MongoDB."),
        ERROR_GETTING_All_TEAMS("Error getting teams in MongoDB."),

        ERROR_TEAM("An error occurred while creating the record due to ID duplicates."),
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
        CYCLIST_LIST_CYCLIST_DISTINCT_TEAM_NUMBER("There are cyclist with a different team number");


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

