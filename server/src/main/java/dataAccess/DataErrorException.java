package dataAccess;


public class DataErrorException extends Exception {
    private int errorCode;

    public DataErrorException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;

    }

    public int getErrorCode() {
        return errorCode;
    }
}
