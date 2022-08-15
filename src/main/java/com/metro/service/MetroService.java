package com.metro.service;

import java.time.LocalDateTime;
import java.util.List;

import com.metro.beans.User;
import com.metro.exceptions.InsufficientBalance;
import com.metro.exceptions.InvalidStationException;

public interface MetroService {
	boolean userExists(int id);
	boolean setUserDetails(User user);
	int getCardIdOfUser(int userId);
	LocalDateTime swipeIn(String src,int user_id) throws InsufficientBalance,InvalidStationException;
	double getBalance(int userId);
	boolean addBalance(int userId,double bal);
	boolean swipeOut(String src,String dest, int userId,LocalDateTime swipeInTime) throws InvalidStationException;
	List<String> getAllStations();
	boolean emailCheck(String email);
	boolean checkCredentials(String email, String pass);
	int getUserId(String email, String pass);
}
