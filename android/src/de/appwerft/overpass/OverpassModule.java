/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package de.appwerft.overpass;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiProperties;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

@Kroll.module(name = "Overpass", id = "de.appwerft.overpass")
public class OverpassModule extends KrollModule {

	public static final String LCAT = "OverPass 🐌🐜";
	@Kroll.constant
	public static final String ENDPOINT_MAIN = "http://overpass-api.de/api/";
	@Kroll.constant
	public static final String ENDPOINT_RAMBLER = "http://overpass.osm.rambler.ru/cgi/";
	@Kroll.constant
	public static final String ENDPOINT_FRENCH = "http://api.openstreetmap.fr/oapi/interpreter/";
	private String ENDPOINT = ENDPOINT_FRENCH;
	private int TIMEOUT = 120000;
	private String out = "body";
	private KrollFunction onResult;

	public OverpassModule() {
		super();
		TiProperties appProps = TiApplication.getInstance().getAppProperties();
		if (appProps.hasProperty("OVERPASS_ENDPOINT"))
			ENDPOINT = appProps.getString("OVERPASS_ENDPOINT", ENDPOINT_FRENCH);
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {

		Log.d(LCAT, "inside onAppCreate");
	}

	@Kroll.method
	public void createRequest(String query, String out, Object res)
			throws UnsupportedEncodingException {
		if (out != null && (out == "skel" || out == "meta" || out == "body")) {
			this.out = out;
		}
		if (res != null & res instanceof KrollFunction)
			onResult = (KrollFunction) res;
		doQuery(query, null);
	}

	@Kroll.method
	public void getBody(String query, Object res)
			throws UnsupportedEncodingException {
		this.out = "body";
		if (res != null & res instanceof KrollFunction)
			onResult = (KrollFunction) res;
		doQuery(query, null);
	}

	@Kroll.method
	public void getSkel(String query, Object res)
			throws UnsupportedEncodingException {
		this.out = "skel";
		if (res != null & res instanceof KrollFunction)
			onResult = (KrollFunction) res;
		doQuery(query, null);
	}

	@Kroll.method
	public void getMeta(String query, Object res)
			throws UnsupportedEncodingException {
		this.out = "meta";
		if (res != null & res instanceof KrollFunction)
			onResult = (KrollFunction) res;
		doQuery(query, null);
	}

	@Kroll.method
	public void getStreetsByPosition(KrollDict options, Object res)
			throws UnsupportedEncodingException {
		if (res != null && res instanceof KrollFunction)
			onResult = (KrollFunction) res;
		else
			Log.e(LCAT, "getStreetsByPosition has no callback for result");
		double radius = 1000.0;
		if (options.containsKeyAndNotNull(TiC.PROPERTY_LATITUDE)) {
			double lat = options.getDouble(TiC.PROPERTY_LATITUDE);
			if (options.containsKeyAndNotNull(TiC.PROPERTY_LONGITUDE)) {
				double lon = options.getDouble(TiC.PROPERTY_LONGITUDE);
				if (options.containsKeyAndNotNull("radius")) {
					radius = options.getDouble("radius");
				}
				String query = "way[highway][name](around:" + radius + ","
						+ lat + "," + lon + ");(._;>;);";
				doQuery(query, "getWays");
			}
		}
	}

	@Kroll.method
	public void getStreetNamesByPosition(KrollDict options, Object res)
			throws UnsupportedEncodingException {
		if (res != null && res instanceof KrollFunction)
			onResult = (KrollFunction) res;
		else
			Log.e(LCAT, "getStreetsByPosition has no callback for result");
		double radius = 1000.0;
		if (options.containsKeyAndNotNull(TiC.PROPERTY_LATITUDE)) {
			double lat = options.getDouble(TiC.PROPERTY_LATITUDE);
			if (options.containsKeyAndNotNull(TiC.PROPERTY_LONGITUDE)) {
				double lon = options.getDouble(TiC.PROPERTY_LONGITUDE);
				if (options.containsKeyAndNotNull("radius")) {
					radius = options.getDouble("radius");
				}
				String query = "way[highway][name](around:" + radius + ","
						+ lat + "," + lon + ");(._;>;);";
				doQuery(query, "getStreetNames");
			}
		}
	}

	@Kroll.method
	public void getAmenitiesByPosition(KrollDict options, Object res)
			throws UnsupportedEncodingException {
		if (res != null & res instanceof KrollFunction)
			onResult = (KrollFunction) res;
		double radius = 1000.0;
		String[] types = {};
		if (options.containsKeyAndNotNull(TiC.PROPERTY_LATITUDE)) {
			double lat = options.getDouble(TiC.PROPERTY_LATITUDE);
			if (options.containsKeyAndNotNull(TiC.PROPERTY_LONGITUDE)) {
				double lon = options.getDouble(TiC.PROPERTY_LONGITUDE);
				if (options.containsKeyAndNotNull("radius")) {
					radius = options.getDouble("radius");
				}
				if (options.containsKeyAndNotNull("types")) {
					types = options.getStringArray("types");
				}
				String typesString = StringUtils.join(types, "|");
				String query = "node[~\"^(amenity)$\"~(" + typesString
						+ ")(around:" + radius + "," + lat + "," + lon
						+ ");(._;>;)";
				Log.d(LCAT, "overpass-Query: " + query);
				doQuery(query, null);
			}
		}

	}

	@Kroll.method
	public void getPOIs(KrollDict options, Object res)
			throws UnsupportedEncodingException {
		if (res != null & res instanceof KrollFunction)
			onResult = (KrollFunction) res;
		for (String key : options.keySet()) {
			if (key.equals("bbx")) {
				Float[] coords = toFloatArray(options.get("bbx"));
			} else {

			}
		}

	}

	private Float[] toFloatArray(Object objectArray) {
		if (!(objectArray.getClass().isArray())) {
			throw new IllegalArgumentException("bbx must be an array");
		}

		Object[] coordsArray = (Object[]) objectArray;
		Float[] floatArray = Arrays.copyOf(coordsArray, coordsArray.length,
				Float[].class);
		/*
		 * float[] coords = new float[coordsArray.length];
		 * 
		 * for (int i = 0; i < coordsArray.length; i++) { coords[i] = ((Number)
		 * coordsArray[i]).floatValue(); }
		 */
		return floatArray;
	}

	private void doQuery(String query, String postProcess)
			throws UnsupportedEncodingException {
		Log.d(LCAT, query);
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(TIMEOUT);
		String url = ENDPOINT + "?data=[out:json];"
				+ URLEncoder.encode(query + "out " + out + ";", "UTF-8");
		Log.d(LCAT, url);
		RequestParams params = null;
		OverpassResponseHandler respHandler = new OverpassResponseHandler(
				getKrollObject(), onResult, postProcess);
		client.get(url, params, respHandler);
	};

	@Kroll.setProperty
	public void setEndpoint(String ep) {
		this.ENDPOINT = ep;
	}

}
