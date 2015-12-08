/**
 * 
 */
package se.wegelius.PopulateBLDatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class CsvToTable {

	public void populateCompanyTable() {
		String csvFile = "files/companies.txt";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		String query = "INSERT INTO company(company_code, company_text) VALUES (?,?)";
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				// company code
				String company_code = line.substring(0, line.indexOf(cvsSplitBy));
				// remove company code from line
				line = line.substring(line.indexOf(cvsSplitBy) + 1, line.length());
				String company_text = line;
				Connection con = DBConnect.getConnection();
				try {
					PreparedStatement statement = con.prepareStatement(query);
					statement.setString(1, company_code);
					statement.setString(2, company_text);
					statement.execute();
					statement.close();
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Done");
	}

	public void populateAddressTable() {
		String csvFile = "files/address.txt";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		String query = "INSERT INTO address(street, zip, district) VALUES (?,?,?)";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				// zip
				String zip = line.substring(0, line.indexOf(cvsSplitBy)).trim();
				// remove zip from string
				line = line.substring(line.indexOf(cvsSplitBy) + 1, line.length());
				// district
				String district = line.substring(0, line.indexOf(cvsSplitBy)).trim();
				// remove district from string
				line = line.substring(line.indexOf(cvsSplitBy) + 1, line.length());
				// street
				String street = line.trim();
				Connection con = DBConnect.getConnection();
				try {
					PreparedStatement statement = con.prepareStatement(query);
					statement.setString(1, street);
					statement.setString(2, zip);
					statement.setString(3, district);
					statement.execute();
					statement.close();
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Done");
	}

	public void populateCompanyAddress() {
		String csvFile = "files/company_address.txt";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		String query1 = "select company_id from company where company_code=? limit 1";
		String query2 = "select address_id from address where street=? and district =? limit 1";
		String query3 = "insert into company_address(ca_company_id,ca_address_id)VALUES(?,?)";
		try {
			Connection con = DBConnect.getConnection();
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String company_code = line.substring(0, line.indexOf(cvsSplitBy)).trim();
				// remove company code from line
				line = line.substring(line.indexOf(cvsSplitBy) + 1, line.length());
				String street = line.substring(0, line.indexOf(cvsSplitBy)).trim();
				// remove street from line
				line = line.substring(line.indexOf(cvsSplitBy) + 1, line.length());
				// String zip = line.substring(0,
				// line.indexOf(cvsSplitBy)).trim();
				// remove zip from line
				line = line.substring(line.indexOf(cvsSplitBy) + 1, line.length());
				String district = line;
				try {
					PreparedStatement statement1 = con.prepareStatement(query1);
					PreparedStatement statement2 = con.prepareStatement(query2);
					PreparedStatement statement3 = con.prepareStatement(query3);
					statement1.setString(1, company_code);
					statement2.setString(1, street);
					statement2.setString(2, district);
					ResultSet rs = statement1.executeQuery();
					int comp_id = 0;
					if (rs.next()) {
						comp_id = rs.getInt("company_id");
						statement3.setInt(1, comp_id);
					}
					if (comp_id > 0) {
						ResultSet rs2 = statement2.executeQuery();
						if (rs2.next()) {
							int adr_id = rs2.getInt("address_id");
							statement3.setInt(2, adr_id);
							statement3.execute();
						}
					}
					statement1.close();
					statement2.close();
					statement3.close();
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (

		FileNotFoundException e)

		{

			e.printStackTrace();
		} catch (

		IOException e)

		{
			e.printStackTrace();
		} finally

		{
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
	}

	public void importLocFromOldTable() {
		String csvFile = "files/address_with_location.txt";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		String query1 = "update address set latitude=?,longitude=? where street=? and district=?;";
		PreparedStatement pstmt1 = null;
		Connection connection = null;
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
		try {
			br = new BufferedReader(new FileReader(csvFile));
		} catch (IOException e) {
			System.err.println("There was an error getting the buffert reader");
		}
		try {
			while ((line = br.readLine()) != null) {
				try {
					pstmt1 = connection.prepareStatement(query1);
					String lat = line.substring(line.lastIndexOf(cvsSplitBy) + 1, line.length());
					Double latitude = new Double(lat).doubleValue();
					line = line.substring(0, line.lastIndexOf(cvsSplitBy));
					String lng = line.substring(line.lastIndexOf(cvsSplitBy) + 1, line.length());
					Double longitude = new Double(lng).doubleValue();
					line = line.substring(0, line.lastIndexOf(cvsSplitBy));
					String district = line.substring(line.lastIndexOf(cvsSplitBy) + 2, line.length()-1);
					line = line.substring(0, line.lastIndexOf(cvsSplitBy));
					line = line.substring(0, line.lastIndexOf(cvsSplitBy));
					String street = line.substring(line.lastIndexOf(cvsSplitBy) + 2, line.length()-1);
					pstmt1.setDouble(1, latitude);
					pstmt1.setDouble(2, longitude);
					pstmt1.setString(3, street);
					pstmt1.setString(4, district);
					pstmt1.execute();
					connection.commit();
					//System.out.println("street " + street + " district " + district + "lat " + lat + " long " + lng);
					System.err.println("The transaction was successfully executed");
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addMainBranch(){
		String csvFile = "files/branch_main_companies.txt";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		String query1 = "update company set c_main_branch_id=? where company_code=?";
		PreparedStatement pstmt1 = null;
		Connection connection = null;		
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
		try {
			br = new BufferedReader(new FileReader(csvFile));
		} catch (IOException e) {
			System.err.println("There was an error getting the buffert reader");
		}	
		try {
			while ((line = br.readLine()) != null) {
				try {
					pstmt1 = connection.prepareStatement(query1);
					// cut the description
					line = line.substring(0, line.lastIndexOf(cvsSplitBy));
					String branch_code = line.substring(line.lastIndexOf(cvsSplitBy) + 1, line.length());
					// cut the branch code
					line = line.substring(0, line.lastIndexOf(cvsSplitBy));
					String cvr = line.substring(line.lastIndexOf(cvsSplitBy) + 1, line.length());
					pstmt1.setString(1, branch_code);
					pstmt1.setString(2, cvr);
					pstmt1.execute();
					connection.commit();
					System.err.println("The transaction was successfully executed");
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
