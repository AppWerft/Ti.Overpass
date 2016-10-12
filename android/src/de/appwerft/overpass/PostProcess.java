package de.appwerft.overpass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.appcelerator.kroll.common.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostProcess {
	private JSONObject foo;
	private JSONArray elements;
	private final String LCAT = OverpassModule.LCAT;
	private JSONObject nodes;

	public PostProcess(JSONObject foo) throws JSONException {
		this.foo = foo;
		this.elements = this.foo.getJSONArray("elements");
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
			if (element.getString("type").equals("way")) {
				String name = element.getJSONObject("tags").getString("name");
				ways.add(name);
			}
		}
		JSONObject result = new JSONObject();
		result.put("names", ways.toArray());
		return result;
	}

	public JSONObject getWays() throws JSONException {
		Log.d(LCAT, "start getWays");
		JSONArray ways = new JSONArray();
		for (int i = 0; i < elements.length(); i++) {
			JSONObject element = elements.getJSONObject(i);
			if (element.getString("type").equals("way")) {
				JSONArray points = new JSONArray();
				JSONObject way = new JSONObject();
				way.put("tags", element.getJSONObject("tags"));
				JSONArray nodeIds = element.getJSONArray("nodes");
				for (int j = 0; j < nodeIds.length(); j++) {
					String id = "" + nodeIds.getInt(j);
					points.put(getPointById(id));
				}
				concatArray(ways, points);
				way.put("points", points);
				ways.put(way);
			}
		}
		JSONObject result = new JSONObject();
		result.put("streets", ways);
		return result;
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

	private JSONObject getPointById(String id) throws JSONException {
		JSONObject node = this.nodes.getJSONObject(id);
		return new JSONObject("{latitude:" + node.getDouble("lat")
				+ ",longitude:" + node.getDouble("lon") + "}");
	}

	private JSONObject getNodes() throws JSONException {
		JSONObject bar = new JSONObject();
		for (int i = 0; i < elements.length(); i++) {
			JSONObject node = elements.getJSONObject(i);
			if (node.getString("type").equals("node")) {
				bar.put("" + node.getInt("id"), node);
			}
		}
		return bar;
	}

}
