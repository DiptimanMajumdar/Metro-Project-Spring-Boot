package com.metro.exceptions;

public class InsufficientBalance extends Exception{

	@Override
	public String getMessage() {
		return "You do not have sufficient balance, please recharge your card to swipe in";
	}
}
