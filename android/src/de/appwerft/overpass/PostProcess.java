package de.appwerft.overpass;

import java.util.HashMap;
import java.util.HashSet;

import org.appcelerator.kroll.common.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostProcess {
	private JSONObject foo;
	private JSONArray elements;
	private final String LCAT = OverpassModule.LCAT;
	private final String ELEMENTS = "elements";
	private final String TYPE = "type";
	private final String WAY = "way";
	private final String NODE = "node";
	private final String NODES = "nodes";
	private final String NAME = "name";
	private final String TAGS = "tags";

	private HashMap<Integer, JSONObject> nodes;

	public PostProcess(JSONObject foo) throws JSONException {
		this.foo = foo;
		this.elements = this.foo.getJSONArray(ELEMENTS);
		this.nodes = getNodes();
	}

	public JSONObject passThrough() {
		return foo;
	}

	public JSONObject getStreetNames() throws JSONException {
		Log.d(LCAT, "start getWays");
		HashSet<String> ways = new HashSet<String>();
		for (int i = 0; i < elements.length(); i++) {
			JSONObject element = elements.getJSONObject(i);
			if (element.getString(TYPE).equals(WAY)) {
				String name = element.getJSONObject(TAGS).getString(NAME);
				ways.add(name);
			}
		}
		JSONObject result = new JSONObject();
		result.put("names", ways.toArray());
		return result;
	}

	public JSONArray getWays() throws JSONException {
		Log.d(LCAT, "start getWays");
		JSONArray ways = new JSONArray();
		for (int i = 0; i < elements.length(); i++) {
			JSONObject element = elements.getJSONObject(i);
			if (element.getString(TYPE).equals(WAY)) {
				JSONArray points = new JSONArray();
				JSONObject way = new JSONObject();
				way.put(TAGS, element.getJSONObject(TAGS));
				JSONArray nodeIds = element.getJSONArray(NODES);
				for (int j = 0; j < nodeIds.length(); j++) {
					points.put(getPointById(nodeIds.getInt(j)));
				}
				concatArray(ways, points);
				way.put("points", points);
				ways.put(way);
			}
		}
		return ways;
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

	private JSONObject getPointById(int id) throws JSONException {
		JSONObject node = this.nodes.get(id);
		return new JSONObject("{latitude:" + node.getDouble("lat")
				+ ",longitude:" + node.getDouble("lon") + "}");
	}

	private HashMap<Integer, JSONObject> getNodes() throws JSONException {
		HashMap<Integer, JSONObject> bar = new HashMap<Integer, JSONObject>();
		for (int i = 0; i < elements.length(); i++) {
			JSONObject node = elements.getJSONObject(i);
			if (node.getString(TYPE).equals(NODE)) {
				bar.put(node.getInt("id"), node);
			}
		}
		return bar;
	}

}
