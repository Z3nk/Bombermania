package org;

public class InvalidMapFileException extends Exception
{
	private static final long serialVersionUID = 2340643046220245022L;

	public InvalidMapFileException()
    {
    }

    public InvalidMapFileException(String message)
    {
        super(message);
    }

    public InvalidMapFileException(Throwable cause)
    {
        super(cause);
    }

    public InvalidMapFileException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
