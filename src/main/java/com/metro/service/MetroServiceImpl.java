package com.metro.service;

import java.time.LocalDateTime;
import java.util.List;

import com.metro.beans.User;
import com.metro.exceptions.InsufficientBalance;
import com.metro.exceptions.InvalidStationException;
import com.metro.persistence.MetroPersistence;
import com.metro.persistence.MetroPersistenceImpl;

public class MetroServiceImpl implements MetroService{

	private MetroPersistence metroPersistence = new MetroPersistenceImpl();

	@Override
	public boolean userExists(int id) {
		List<Integer> users = metroPersistence.getAllUsers();
		for (Integer user_id : users)
			if (user_id == id)
				return true;

		return false;
	}

	@Override
	public boolean setUserDetails(User user) {
		return metroPersistence.createUser(user);
	}

	@Override
	public int getCardIdOfUser(int userId) {
		return metroPersistence.getCardIdFromUser(userId);
	}
	private boolean checkStation(List<String> stations,String src) {
		for(String s:stations)
			if(s.equalsIgnoreCase(src))
				return true;
		return false;
	}
	@Override
	public LocalDateTime swipeIn(String src, int user_id) throws InsufficientBalance, InvalidStationException {
		// TODO Auto-generated method stub
		if(!checkStation(metroPersistence.getAllStations(), src))
			throw new InvalidStationException();
		double balance = metroPersistence.getBalance(getCardIdOfUser(user_id));
		if(balance<20)
			throw new InsufficientBalance();
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime;
	}
	
	
	@Override
	public double getBalance(int userId) {
		return metroPersistence.getBalance(getCardIdOfUser(userId));
	}

	@Override
	public boolean addBalance(int userId, double bal) {
		return metroPersistence.addBalance(getCardIdOfUser(userId),bal);
	}

	@Override
	public boolean swipeOut(String src, String dest, int userId,LocalDateTime swipeInTime) throws InvalidStationException {
		if(!checkStation(metroPersistence.getAllStations(), dest))
			throw new InvalidStationException();
		int diff = metroPersistence.getDifference(src,dest);
		if(!metroPersistence.deductBalance(getCardIdOfUser(userId),diff*5))
			return false;
		LocalDateTime swipeOutTime = LocalDateTime.now();
		metroPersistence.storeTransactionDetails(getCardIdOfUser(userId),src,dest,swipeInTime,swipeOutTime,diff*5);
		return true;
	}

	@Override
	public List<String> getAllStations() {
		return metroPersistence.getAllStations();
	}
	
	@Override
	public boolean emailCheck(String email) {
		List<String> users = metroPersistence.getAllUserEmail();
		for (String user_email : users)
			if (user_email.equals(email))
				return true;

		return false;
	}

	@Override
	public boolean checkCredentials(String email,String pass) {
		return metroPersistence.checkCredentials(email, pass);
	}

	@Override
	public int getUserId(String email, String pass) {
		
		return metroPersistence.getUserId(email,pass);
	}

}
