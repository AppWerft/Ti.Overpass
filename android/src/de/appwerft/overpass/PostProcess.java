package de.appwerft.overpass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostProcess {
	private JSONObject foo;
	private JSONArray elements;

	public PostProcess(JSONObject foo) throws JSONException {
		this.foo = foo;
		this.elements = this.foo.getJSONArray("elements");
	}

	public JSONObject passThrough() {
		return foo;
	}

	public JSONArray getWays() throws JSONException {
		JSONArray ways = new JSONArray();
		for (int i = 0; i < elements.length(); i++) {
			JSONObject element = elements.getJSONObject(i);
			if (element.getString("type") == "way") {
				JSONArray points = new JSONArray();
				JSONObject way = new JSONObject();
				// copy of tags
				way.put("tags", element.getJSONObject("tags"));
				JSONArray nodeIds = element.getJSONArray("nodes");
				for (int j = 0; j < nodeIds.length(); j++) {
					if (nodeIds.get(j) instanceof Integer) {
						concatArray(points,
								getNodesbyId((int) nodeIds.getInt((int) j)));
					}
				}
				way.put("points", points);
				ways.put(way);
			}
		}
		return ways;
	}

	public JSONObject getStreetNames() {
		return foo;
	}

	private JSONArray concatArray(JSONArray... arrs) throws JSONException {
		JSONArray result = new JSONArray();
		for (JSONArray arr : arrs) {
			for (int i = 0; i < arr.length(); i++) {
				result.put(arr.get(i));
			}
		}
		return result;
	}

	private JSONArray getNodesbyId(int id) throws JSONException {
		JSONArray bar = new JSONArray();
		for (int i = 0; i < elements.length(); i++) {
			JSONObject element = elements.getJSONObject(i);
			if (element.getString("type") == "node") {
				if (element.getInt("id") == id) {
					bar.put(new JSONObject("{latitude:"
							+ element.getDouble("lat") + ",longitude:"
							+ element.getDouble("lon") + "}"));
				}
			}
		}
		return bar;
	}

}
