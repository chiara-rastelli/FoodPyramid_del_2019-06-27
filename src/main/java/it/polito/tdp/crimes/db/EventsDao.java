package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> listAllCategories(){
		String sql = "	SELECT distinct offense_category_id AS id FROM EVENTS ORDER BY id asc" ;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			List<String> list = new ArrayList<>();
			ResultSet res = st.executeQuery();
			while(res.next()) {
				try {
					list.add(res.getString("id"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Set<LocalDate> listAllDays(){
		String sql = "SELECT distinct reported_date FROM EVENTS ORDER BY reported_date asc" ;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			Set<LocalDate> list = new HashSet<>();
			ResultSet res = st.executeQuery();
			while(res.next()) {
				try {
					list.add(res.getDate("reported_date").toLocalDate());
				//	System.out.println(res.getDate("reported_date").toLocalDate());
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> listAllEventsByCategoryAndDate(int anno, int mese, int giorno, String categoria){
		String sql = 	"SELECT * FROM EVENTS WHERE YEAR(reported_date) = ? AND MONTH(reported_date) = ? " + 
						"AND DAY(reported_date) = ? AND offense_category_id = ?" ;
			try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			List<String> list = new ArrayList<>();
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			st.setString(4, categoria);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				try {
					list.add(res.getString("offense_type_id"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> listAllAdiacenza(int anno, int mese, int giorno, String categoria){
		String sql = 	"	SELECT e1.offense_type_id AS id1, e2.offense_type_id AS id2, COUNT(*) AS peso " + 
						"	FROM EVENTS e1, EVENTS e2 " + 
						"	WHERE YEAR(e1.reported_date) = ? " + 
						"	AND MONTH(e1.reported_date) = ? " + 
						"	AND DAY(e1.reported_date) = ? " + 
						"	AND e1.offense_category_id = ? " + 
						"	AND YEAR(e2.reported_date) = ? " + 
						"	AND MONTH(e2.reported_date) = ? " + 
						"	AND DAY(e2.reported_date) = ? " + 
						"	AND e2.offense_category_id = e1.offense_category_id " + 
						"	AND e2.offense_type_id < e1.offense_type_id " + 
						"	GROUP BY id1, id2" ;
			try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			List<Adiacenza> list = new ArrayList<>();
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			st.setString(4, categoria);
			st.setInt(5, anno);
			st.setInt(6, mese);
			st.setInt(7, giorno);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				try {
					Adiacenza aTemp = new Adiacenza(res.getString("id1"), res.getString("id2"), res.getInt("peso"));
					System.out.println(aTemp+"\n");
					list.add(aTemp);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
