package se.wegelius.PopulateBLDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class PopulateLatLong {

	public void oneDaysLatLongs(String apiKey) {
		Connection connection = null;
		String query1 = "SELECT address_id,street,zip,district FROM address where latitude is null limit 1;";
		String query2 = "update address set latitude=?,longitude=? where street=? and district=?;";
		String query3 = "INSERT INTO address_without_location(address_without_location_id,street,zip,district)VALUES(?,?,?,?);";
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
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
		for (int i = 0; i < 2500; i++) {
			try {
				pstmt1 = connection.prepareStatement(query1);
				pstmt2 = connection.prepareStatement(query2);
				pstmt3 = connection.prepareStatement(query3);
				ResultSet rs = pstmt1.executeQuery();
				if (rs.next()) {
					int address_id = rs.getInt("address_id");
					String street = rs.getString("street");
					String zip = rs.getString("zip");
					String district = rs.getString("district");
					String address = street + "," + zip;
					Double[] latLng = getLatLong(address, apiKey);
					if (latLng[0] != null) {
						pstmt2.setString(3, street);
						pstmt2.setString(4, district);
						pstmt2.setDouble(1, latLng[0]);
						pstmt2.setDouble(2, latLng[1]);
						pstmt2.execute();
						connection.commit();
						System.err.println("The transaction was successfully executed");
					} else {
						System.err.println("got no location for address " + address);
						pstmt3.setInt(1, address_id);
						pstmt3.setString(2, street);
						pstmt3.setString(3, zip);
						pstmt3.setString(4, district);
						pstmt3.execute();
						connection.commit();
					}
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
		Double[] latLong = new Double[2];
		try {
			results = GeocodingApi.geocode(context, address).await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (results.length > 0) {
			latLong[0] = results[0].geometry.location.lat;
			latLong[1] = results[0].geometry.location.lng;
			System.out.println("lat, long: " + latLong[0] + ", " + latLong[1]);
		}
		return latLong;
	}

}
