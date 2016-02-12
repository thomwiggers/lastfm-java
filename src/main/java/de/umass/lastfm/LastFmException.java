package de.umass.lastfm;

public class LastFmException extends RuntimeException {
	private static final long serialVersionUID = 6429978222424979419L;

	public static final int INVALID_SERVICE = 2;
	public static final int INVALID_METHOD = 3;
	public static final int AUTHENTICATOIN_FAILED = 4;
	public static final int INVALID_FORMAT = 5;
	public static final int INVALID_PARAMETERS = 6;
	public static final int INVALID_RESOURCE = 7;
	public static final int OPERATION_FAILED = 8;
	public static final int INVALID_SESSION_KEY = 9;
	public static final int INVALID_API_KEY = 10;
	public static final int SERVICE_OFFLINE = 11;
	public static final int INVALID_METHOD_SIGNATURE = 13;
	public static final int TEMPORARY_ERROR = 16;
	public static final int SUSPENDED_API_KEY = 26;
	public static final int RATE_LIMIT_EXCEEDED = 29;

	private int code;

	public LastFmException(int code, String message) {
		super(message);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
