package org.chrisjordan.googlemapsmarker.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class GoogleMapController {
    private static final Logger logger = LoggerFactory.getLogger(GoogleMapController.class);

    /**
     * While the key request parameter is not required, if it is not supplied, the no-key view is given.
     * It instructs users to provide a key. Requests made with a key will receive the map view.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(String key, Model model) {
        if (StringUtils.isBlank(key)) {
            return "no-key";
        }

        model.addAttribute("key", key);
        return "map";
    }
}
