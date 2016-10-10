#TiOpenpass

Titanium module for handling Openpass protocol.

##Usage
```javascript
var Openpass = require("de.appwerft.openpass");
Openpass.createRequest("area[name=\"Troisdorf\"];way(area)[highway][name];out;",function(e){
	if (e.success) {
		console.log(e.result); // JSON object
	}
})

```