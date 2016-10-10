#Ti.Openpass

Titanium module for handling [Openpass protocol](http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_API_by_Example).

The Overpass API offers a variety of search possibilities. This is also known as querying.

##Usage
```javascript
var Openpass = require("de.appwerft.openpass");
Openpass.setEndpoint(OpenPass.ENDPOINT_RAMBLER); // default
Openpass.endpoint = OpenPass.ENDPOINT_RAMBLER; // default

// ENDPOINT_MAIN | ENDPOINT_RAMBLER | ENDPOINT_FRENCH
Openpass.createRequest("area[name=\"Troisdorf\"];way(area)[highway][name];out;",function(e){
	if (e.success) {
		console.log(e.result); // JSON object
	}
})

```