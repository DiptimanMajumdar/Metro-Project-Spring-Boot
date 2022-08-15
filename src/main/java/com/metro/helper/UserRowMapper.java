package com.metro.helper;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.metro.beans.User;

public class UserRowMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		int id = resultSet.getInt("user_id");
		String fname = resultSet.getString("first_name");
		String lname = resultSet.getString("last_name");
		String address = resultSet.getString("address");
		Long phone = resultSet.getLong("phn_number");
		String email = resultSet.getString("email");
		String pass = resultSet.getString("password");
		
		User user=new User(id,null,fname,lname,address,email,pass,phone); //temporary set to null 
		
		return user;
	}

}
