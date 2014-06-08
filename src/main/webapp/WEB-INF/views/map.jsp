<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
  <head>
    <title>Google Maps Markers</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
      html { height: 100% }
      body { height: 100%; margin: 0; padding: 0 }
      #map-canvas { height: 100% }
    </style>
    <script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?key=${key}">
    </script>
    <script type="text/javascript">
      function initialize() {
        var mapOptions = {
            mapTypeId: 'roadmap'
        };
        var map = new google.maps.Map(document.getElementById("map-canvas"),
            mapOptions);
        var bounds = new google.maps.LatLngBounds();
        
        // Sample markers
        var markers = [
            ['car', 47.6204674,-122.3491156],
            ['robber', 47.2395711,-122.42874],
            ['house', 47.5962326,-120.6614765]
        ];
        
        // Loop through our array of markers & place each one on the map         
        for( i = 0; i < markers.length; i++ ) {
            var icon = 'resources/car-icon.png';
            if ('robber' === markers[i][0]) {
                icon = 'resources/car-robber-icon.png';    
            } else if ('house' == markers[i][0]) {
                icon = 'resources/house-icon.png';
            }
            
            var position = new google.maps.LatLng(markers[i][1], markers[i][2]);
            bounds.extend(position);
            marker = new google.maps.Marker({
                position: position,
                map: map,
                title: markers[i][0],
                icon: icon
            });    
        }
     
        // Automatically center the map fitting all markers on the screen
        map.fitBounds(bounds);
      }
      google.maps.event.addDomListener(window, 'load', initialize);
    </script>
  </head>
  <body>
    <h1>
        Google Maps Markers 
    </h1>
    <div>Icons made by 
        <a href="http://www.freepik.com" alt="Freepik.com" title="Freepik.com">Freepik</a> 
        from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a>
    </div>
    <div id="map-canvas"/>    
  </body>
</html>
