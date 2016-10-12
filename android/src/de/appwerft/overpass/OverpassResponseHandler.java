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
	private String postProcessMethodName = "passThrue";

	public OverpassResponseHandler(KrollObject o, KrollFunction cb,
			String postProcess) {
		krollObject = o;
		onResult = cb;
		postProcessMethodName = postProcess;
		startTime = System.currentTimeMillis();
	}

	public void setPostProcess(String postProcess) {
		this.postProcessMethodName = postProcess;
	}

	public onResultListener resultListener;

	public interface onResultListener {
		public void onResult(KrollDict dict);
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
					if (obj instanceof JSONObject) {
						KrollDict res = new KrollDict();
						res.put("success", true);
						res.put("result", (JSONObject) obj);
						onResult.call(krollObject, res);
					}
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
}
