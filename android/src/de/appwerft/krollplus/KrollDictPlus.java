package de.appwerft.krollplus;

import org.appcelerator.kroll.KrollDict;

@SuppressWarnings("serial")
public class KrollDictPlus extends KrollDict {
	public static float[] getFloatArray(Object[] inArray) {
		float[] outArray = new float[inArray.length];
		for (int i = 0; i < inArray.length; i++) {
			outArray[i] = ((Number) inArray[i]).floatValue();
		}
		return outArray;
	}

}
