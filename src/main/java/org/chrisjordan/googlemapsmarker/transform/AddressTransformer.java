package org.chrisjordan.googlemapsmarker.transform;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.chrisjordan.googlemapsmarker.bean.Address;
import org.chrisjordan.googlemapsmarker.dao.GoogleGeocodeDao;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A utility for transforming street addresses and their corresponding metadata into
 * Address objects. The main method generates JSON strings that can be processed by
 * the Web app.
 *
 */
public class AddressTransformer {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String addressSuffix;
    private Map<String,String> labelMap;
    private GoogleGeocodeDao googleGeocodeDao;

    public Address lookupCoordinate(String streetAddress, String label, String dateTime) {
        Address address = new Address();
        address.setStreetAddress(streetAddress + addressSuffix);
        address.setLabel(labelMap.get(label));
        address.setDateTime(dateTime);

        address.setCoordinate(googleGeocodeDao.getCoords(address.getStreetAddress()));
        return address;
    }

    /**
     * This main method processes crime report records. Expected command line arguments:
     * <ul>
     * <li>args[0] - Google API key</li>
     * <li>args[1] - Street address suffix. Typically records are missing city and state</li>
     * <li>args[2] - File path for the crime report records file</li>
     * </ul>
     *
     * Crime report records are expected to be tab delimited. Expected columns:
     * <ul>
     * <li>Street address</li>
     * <li>Crime code label</li>
     * <li>Date time</li>
     * </ul>
     */
    public static void main (String args[]) throws IOException {
        AddressTransformer transformer = new AddressTransformer();
        transformer.setGoogleGeocodeDao(new GoogleGeocodeDao(args[0]));
        transformer.setAddressSuffix(args[1]);

        HashMap<String,String> labelMap = new HashMap<>();
        labelMap.put("Burglary/breaking and entering", "house");
        labelMap.put("Theft of motor vehicle parts/accessories", "robber");
        labelMap.put("Theft from motor vehicle", "robber");
        labelMap.put("Motor vehicle theft", "car");
        transformer.setLabelMap(labelMap);

        Scanner scanner = new Scanner(new File(args[2]));
        while (scanner.hasNextLine()) {
            String tokens[] = scanner.nextLine().split("\t");
            if (tokens.length >= 3) {
                String streetAddress = tokens[0];
                String label = tokens[1];
                String dateTime = tokens[2];

                Address address = transformer.lookupCoordinate(streetAddress, label, dateTime);
                System.out.println(OBJECT_MAPPER.writeValueAsString(address));
            }
        }
        scanner.close();
    }

    public void setAddressSuffix(String addressSuffix) {
        this.addressSuffix = addressSuffix;
    }

    public void setGoogleGeocodeDao(GoogleGeocodeDao googleGeocodeDao) {
        this.googleGeocodeDao = googleGeocodeDao;
    }

    public void setLabelMap(Map<String, String> labelMap) {
        this.labelMap = labelMap;
    }
}
