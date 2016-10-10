#Ti.Overpass

Titanium module for handling [Overpass protocol](http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_API_by_Example).

The Overpass API offers a variety of search possibilities. This is also known as querying.
<img src="http://overpass-api.de/logo.png" width=600 />

##Usage
```javascript
var OP = require("de.appwerft.overpass");
OP.setEndpoint(OP.ENDPOINT_RAMBLER); // default
OP.endpoint = OP.ENDPOINT_RAMBLER; // default

// ENDPOINT_MAIN | ENDPOINT_RAMBLER | ENDPOINT_FRENCH

// get the street "Kleine Freiheit in Hamburg"
OP.createRequest('way["name"="Kleine Freiheit"];', function(e) {
	console.log(e);
});

// get all Stolpersteine in Hamburg
OP.createRequest('area[name="Hamburg"];node(area)["memorial:type"="stolperstein"];',
		function(e) {
			console.log(e);
});
```

###Additional functions/methods
```javascript
OP.getPOIs({
		area : {
			latlng : [53.0,10]
			radius : 5000 // m
		},
		"amenity" : "post_box"
	},
	function(e) {
		console.log(e);
});

OP.getWay({
		name : "Paul-Roosen-Straße".
		area : "Hamburg"  // optional
	},
	function(e) {
		console.log(e);
});
``