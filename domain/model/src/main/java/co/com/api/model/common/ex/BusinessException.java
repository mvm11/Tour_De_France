package co.com.api.model.common.ex;

import java.util.function.Supplier;

public class BusinessException extends ApplicationException {

    public enum Type {

        ERROR_GETTING_All_cyclist("Error getting cyclist in MongoDB."),
        DUPLICATE_CYCLIST_NUMBER("there cannot exist cyclists with the same number"),
        INCOMPLETE_INFORMATION("Incomplete cyclist information\""),
        ERROR_CYCLIST("An error occurred while creating the record due to ID duplicates.");

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

