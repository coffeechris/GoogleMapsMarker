package org.chrisjordan.googlemapsmarker.dao;

import java.util.List;

import org.chrisjordan.googlemapsmarker.bean.Address;

public interface AddressDao {
    public List<Address> getAddresses(String setName);
}
