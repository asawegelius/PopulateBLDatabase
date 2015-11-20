package se.wegelius.PopulateBLDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class PopulateLatLong {


	public void oneDaysLatLongs(String apiKey){
		Connection connection = null;
		String query1 = "SELECT street,zip,district FROM address limit 1;";
		String query2 = "INSERT INTO address_with_location(street,zip,district,lat,lng)VALUES(?,?,?,?,?);";
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		try {
			connection = DBConnect.getConnection();
		} catch (Exception e) {
			System.err.println("There was an error getting the connection");
		}
		try {
			connection.setAutoCommit(false);
			System.err.println("The autocommit was disabled!");
		} catch (SQLException e) {
			System.err.println("There was an error disabling autocommit");
		}
		for(int i = 0; i < 2500; i++){
			try {
				pstmt1 = connection.prepareStatement(query1);
				pstmt2 = connection.prepareStatement(query2);
				ResultSet rs = pstmt1.executeQuery();
				if (rs.next()) {
					String street = rs.getString("street");
					String zip = rs.getString("zip");
					String district = rs.getString("district");
					String address = street + "," + zip + "," + district;
					Double[] latLng = getLatLong(address, apiKey);
					pstmt2.setString(1, street);
					pstmt2.setString(2, zip);
					pstmt2.setString(3, district);
					pstmt2.setDouble(4, latLng[0]);
					pstmt2.setDouble(5, latLng[1]);
					pstmt2.execute();
					connection.commit();
					System.err.println("The transaction was successfully executed");
				}
			} catch (SQLException e) {
				try {
					// We rollback the transaction, atomicity!
					connection.rollback();
					System.err.println(e.getMessage());
					System.err.println("The transaction was rollback");
				} catch (SQLException e1) {
					System.err.println("There was an error making a rollback");
				}
			}
		}
	}
	
	
	
	public Double[] getLatLong(String address, String apiKey) {
		GeoApiContext context = new GeoApiContext().setApiKey(apiKey);
		GeocodingResult[] results = null;
		Double[] coding = new Double[2];
		try {
			results = GeocodingApi.geocode(context, address).await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		coding[0] = results[0].geometry.location.lat;
		coding[1] = results[0].geometry.location.lng;
		return coding;
	}

}
