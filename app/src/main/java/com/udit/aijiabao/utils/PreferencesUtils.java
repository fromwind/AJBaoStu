package com.udit.aijiabao.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PreferencesUtils, easy to get or put data
 * <ul>
 * <strong>Preference Name</strong>
 * <li>you can change preference name by {@link #PREFERENCE_NAME}</li>
 * </ul>
 * <ul>
 * <strong>Put Value</strong>
 * <li>put string {@link #putString(Context, String, String)}</li>
 * <li>put int {@link #putInt(Context, String, int)}</li>
 * <li>put long {@link #putLong(Context, String, long)}</li>
 * <li>put float {@link #putFloat(Context, String, float)}</li>
 * <li>put boolean {@link #putBoolean(Context, String, boolean)}</li>
 * </ul>
 * <ul>
 * <strong>Get Value</strong>
 * <li>get string {@link #getString(Context, String)}, {@link #getString(Context, String, String)}</li>
 * <li>get int {@link #getInt(Context, String)}, {@link #getInt(Context, String, int)}</li>
 * <li>get long {@link #getLong(Context, String)}, {@link #getLong(Context, String, long)}</li>
 * <li>get float {@link #getFloat(Context, String)}, {@link #getFloat(Context, String, float)}</li>
 * <li>get boolean {@link #getBoolean(Context, String)}, {@link #getBoolean(Context, String, boolean)}</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-3-6
 */
public class PreferencesUtils {

	private static String SHARED_PREFERENCES_NAME = "user";

	private static SharedPreferences mPreferences;

	private PreferencesUtils() {
		throw new AssertionError();
	}

	public static void init(Context context) {
		mPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * put string preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putString(String key, String value) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * get string preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or null. Throws ClassCastException if there is a preference with this
	 * name that is not a string
	 * @see #getString(Context, String, String)
	 */
	public static String getString(String key) {
		return getString(key, null);
	}

	/**
	 * get string preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a string
	 */
	public static String getString(String key, String defaultValue) {
		return mPreferences.getString(key, defaultValue);
	}

	/**
	 * put int preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putInt(String key, int value) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	/**
	 * get int preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
	 * name that is not a int
	 * @see #getInt(Context, String, int)
	 */
	public static int getInt(String key) {
		return getInt(key, -1);
	}

	/**
	 * get int preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a int
	 */
	public static int getInt(String key, int defaultValue) {
		return mPreferences.getInt(key, defaultValue);
	}

	/**
	 * put long preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putLong(String key, long value) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	/**
	 * get long preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
	 * name that is not a long
	 * @see #getLong(Context, String, long)
	 */
	public static long getLong(String key) {
		return getLong(key, -1);
	}

	/**
	 * get long preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a long
	 */
	public static long getLong(String key, long defaultValue) {
		return mPreferences.getLong(key, defaultValue);
	}

	/**
	 * put float preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putFloat(String key, float value) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	/**
	 * get float preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
	 * name that is not a float
	 * @see #getFloat(Context, String, float)
	 */
	public static float getFloat(String key) {
		return getFloat(key, -1);
	}

	/**
	 * get float preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a float
	 */
	public static float getFloat(String key, float defaultValue) {
		return mPreferences.getFloat(key, defaultValue);
	}

	/**
	 * put boolean preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	/**
	 * get boolean preferences, default is false
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
	 * name that is not a boolean
	 * @see #getBoolean(Context, String, boolean)
	 */
	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	/**
	 * get boolean preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a boolean
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		return mPreferences.getBoolean(key, defaultValue);
	}
}
