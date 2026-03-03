package paulodev.orderflowapi.esception;

public class ConflictException extends RuntimeException{

    public ConflictException(String message) {
        super(message);
    }
}
