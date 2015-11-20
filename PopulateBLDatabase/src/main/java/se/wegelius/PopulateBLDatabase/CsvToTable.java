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
				String street = line.substring(0,line.indexOf(cvsSplitBy)).trim();
				// remove street from line
				line = line.substring(line.indexOf(cvsSplitBy) + 1, line.length());
				//String zip = line.substring(0, line.indexOf(cvsSplitBy)).trim();
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
}
