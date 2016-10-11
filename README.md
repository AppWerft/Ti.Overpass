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
			var streetnames = (e.result.filter(function(way){
				return (way.type=="way") ? true : false;
			})).map(function(way){return way.tags.name})
});
```


###Retreiving  all Stolpersteine in Hamburg
```javascript
OP.createRequest('area[name="Hamburg"];node(area)["memorial:type"="stolperstein"];',
		function(e) {
			console.log(e);
});
```
Answer:
```json
<?xml version="1.0" encoding="UTF-8"?>
<osm version="0.6" generator="Overpass API">
<note>The data included in this document is from www.openstreetmap.org. The data is made available under ODbL.</note>
<meta osm_base="2016-10-11T09:57:03Z" areas="2016-10-11T06:26:03Z"/>

  <node id="274098391" lat="53.5411726" lon="10.0474204">
    <tag k="historic" v="memorial"/>
    <tag k="memorial:addr" v="Großmannstraße, Hamburg"/>
    <tag k="memorial:addr:city" v="Hamburg"/>
    <tag k="memorial:addr:postcode" v="20539"/>
    <tag k="memorial:addr:street" v="Großmannstraße"/>
    <tag k="memorial:type" v="stolperstein"/>
    <tag k="name" v="Alfons Ganser"/>
    <tag k="network" v="Stolpersteine Hamburg"/>
    <tag k="person:date_of_birth" v="1914"/>
  </node>
  <node id="341788109" lat="53.5586970" lon="10.0128167">
    <tag k="addr:city" v="Hamburg"/>
    <tag k="addr:country" v="DE"/>
    <tag k="historic" v="memorial"/>
    <tag k="memorial:addr" v="Lange Reihe 93, Hamburg"/>
    <tag k="memorial:text" v="Hier wohnte Adolph Mannheimer JG. 1878 deportiert 1941 Minsk ???"/>
    <tag k="memorial:type" v="stolperstein"/>
    <tag k="name" v="Adolph Mannheimer"/>
    <tag k="network" v="Stolpersteine Hamburg"/>
  </node>
 </osm> 
```

###All pos boxes in Hamburg
```javascript
OP.createRequest('node["amenity"="post_box"](53.35,9.8,53.65,10.2);',
		function(e) {
			console.log(e);
});
```
Answer:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<osm version="0.6" generator="Overpass API">
<note>The data included in this document is from www.openstreetmap.org. The data is made available under ODbL.</note>
<meta osm_base="2016-10-11T10:55:02Z"/>

  <node id="25699153" lat="53.4708618" lon="9.8526818">
    <tag k="amenity" v="post_box"/>
    <tag k="collection_times" v="Mo-Fr 16:30; Sa 11:15"/>
    <tag k="collection_times:lastcheck" v="2016-05-01"/>
    <tag k="operator" v="Deutsche Post AG"/>
    <tag k="ref" v="Süderelbeweg / Marktpassage, 21149 Neugraben"/>
  </node>
  <node id="26263930" lat="53.4745387" lon="9.8944674">
    <tag k="amenity" v="post_box"/>
    <tag k="collection_times" v="Mo-Fr 17:15; Sa 13:15"/>
    <tag k="collection_times:lastcheck" v="2016-05-01"/>
    <tag k="operator" v="Deutsche Post AG"/>
    <tag k="ref" v="Hausbrucher Bahnhofstraße / Wiedenthaler Bogen, 21147 Hamburg-Neugraben"/>
  </node>
  <node id="26284492" lat="53.4993434" lon="9.8952152">
    <tag k="amenity" v="post_box"/>
    <tag k="collection_times" v="Mo-Sa 08:00"/>
    <tag k="collection_times:lastcheck" v="2016-05-08"/>
    <tag k="operator" v="Deutsche Post AG"/>
    <tag k="ref" v="Moorburger Elbdeich 401, 21079 Hamburg"/>
  </node>
</osm>
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