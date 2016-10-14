package de.appwerft.overpass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollObject;
import org.appcelerator.kroll.common.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public final class OverpassResponseHandler extends JsonHttpResponseHandler {
	private KrollObject krollObject;
	private KrollFunction onResult;
	private String LCAT = OverpassModule.LCAT;
	private long startTime;
	final private String DEFAULTPOSTPROCESS = "passThrough";
	private String postProcessMethodName = DEFAULTPOSTPROCESS;

	public OverpassResponseHandler(KrollObject o, String postProcessMethodName,
			KrollFunction callback) {
		krollObject = o;
		onResult = callback;
		if (postProcessMethodName != null) // in out case default "passthrough"
			this.postProcessMethodName = postProcessMethodName;
		startTime = System.currentTimeMillis();
		Log.d(LCAT, "postProcessMethodName=" + postProcessMethodName);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, JSONObject errorResponse) {
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, JSONArray errorResponse) {
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			String responseString, Throwable throwable) {
		if (onResult != null) {
			Log.d(LCAT, "status=" + statusCode);
			KrollDict dict = new KrollDict();
			dict.put("status", statusCode);
			if (System.currentTimeMillis() - startTime < 100) {
				dict.put("error", "offline");
				dict.put("message", "Host not reachable");
			} else {
				dict.put("error", "timeout");
				dict.put("time", "" + (System.currentTimeMillis() - startTime));
				dict.put("message", "Server don't answer in 30 sec. ");
			}
			onResult.call(krollObject, dict);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		if (onResult != null) {
			Log.d(LCAT, "try to start postprocess >>>>>>>> "
					+ postProcessMethodName);
			PostProcess postProcess;
			try {
				postProcess = new PostProcess(response);
				Method method = null;
				KrollDict res = new KrollDict();
				res.put("success", true);
				try {
					method = postProcess.getClass().getMethod(
							postProcessMethodName);
					try {
						Object obj = method.invoke(postProcess);
						if (obj instanceof JSONObject) {
							res.put("result", new KrollDict((JSONObject) obj));
						} else if (obj instanceof HashSet) {
							res.put("result", ((HashSet<String>) obj).toArray());
						} else if (obj instanceof JSONArray) {
							JSONArray jArray = (JSONArray) obj;
							HashSet<KrollDict> hashSet = new HashSet<KrollDict>();
							for (int i = 0; i < jArray.length(); i++)
								hashSet.add(new KrollDict((JSONObject) jArray
										.get(i)));
							jArray = null;
							res.put("result", hashSet.toArray());
						}
						onResult.call(krollObject, res);
					} catch (IllegalArgumentException e) {
						Log.d(LCAT, e.getMessage());
					} catch (IllegalAccessException e) {
						Log.d(LCAT, e.getMessage());
					} catch (InvocationTargetException e) {
						Log.d(LCAT, e.getMessage());
					}

				} catch (SecurityException e) {
					Log.d(LCAT, e.getMessage());
				} catch (NoSuchMethodException e) {
					Log.d(LCAT, e.getMessage());
				}

			} catch (JSONException e1) {
				e1.printStackTrace();
			}

		}
	}
}
