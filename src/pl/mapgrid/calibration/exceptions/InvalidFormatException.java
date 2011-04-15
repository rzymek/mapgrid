package pl.mapgrid.calibration.exceptions;

import pl.mapgrid.Messages;

public class InvalidFormatException extends RuntimeException {

	public InvalidFormatException(String expected, String got, int line) {
		super(expected+" != "+got+": "+line);
	}

	public InvalidFormatException(String string) {
		super(string);
	}

	public InvalidFormatException(Exception e, String line) {
		super(Messages.ERROR_READING_LINE+line, e);
	}

	public InvalidFormatException(String string, int lineNo) {
		super(string+": "+lineNo);
	}

}
