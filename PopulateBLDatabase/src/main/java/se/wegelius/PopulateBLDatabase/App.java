package se.wegelius.PopulateBLDatabase;

/**
 * "AIzaSyAAdfFpqk9vHsZFN4z6-3oqVUVpNI8khTM"
 * 
 * "AIzaSyALoo8aov-Vu957kQ0561S2g_3ZJPBzssk"
 * "AIzaSyAUCnnRm2hdBsvZUPtdB46N9QWamCERBmY"
 * speedvoter "AIzaSyCSTYvE2u9iQcZLo70_CPVMRNDVjHP6j_U"
 * 
 * locatorbusiness AIzaSyCp1pp9LvbUnTu483n1p7rvaELC7HseDpE
 * 
 * locabusin AIzaSyCsnx32jmmmQ-b8MiHcDkbHQEcKoAdO-Ks
 * 
 * AIzaSyCDZlaqwmHbha-v_8I3BxCjLceYKVGpmrA
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //CsvToTable populator = new CsvToTable();
        //populator.populateCompanyTable();
        //populator.populateAddressTable();
        //populator.importLocFromOldTable();
        //populator.populateCompanyAddress();
        PopulateLatLong latLong = new PopulateLatLong();
        latLong.oneDaysLatLongs("AIzaSyCDZlaqwmHbha-v_8I3BxCjLceYKVGpmrA");
    }
}
