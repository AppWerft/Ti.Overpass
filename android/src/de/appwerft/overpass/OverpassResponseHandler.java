package de.appwerft.overpass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

	public OverpassResponseHandler(KrollObject o, KrollFunction cb,
			String postProcessMethodName) {
		krollObject = o;
		onResult = cb;
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

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		if (onResult != null) {
			Log.d(LCAT, "try to start postprocess >>>>>>>> "
					+ postProcessMethodName);
			PostProcess postProcess;
			try {
				postProcess = new PostProcess(response);
				Method method = null;
				try {
					method = postProcess.getClass().getMethod(
							postProcessMethodName);
				} catch (SecurityException e) {
				} catch (NoSuchMethodException e) {
				}
				try {
					Object obj = method.invoke(postProcess);
					KrollDict res = new KrollDict();
					res.put("success", true);
					if (obj instanceof JSONObject) {
						res.put("result", new KrollDict((JSONObject) obj));
					} else if (obj instanceof JSONArray) {
						JSONArray jArray = (JSONArray) obj;
						res.put("result", jArray);
					}
					onResult.call(krollObject, res);
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

		}
	}
}
