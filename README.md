#Ti.Overpass

Titanium module for handling [Overpass protocol](http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_API_by_Example).

The Overpass API offers a variety of search possibilities. This is also known as querying. Lets look into usage.
<img src="http://overpass-api.de/logo.png" width=600 />

##Usage
```javascript
var OP = require("de.appwerft.overpass");
OP.setEndpoint(OP.ENDPOINT_RAMBLER); // optiona, default is OP.ENDPOINT_RAMBLER
```
Possible endpoints are ENDPOINT_MAIN, ENDPOINT_RAMBLER, ENDPOINT_FRENCH

You can put your own endpoint (if you maintain an own server) in tiapp.xml:
```xml
<property name="OVERPASS_ENDPOINT" type="String">YOUR ENDPOINT</property>
```

##Some examples:
###Retreiving the street "Am Brunnenhof" in Hamburg" as polyline
```javascript
OP.createRequest('way["name"="Am Brunnenhof"];(._;>;);',
	null, 
	function(e) {
		console.log(e);
});
```
The second parameter defines the output format. Can be 'skel' (only positions), 'body' (default) or 'meta'.
For your convenience you can use the following methods too:
```javascript
getBody(query,function(){});
getSkel(query,function(){});
getMeta(query,function(){});
```

Here the [result of this query](http://overpass-turbo.eu/s/jfD)

If the street name is not unique on our planet, the you can filter:
```javascript
OP.getBody('area[name="Amsterdam"];way(area)["name"="Prinsengracht"];(._;>;);', function(e) {
	console.log(e);
});
```
Answer you can find [here](http://overpass-turbo.eu/s/jhk).

<img src="https://raw.githubusercontent.com/AppWerft/Ti.Overpass/master/amsterdam.png" width=560 />


###Retreiving all one-way streets in St. Pauli
```javascript
OP.getBody('area[name="St. Pauli"];way(area)[highway][name][oneway="yes"];(._;>;);',
		function(e) {
			if (e.success==true)
				var streetnames = (e.result.elements.filter(function(way){
					return (way.type=="way") ? true : false;
				})).map(function(way){return way.tags.name});
			else console.log(e.error);	
});
```
<img src="https://raw.githubusercontent.com/AppWerft/Ti.Overpass/master/1way.png" width=560 />
[Result on turboOverpass](http://overpass-turbo.eu/s/jg6)

If we need the coords for rendering in map we have to resolve the references from ways to nodes.

```javascript
OP.createRequest('area[name="St. Pauli"];way(area)[highway][name][oneway="yes"];(._;>;);',
		null,
		function(e) {
			var streets = (e.result.elements.filter(function(way){
				return (way.type=="way") ? true : false;
			})).map(function(way){return {
				name : way.tags.name,
				nodes : way.nodes}})
			// now every street has name and [nodes]
			var polylines = [];
			streets.forEach(function(street) {
				function getNodebyId(id) {
					return e.elements.filter(function(elem){
						return (elem.type =="node"  && elem.id==id) ? true : false;
					});
				}
				var points = street.nodes.map(function(node){
					return {
						latitude : getNodeById(node).lat, 
						longitude : getNodeById(node).lon
					};
				});
				
				polylines.push({
					name:street.name,
					points: points
				});
		});			
});
```

###Retreiving all stuff from Hamburgs harbour area:

```javascript
 var poly = "53.5437410 9.9611520 53.5345088 10.0158279 53.5249939 10.0419642 53.5146603 10.0578009 53.4929643 10.0469022 53.4735433 10.0599703 53.4732304 10.0349187 53.4696478 10.0112404 53.4782174 9.9223417 53.5089534 9.8797752 53.5355864 9.8845872 53.5431290 9.9498294"; 
OP.createRequest('node(poly:poly)["name"]',
	null,
	function(e) {
});
```
Give us 671 nodes. [Result](http://overpass-turbo.eu/s/jg5)

###Generating of all annotations;
```javascript`
OP.createRequest('node(poly:poly)["name"]["amenity"]',
	null,
	function(e) {
		TiMapView.addAnnotations(e.elements.map(function(node){
			Ti.Map.createAnnotation({
				latitude : node.lat,
				longitude : node.lon,
				title : node.name,
				image : "images" + node.aminity
			});
		}));
		
});

```

###Retreiving  all Stolpersteine in Hamburg
```javascript
OP.createRequest('area[name="Hamburg"];node(area)["memorial:type"="stolperstein"];',
		null,
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
		null,
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
  

##Additional functions/methods

###Retreiving of aminities by position/radius or bounding box
```javascript
OP.getPOIs({
		"bbx" : [53.0,9.8,53.4,10.1],
		"amenity" : "post_box"
	},
	function(e) {
		console.log(e);
});
OP.getPOIs({
		latitude : 53.5653801,
		longitude : 9.98625,
		radius : 250,
		"amenity" : "post_box"
	},
	function(e) {
		console.log(e);
});
```
###Retreiving streets by by position/radius or bounding box
```javascript
OP.getStreetsByPosition({
		latitude : 53.5653801,
		longitude : 9.98625,
		radius : 5000 // 5 km
	},
	function(e) {
		console.log(e);
});

OP.getStreetsByPosition({
		latitude : 53.5515311,  // Reeperbahn /St. Pauli
		longitude : 9.9556582,
		radius : 5000
	},
	function(e) {
		console.log(e);
});

```