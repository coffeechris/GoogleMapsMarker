package org.chrisjordan.googlemapsmarker.controller;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.chrisjordan.googlemapsmarker.dao.AddressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
public class GoogleMapController {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private AddressDao addressDao;

    /**
     * While the key request parameter is not required, if it is not supplied, the no-key view is given.
     * It instructs users to provide a key. Requests made with a key will receive the map view.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(String key, @RequestParam(defaultValue="default") String addressKey, Model model) throws IOException {
        if (StringUtils.isBlank(key)) {
            return "no-key";
        }

        model.addAttribute("key", key);
        model.addAttribute("addresses", OBJECT_MAPPER.writeValueAsString(addressDao.getAddresses(addressKey)));
        return "map";
    }

    public void setAddressDao(AddressDao addressDao) {
        this.addressDao = addressDao;
    }
}
