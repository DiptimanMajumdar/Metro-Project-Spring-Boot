package com.metro.exceptions;

public class InvalidStationException extends Exception{
	
	@Override
	public String getMessage() {
		return "Station Doesn't Exist!";
	}
}
