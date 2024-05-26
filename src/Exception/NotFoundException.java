package Exception;
import java.lang.constant.ConstantDesc;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
