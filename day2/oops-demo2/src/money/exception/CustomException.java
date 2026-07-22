package money.exception;
// unchecked (Runtime) exception representing a custom error in the money domain.
// checked exceptions inherit from Exception, while unchecked exceptions inherit from RuntimeException.
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
    
}
