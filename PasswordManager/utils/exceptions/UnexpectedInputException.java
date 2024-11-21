package utils.exceptions;

public class UnexpectedInputException extends FileFormatException {
	
	public UnexpectedInputException(String expected, String found) {
		super("ERROR: '" + expected + "' expected, '" + found + "' found instead");
	}
}
