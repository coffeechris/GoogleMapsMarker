package org.chrisjordan.googlemapsmarker.dao;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.chrisjordan.googlemapsmarker.bean.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleGeocodeDao {
    private String apiKey;

    private static final String GEOCODE_ENDPOINT_FORMAT =
            "https://maps.googleapis.com/maps/api/geocode/json?sensor=false&key=%s&address=%s";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleGeocodeDao.class);

    public GoogleGeocodeDao (String apiKey) {
        this.apiKey = apiKey;
    }

    public Coordinate getCoords(String address) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(String.format(GEOCODE_ENDPOINT_FORMAT, apiKey, address.replaceAll(" ", "+")));
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            JsonNode jsonNode = OBJECT_MAPPER.readTree(EntityUtils.toString(entity));
            JsonNode location = jsonNode.findPath("location");

            return new Coordinate(location.get("lat").asDouble(), location.get("lng").asDouble());
        } catch (IOException e) {
            LOGGER.error("Error communicating with Google geocode endpoint", e);
            throw new RuntimeException("Error communicating with Google geocode endpoint", e);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    public static void main (String args[]) {
        GoogleGeocodeDao geocodeDao = new GoogleGeocodeDao(args[0]);
        System.out.println(geocodeDao.getCoords(args[1]));
    }
}
