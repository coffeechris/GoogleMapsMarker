GoogleMapsMarker
================

Google maps provides an API that you can interact with programmatically. This API gives you functionality that is not available through maps.google.com. One bit of functionality is the ability to place multiple markers. I developed this code to demonstrate how to use the Google geocode API to get coordinates from addresses that you can then place as markers using the Google maps API. This code was written originally to help out my local neighborhood association plot reported car thefts and break and enters but really, it can be applied to any scenario where you want to put multiple markers on Google maps.

Prerequisites
=============
* Google API key: Easy enough to get and usage of the API is free for minimal usage. To get a key, go to https://developers.google.com
* Addresses: I provide a simple utility program that processes a tab delimit file of addresses. The expected fields are addresses, label, and date time. Labels can be "Burglary/breaking and entering", "Theft of motor vehicle parts/accessories", "Theft from motor vehicle", and "Motor vehicle theft". Remember, I wrote this Web app originally to help plot reported incidents of theft :-)

AddressTransformer
==================
This is the utility that I wrote for converts addresses in to coordinates using the Google geocode API.

Web App
=======
The main Web app comes with a hard coded set of coordinates which is good for demonstration purposes. If you want to provide your own coordinates, you will need to start the app with the Java property "marker.address-map" set to a URL for a tab delimited list of key/value pairs where the keys are names of the sets of addresses and the values are URLs for those sets. Each line of the address set should be a JSON representing the Address class. Don't worry, the code is pretty straightforward.

Assuming that you have started the app locally on port 8080, you can use the URL below with your Google API key. If you have specified a marker address map, you can replace 'default' with different keys in it.

http://localhost:8080/googlemapsmarker/?key={YOUR GOOGLE API KEY}&addressKey=default

<img src="http://chrisjordan.ca.website.s3-website-us-east-1.amazonaws.com/images/blog2014/default_map.jpg" width=600/>