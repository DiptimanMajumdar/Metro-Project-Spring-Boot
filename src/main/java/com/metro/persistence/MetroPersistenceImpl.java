package com.metro.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.metro.beans.Card;
import com.metro.beans.User;
import com.metro.helper.UserRowMapper;

@Repository
public class MetroPersistenceImpl implements MetroPersistence {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	static class Pair {
		private Integer prev, next;

		public Pair(Integer prev, Integer next) {
			this.prev = prev;
			this.next = next;
		}
	}

	private Map<Integer, Pair> stationSequence = new HashMap<>();

	{
		setSequence();
	}

	public void setSequence() {

//		try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root", "wiley");
//				PreparedStatement preparedStatement = connection.prepareStatement("Select * from stations");) {
//			ResultSet resultSet = preparedStatement.executeQuery();
//			while (resultSet.next()) {
//				stationSequence.put(resultSet.getInt("station_id"),
//						new Pair(resultSet.getInt("prev_station_id"), resultSet.getInt("next_station_id")));
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

		String query = "Select * from stations";

		
	}

	@Override
	public List<Integer> getAllUsers() {
		/*
		 * List<Integer> listOfUserId = new ArrayList<>(); try (Connection connection =
		 * DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root",
		 * "wiley"); PreparedStatement preparedStatement =
		 * connection.prepareStatement("Select * from user_details");) { ResultSet
		 * resultSet = preparedStatement.executeQuery(); while (resultSet.next()) { int
		 * user_id = resultSet.getInt("user_id"); listOfUserId.add(user_id); }
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); }
		 * 
		 * return listOfUserId;
		 */
		
		return jdbcTemplate.query("Select * from user_details",
				(rs, rowNum) -> new Integer(rs.getInt("user_id")));
	}

	@Override
	public boolean createUser(User user) {
		/*
		 * int rows1 = 0, rows2 = 0; try (Connection connection =
		 * DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root",
		 * "wiley"); PreparedStatement preparedStatement = connection
		 * .prepareStatement("INSERT INTO user_details values(?,?,?,?,?,?,?)");
		 * PreparedStatement preparedStatement1 = connection
		 * .prepareStatement("INSERT INTO cards values(?,?,?)");) {
		 * connection.setAutoCommit(false); preparedStatement.setInt(1,
		 * user.getUserId()); preparedStatement.setString(2, user.getFirstName());
		 * preparedStatement.setString(3, user.getLastName());
		 * preparedStatement.setString(4, user.getAddress());
		 * preparedStatement.setLong(5, user.getPhoneNumber());
		 * preparedStatement.setString(6, user.getEmail());
		 * preparedStatement.setString(7, user.getPassword());
		 * 
		 * rows1 = preparedStatement.executeUpdate();
		 * 
		 * int currentMaxId = currentMaxCardId(); int currentMaxUserId=currMaxUserId();
		 * 
		 * //user.setUserId(getUserId(user.getEmail(), user.getPassword()));
		 * preparedStatement1.setInt(1, currentMaxId == 0 ? 201 : currentMaxId + 1);
		 * preparedStatement1.setInt(2, currentMaxUserId ==0 ? 1 : currentMaxUserId +
		 * 1); preparedStatement1.setDouble(3, 100);
		 * 
		 * user.setCard(new Card(currentMaxId == 0 ? 201 : currentMaxId + 1, 100));
		 * 
		 * rows2 = preparedStatement1.executeUpdate(); connection.commit();
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); }
		 * 
		 * return (rows1 * rows2) != 0;
		 */

		String queryUser = "INSERT INTO user_details values(?,?,?,?,?,?,?)";
		String queryCard = "INSERT INTO cards values(?,?,?)";

		int rows1 = 0, rows2 = 0;
		int currentMaxId = currentMaxCardId();
		int currentMaxUserId = currMaxUserId();

		try {
			jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);
			rows1 = jdbcTemplate.update(queryUser, user.getUserId(), user.getFirstName(), user.getLastName(),
					user.getAddress(), user.getPhoneNumber(), user.getEmail(), user.getPassword());
			rows2 = jdbcTemplate.update(queryCard, currentMaxId == 0 ? 201 : currentMaxId + 1,
					currentMaxUserId == 0 ? 1 : currentMaxUserId + 1, 100);
			jdbcTemplate.getDataSource().getConnection().commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return (rows1 * rows2) != 0;
	}

	private int currentMaxCardId() {
		/*
		 * int result = -1; try (Connection connection =
		 * DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root",
		 * "wiley"); PreparedStatement preparedStatement = connection
		 * .prepareStatement("select max(card_id) as 'id' from cards");) {
		 * 
		 * ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next();
		 * result = resultSet.getInt("id");
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); }
		 * 
		 * return result;
		 */

		String query = "select max(card_id) as 'id' from cards";
		int max = jdbcTemplate.queryForObject(query, new Object[] {}, Integer.class);

		return max;
	}

	private int currMaxUserId() {
		/*
		 * int result = -1; try (Connection connection =
		 * DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root",
		 * "wiley"); PreparedStatement preparedStatement = connection
		 * .prepareStatement("select max(user_id) as 'id' from user_details");) {
		 * 
		 * ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next();
		 * result = resultSet.getInt("id");
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); }
		 * 
		 * return result;
		 */

		String query = "select max(user_id) as 'id' from user_details";
		int maxUserId = jdbcTemplate.queryForObject(query, new Object[] {}, Integer.class);

		return maxUserId;
	}

