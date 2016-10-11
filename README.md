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
###Retreiving the street "Kleine Freiheit in Hamburg" as polyline
```javascript
OP.createRequest('way["name"="Kleine Freiheit"];', function(e) {
	console.log(e);
});
```
If the street name is not unique:
```javascript
OP.createRequest('area[name="Amsterdam"];way(area)["name"="Docklandsweg"];(._;>;);', function(e) {
	console.log(e);
});
```
Answer:

<img src="https://raw.githubusercontent.com/AppWerft/Ti.Overpass/master/amsterdam.png" width=300 />


###Retreiving all oneway streets in St. Pauli
```javascript
OP.createRequest('area[name="St. Pauli"];way(area)[highway][name][oneway="yes"];(._;>;);',
		function(e) {
			var streetnames = (e.result.elements.filter(function(way){
				return (way.type=="way") ? true : false;
			})).map(function(way){return way.tags.name})
});
```
<img src="https://raw.githubusercontent.com/AppWerft/Ti.Overpass/master/1way.png" width=560 />


###Retreiving  all Stolpersteine in Hamburg
```javascript
OP.createRequest('area[name="Hamburg"];node(area)["memorial:type"="stolperstein"];',
		function(e) {
			console.log(e);
});
```
Answer:
```json
{
  "version": 0.6,
  "generator": "Overpass API",
  "osm3s": {
    "timestamp_osm_base": "2016-10-11T11:16:02Z",
    "timestamp_areas_base": "2016-10-11T06:26:03Z",
    "copyright": "The data included in this document is from www.openstreetmap.org. The data is made available under ODbL."
  },
  "elements": [

{
  "type": "node",
  "id": 274098391,
  "lat": 53.5411726,
  "lon": 10.0474204,
  "tags": {
    "historic": "memorial",
    "memorial:addr": "Großmannstraße, Hamburg",
    "memorial:addr:city": "Hamburg",
    "memorial:addr:postcode": "20539",
    "memorial:addr:street": "Großmannstraße",
    "memorial:type": "stolperstein",
    "name": "Alfons Ganser",
    "network": "Stolpersteine Hamburg",
    "person:date_of_birth": "1914"
  }
},
{
  "type": "node",
  "id": 341788109,
  "lat": 53.5586970,
  "lon": 10.0128167,
  "tags": {
    "addr:city": "Hamburg",
    "addr:country": "DE",
    "historic": "memorial",
    "memorial:addr": "Lange Reihe 93, Hamburg",
    "memorial:text": "Hier wohnte Adolph Mannheimer JG. 1878 deportiert 1941 Minsk ???",
    "memorial:type": "stolperstein",
    "name": "Adolph Mannheimer",
    "network": "Stolpersteine Hamburg"
  }
},
```

###All pos boxes in Hamburg
```javascript
OP.createRequest('node["amenity"="post_box"](53.35,9.8,53.65,10.2);',
		function(e) {
			console.log(e);
});
```
Answer:
```json
{
  "version": 0.6,
  "generator": "Overpass API",
  "osm3s": {
    "timestamp_osm_base": "2016-10-11T11:17:03Z",
    "copyright": "The data included in this document is from www.openstreetmap.org. The data is made available under ODbL."
  },
  "elements": [

{
  "type": "node",
  "id": 25699153,
  "lat": 53.4708618,
  "lon": 9.8526818,
  "tags": {
    "amenity": "post_box",
    "collection_times": "Mo-Fr 16:30; Sa 11:15",
    "collection_times:lastcheck": "2016-05-01",
    "operator": "Deutsche Post AG",
    "ref": "Süderelbeweg / Marktpassage, 21149 Neugraben"
  }
},
{
  "type": "node",
  "id": 26263930,
  "lat": 53.4745387,
  "lon": 9.8944674,
  "tags": {
    "amenity": "post_box",
    "collection_times": "Mo-Fr 17:15; Sa 13:15",
    "collection_times:lastcheck": "2016-05-01",
    "operator": "Deutsche Post AG",
    "ref": "Hausbrucher Bahnhofstraße / Wiedenthaler Bogen, 21147 Hamburg-Neugraben"
  }
}
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


OP.getStreetsByPosition({
		latitude : 53.5653801,
		longitude : 9.98625,
		radius : 250
	},
	function(e) {
		console.log(e);
});

OP.getAmenitiesByPosition({
		latitude : 53.5515311,  // Reeperbahn /St. Pauli
		longitude : 9.9556582,
		radius : 750,
		types : ["swingerclub","stripclub"]  // http://wiki.openstreetmap.org/wiki/Key:amenity
	},
	function(e) {
		console.log(e);
});

```