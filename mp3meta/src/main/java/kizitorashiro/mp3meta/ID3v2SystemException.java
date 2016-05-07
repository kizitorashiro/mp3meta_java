package kizitorashiro.mp3meta;

public class ID3v2SystemException extends ID3v2Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3303133657361817810L;
	public ID3v2SystemException(String message){
		super(message);
	}
	public ID3v2SystemException(String message, Exception cause){
		super(message, cause);
	}
}
