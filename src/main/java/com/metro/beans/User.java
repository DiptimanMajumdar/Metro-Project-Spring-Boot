package com.metro.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
	private int userId;
	private Card card;
	private String firstName;
	private String lastName;
	private String address;
	private String email;
	private String password;
	private long phoneNumber;
}
