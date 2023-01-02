package co.com.api.model.common.ex;

import java.util.function.Supplier;

public class NotFoundException  extends ApplicationException {

    public enum Type {

        TEAMS_NOT_FOUND_BY_COUNTRY("Teams haven't been found with country: ");

        private final String message;

        public String getMessage() {
            return message;
        }

        public NotFoundException build(String errorInfo) {
            return new NotFoundException(this, errorInfo);
        }

        public Supplier<Throwable> defer() {
            return () -> new NotFoundException(this, "");
        }

        Type(String message) {
            this.message = message;
        }

    }

    private final NotFoundException.Type type;

    public NotFoundException(NotFoundException.Type type, String exceptionInfo) {
        super(type.message + " " + exceptionInfo);
        this.type = type;
    }

    @Override
    public String getCode() {
        return type.name();
    }

    public NotFoundException.Type getType() {
        return type;
    }

}