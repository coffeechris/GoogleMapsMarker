package org.chrisjordan.googlemapsmarker.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.chrisjordan.googlemapsmarker.bean.Address;
import org.chrisjordan.googlemapsmarker.bean.Coordinate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A simple address dao that loads a cache of address data through a series of HTTP
 * requests. There is a default set of data hard coded in the constructor for demonstration
 * purposes.
 *
 */
@Repository
public class AddressDaoSimpleHttpCacheImpl implements AddressDao {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * An URL for an address map can be optionally specified through a JVM property
     */
    private final static String ADDRESS_MAP_PROP = "marker.address-map";

    Map<String,List<Address>> addressCache;

    public AddressDaoSimpleHttpCacheImpl () {
        addressCache = new HashMap<>();

        // Hard coded default data
        addressCache.put("default", new ArrayList<Address>());
        List<Address> addresses = addressCache.get("default");
        Address address = new Address();
        address.setStreetAddress("Leavenworth, WA");
        address.setLabel("house");
        address.setCoordinate(new Coordinate(47.5962326,-120.6614765));
        addresses.add(address);

        address = new Address();
        address.setStreetAddress("Seattle, WA");
        address.setLabel("car");
        address.setCoordinate(new Coordinate(47.6062095,-122.3320708));
        addresses.add(address);

        address = new Address();
        address.setStreetAddress("Tacoma, WA");
        address.setLabel("robber");
        address.setCoordinate(new Coordinate(47.2528768,-122.4442906));
        addresses.add(address);

        // Check for the JVM property
        String addressMapURL = System.getProperty(ADDRESS_MAP_PROP);
        if (StringUtils.isNotBlank(addressMapURL)) {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(addressMapURL);
            CloseableHttpResponse response = null;
            String mapData = null;
            try {
                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                mapData = EntityUtils.toString(entity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(response);
                IOUtils.closeQuietly(httpclient);
            }

            Scanner scanner = new Scanner(mapData);
            try {
                while (scanner.hasNextLine()) {
                    String tokens[] = scanner.nextLine().split("\t");
                    addressCache.put(tokens[0], downloadAddresses(tokens[1]));
                }
            } finally {
                IOUtils.closeQuietly(scanner);
            }
        }
    }

    private List<Address> downloadAddresses (String url) {
        ArrayList<Address> addresses = new ArrayList<Address>();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        Scanner scanner = null;
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            scanner = new Scanner(EntityUtils.toString(entity));
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (StringUtils.isNotBlank(line)) {
                    addresses.add(OBJECT_MAPPER.readValue(line, Address.class));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(scanner);
            IOUtils.closeQuietly(response);
            IOUtils.closeQuietly(httpclient);
        }

        return addresses;
    }

    @Override
    public List<Address> getAddresses(String setName) {
        return addressCache.get(setName);
    }
}