	@Override
	public int getCardIdFromUser(int userId) {
		/*
		 * int result = -1; try (Connection connection =
		 * DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root",
		 * "wiley"); PreparedStatement preparedStatement = connection
		 * .prepareStatement("select card_id as 'id' from cards where user_id = " +
		 * userId);) {
		 * 
		 * ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next();
		 * result = resultSet.getInt("id");
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); }
		 * 
		 * return result;
		 */

		String query = "select card_id as 'id' from cards where user_id = ?";
		int cardId = jdbcTemplate.queryForObject(query, new Object[] { userId }, Integer.class);

		return cardId;
	}

	@Override
	public List<String> getAllStations() {

		/*
		 * try (Connection connection =
		 * DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root",
		 * "wiley"); PreparedStatement preparedStatement = connection
		 * .prepareStatement("Select station_name as 'sn' from stations");) { ResultSet
		 * resultSet = preparedStatement.executeQuery(); while (resultSet.next()) {
		 * String sn = resultSet.getString("sn"); stations.add(sn); }
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); }
		 */

		return jdbcTemplate.query("Select station_name from stations",
				(rs, rowNum) -> new String(rs.getString("station_name")));
	}

	@SuppressWarnings("deprecation")
	@Override
	public double getBalance(int cardId) {
		/*double result = -1;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root", "wiley");
				PreparedStatement preparedStatement = connection
						.prepareStatement("select balance as 'bal' from cards where card_id= " + cardId);) {
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			result = resultSet.getDouble("bal");
		} catch (SQLException e) {
			e.printStackTrace();
		}*/

		return jdbcTemplate.queryForObject("select balance from cards where card_id= ?", 
				new Object[] {cardId}, Double.class);
	}

	@Override
	public boolean addBalance(int cardId, double bal) {
		/*try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root", "wiley");
				PreparedStatement preparedStatement = connection
						.prepareStatement("update cards set balance = balance + ? where card_id= " + cardId);) {
			preparedStatement.setDouble(1, bal);
			int rows = preparedStatement.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		
		
		return jdbcTemplate.update("update cards set balance = balance + ? where card_id= ?",
				cardId,bal)>0;
	}

	@Override
	public int getDifference(String src, String dest) {
		int srcId = stationIdByName(src);
		int destId = stationIdByName(dest);
		int curr = srcId;
		int diff = 0;
		while (curr != destId) {
			diff++;
			curr = stationSequence.get(curr).next;
		}
		return diff;
	}

	@Override
	public boolean deductBalance(int cardId, double bal) {
		/*try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root", "wiley");
				PreparedStatement preparedStatement = connection
						.prepareStatement("update cards set balance = balance - ? where card_id= " + cardId);) {
			preparedStatement.setDouble(1, bal);
			int rows = preparedStatement.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		
		return jdbcTemplate.update("update cards set balance = balance - ? where card_id= ?",
				cardId,bal)>0;
	}

	@Override
	public void storeTransactionDetails(int cardId, String src, String dest, LocalDateTime swipeInTime,
			LocalDateTime swipeOuTime, double fare) {
		/*try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root", "wiley");
				PreparedStatement preparedStatement = connection
						.prepareStatement("insert into transaction_histories values(?,?,?,?,?,?,?)");) {
			preparedStatement.setInt(1, 0);
			preparedStatement.setInt(2, cardId);
			preparedStatement.setInt(5, stationIdByName(src));
			preparedStatement.setInt(6, stationIdByName(dest));
			preparedStatement.setString(3, swipeInTime.toString());
			preparedStatement.setString(4, swipeOuTime.toString());
			preparedStatement.setDouble(7, fare);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}*/

		jdbcTemplate.update("insert into transaction_histories values(?,?,?,?,?,?,?)",
				0,cardId,stationIdByName(src),stationIdByName(dest),swipeInTime.toString(),
				swipeOuTime.toString(),fare);
	}

	private int stationIdByName(String station) {
		/*try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root", "wiley");
				PreparedStatement preparedStatement = connection
						.prepareStatement("select station_id as 'si' from stations where station_name = ?");) {
			preparedStatement.setString(1, station);
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			return rs.getInt("si");
		} catch (SQLException e) {

			e.printStackTrace();
		}*/
		
		@SuppressWarnings("deprecation")
		int stationId = jdbcTemplate.queryForObject("select station_id as 'si' from stations where station_name = ?", 
				new Object[] { station }, Integer.class);
		
		return stationId;
	}

	@Override
	public List<String> getAllUserEmail() {
		/*List<String> listOfUserEmail = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root", "wiley");
				PreparedStatement preparedStatement = connection.prepareStatement("Select * from user_details");) {
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String email = resultSet.getString("email");
				listOfUserEmail.add(email);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}*/

		return jdbcTemplate.query("Select * from user_details",
				(rs, rowNum) -> new String(rs.getString("email")));
	}

	@Override
	public boolean checkCredentials(String email, String pass) {
		/*try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root", "wiley");
				PreparedStatement preparedStatement = connection
						.prepareStatement("Select count(*) as cnt from user_details where email=? and password=?");) {
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, pass);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			if (resultSet.getInt("cnt") > 0)
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		//return jdbcTemplate.update("update cards set balance = balance - ? where card_id= ?",
		//		cardId,bal)>0;
		
		return jdbcTemplate.queryForObject(
                "Select count(*) as cnt from user_details where email=? and password=?",
                Integer.class, new Object[] { email,pass })>0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getUserId(String email, String pass) {
		/*try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Metro", "root", "wiley");
				PreparedStatement preparedStatement = connection
						.prepareStatement("Select user_id as id from user_details where email=? and password=?");) {
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, pass);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			return resultSet.getInt("id");

		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		
		return jdbcTemplate.queryForObject("Select user_id as id from user_details where email=? and password=?",
				new Object[] { email,pass }, Integer.class);
	}

}
