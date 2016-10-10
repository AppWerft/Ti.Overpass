#Ti.Overpass

Titanium module for handling [Overpass protocol](http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_API_by_Example).

The Overpass API offers a variety of search possibilities. This is also known as querying.

##Usage
```javascript
var OP = require("de.appwerft.overpass");
OP.setEndpoint(OP.ENDPOINT_RAMBLER); // default
OP.endpoint = OP.ENDPOINT_RAMBLER; // default

// ENDPOINT_MAIN | ENDPOINT_RAMBLER | ENDPOINT_FRENCH
OP.createRequest("area[name=\"Troisdorf\"];way(area)[highway][name];out;",function(e){
	if (e.success) {
		console.log(e.result); // JSON object
	}
})

```