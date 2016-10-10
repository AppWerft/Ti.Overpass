#TiOpenpass

Titanium module for handling [Openpass protocol](http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_API_by_Example).

The Overpass API offers a variety of search possibilities. This is also known as querying. The results of those searches or queries can be displayed directly on a map, but it is also possible to retrieve only the data. On this page we will focus on examples that have to be displayed on a map. This means that all queries can be inserted in the code editor of Overpass turbo . From there the query can be executed and the result will be shown on a map. 

##Usage
```javascript
var Openpass = require("de.appwerft.openpass");
Openpass.createRequest("area[name=\"Troisdorf\"];way(area)[highway][name];out;",function(e){
	if (e.success) {
		console.log(e.result); // JSON object
	}
})

```