package paulodev.orderflowapi.esception;

public class ForbiddenOperationException extends RuntimeException{

    public ForbiddenOperationException(String message) {
        super(message);
    }
}
