package se.wegelius.PopulateBLDatabase;

/**
 * "AIzaSyAAdfFpqk9vHsZFN4z6-3oqVUVpNI8khTM"
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CsvToTable populator = new CsvToTable();
        //populator.populateCompanyTable();
        //populator.populateAddressTable();
        //populator.populateCompanyAddress();
        PopulateLatLong latLong = new PopulateLatLong();
        latLong.oneDaysLatLongs("AIzaSyAAdfFpqk9vHsZFN4z6-3oqVUVpNI8khTM");
    }
}
