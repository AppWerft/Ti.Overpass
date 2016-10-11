#Ti.Overpass

Titanium module for handling [Overpass protocol](http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_API_by_Example).

The Overpass API offers a variety of search possibilities. This is also known as querying. Lets look into usage.
<img src="http://overpass-api.de/logo.png" width=600 />

##Usage
```javascript
var OP = require("de.appwerft.overpass");
OP.setEndpoint(OP.ENDPOINT_RAMBLER); // default
```
Possible endpoints:
ENDPOINT_MAIN | ENDPOINT_RAMBLER | ENDPOINT_FRENCH

You can put your own in tiapp.xml:
```xml
<property name="OVERPASS_ENDPOINT" type="String">YOUR ENDPOINT</property>
```

##Some examples:
```javascript
Retreiving the street "Kleine Freiheit in Hamburg" as polyline
```
```javascript
OP.createRequest('way["name"="Kleine Freiheit"];', function(e) {
	console.log(e);
});
```
If the street name is not unique:
```javascript
OP.createRequest('area[name="Amsterdam"];way(area)["name"="Docklandsweg"];', function(e) {
	console.log(e);
});
```

Retreiving  all Stolpersteine in Hamburg
```javascript
OP.createRequest('area[name="Hamburg"];node(area)["memorial:type"="stolperstein"];',
		function(e) {
			console.log(e);
});
```

###Additional functions/methods
```javascript

OP.getPOIs({
		"bbx" : [53.0,9.8,53.4,10.1],
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