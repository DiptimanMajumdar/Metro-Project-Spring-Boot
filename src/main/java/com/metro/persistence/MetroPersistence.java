package com.metro.persistence;

import java.time.LocalDateTime;
import java.util.List;

import com.metro.beans.User;

public interface MetroPersistence {

	List<Integer> getAllUsers();
	boolean createUser(User user);
	int getCardIdFromUser(int userId);
	List<String> getAllStations();
	double getBalance(int cardId);
	boolean addBalance(int cardId,double bal);
	int getDifference(String src,String dest);
	boolean deductBalance(int cardId,double bal);
	void storeTransactionDetails(int cardId,String src,String dest,LocalDateTime swipeInTime,LocalDateTime swipeOuTime,double fare);
	List<String> getAllUserEmail();
	boolean checkCredentials(String email, String pass);
	int getUserId(String email, String pass);
}
