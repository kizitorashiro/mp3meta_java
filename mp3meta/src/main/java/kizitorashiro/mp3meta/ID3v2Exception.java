package kizitorashiro.mp3meta;

public class ID3v2Exception extends Exception{
	private static final long serialVersionUID = 2775680365304172808L;
	public ID3v2Exception(String message){
		super(message);
	}
	public ID3v2Exception(String message, Exception cause){
		super(message, cause);
	}
}
