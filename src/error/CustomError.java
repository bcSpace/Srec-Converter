package error;

public class CustomError extends Exception {

	//simple error
	
    public CustomError() {}

    public CustomError(String message) {
       super(message);
    }
	
}
