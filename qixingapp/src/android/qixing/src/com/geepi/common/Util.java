package com.geepi.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;


public class Util {
	private static String TAG = "Util";

	private static String vid;
	private static String targetVersion;
	private static String versionName;
	private static String SDCardPath;
	private static String timeStamp;
	private static boolean isMounted = false;
	private static boolean isSdCardWrittenable = false;
	private static boolean checkAvailableStorage = false;
	private static long storageAvaliableSize = -1;
	private static String timeStr;


	/**
	 * 拿到当前安卓版本
	 * 
	 * @return targetVersion String
	 */
	public static String getTargetVersion() {
		targetVersion = "Android";
		targetVersion += android.os.Build.VERSION.RELEASE;
		return targetVersion;
	}

	public static String getAppRootPathSize() {
		return "";
	}
	


	public static int getDeviceDpi(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
		return densityDpi;
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}


	/**
	 * 判断SD卡是否mounted
	 * 
	 * @return isMounted boolean 已挂载SDCard返回true，否则返回false
	 */
	public static boolean isMounted() {
		isMounted = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		return isMounted;
	}

	/**
	 * 判断SD卡是否可写入
	 * 
	 * @return isSdCardWrittenable boolean SDCard可写则返回true，否则返回false
	 */
	public static boolean isSdCardWrittenable() {
		isSdCardWrittenable = Environment.getExternalStorageDirectory()
				.canWrite();
		return isSdCardWrittenable;
	}


	public static boolean isStringValid(String str) {
		return str != null && !str.trim().equals("");
	}

	public static boolean isFilePathValid(String filePath)
			throws MalformedURLException {
		boolean isFilePathValid = false;
		filePath = filePath.replaceAll("\\\\", "/").trim();
		Pattern p = Pattern.compile("(^\\.|^/|^[a-zA-Z])?:?/.+(/$)?");
		Matcher m = p.matcher(filePath);
		// 不符合要求直接返回
		if (!m.matches()) {
			Log.v(TAG, "文件路径无效！");
			throw new MalformedURLException("文件路径无效！");
		} else {
			isFilePathValid = true;
		}
		return isFilePathValid;

	}

	/**
	 * 传一个url进去，解析一个fileName出来
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileNameFromUrl(String url) {
		// 通过 ‘？’ 和 ‘/’ 判断文件名
		int index = url.lastIndexOf('?');
		String filename;
		if (index > 1) {
			filename = url.substring(url.lastIndexOf('/') + 1, index);
		} else {
			filename = url.substring(url.lastIndexOf('/') + 1);
		}

		if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
		// filename = UUID.randomUUID() + ".apk";// 默认取一个文件名
			filename = "";
		}
		return filename;
	}

	/**
	 * 生成流量统计的字符（有待研究）
	 * 
	 * @param size
	 * @return
	 */
	public static String size(long size) {

		if (size / (1024 * 1024) > 0) {
			float tmpSize = (float) (size) / (float) (1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "MB";
		} else if (size / 1024 > 0) {
			return "" + (size / (1024)) + "KB";
		} else
			return "" + size + "B";
	}

	/**
	 * JSONArray解析并生成bean
	 * 
	 * @param t
	 * @param jsonArray
	 * @return t
	 */
	public static <T> Class<T> jsonPlayer(Class<T> t, JSONArray jsonArray) {
		try {
			JSONObject jsonObj = jsonArray.toJSONObject(jsonArray);
			// Iterator<String> iter =jsonObj.keys();
			// String response = "";
			// while(iter.hasNext()){
			// response = iter.next();
			// }
			// Log.v(TAG,"response:"+response);
			// if(response.equals("Response")){
			//
			// }else if(response.equals("error_response")){
			// Log.v(TAG,"error_response:"+response);
			// }
			if (!jsonObj.isNull("Response")) {
				JSONArray ja = jsonObj.optJSONArray("Response");
				if (ja == null) {// 没有JSONArray，只有String

				} else {// 没有String，只有JSONArray

				}
			} else if (!jsonObj.isNull("error_response")) {
				Log.v(TAG, "error_response:"
						+ jsonObj.get("error_response").toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return t;

	}

	/**
	 * 反序列化泛型集合
	 * 
	 * @param ja
	 *            JSONArray
	 * @param genericType
	 *            Class<T> 实体类类型
	 * @return List<T> 封装了实体类对象的List<T>
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> jsonParseCollection(Object jsonObject,
			Class<T> genericType) throws JSONException {

		Object o = jsonObject;
		if (o == null) {
			LogUtil.e("JSON传入的数据不合法");
			return new ArrayList<T>();
		}

		if (o instanceof JSONObject) {
			List<T> list = new ArrayList<T>();
			T object = JsonHelper.parseObjectWithAnno((JSONObject) o,
					genericType);
			list.add(object);
			return list;
		} else if (o instanceof JSONArray) {
			return (List<T>) JsonHelper.parseCollectionWithAnnotation(
					(JSONArray) o, List.class, genericType);
		}
		return new ArrayList<T>();
	}


	public static boolean isNetworkUrl(String url) {
		return URLUtil.isNetworkUrl(url);
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 返回时间戳
	 * 
	 * @return timeStamp String 利用new Date().getTime()生成 及时时间戳
	 */
	public static String getTimeStamp() {
		timeStamp = new Date().getTime() + "";
		return timeStamp;
	}

	/**
	 * 返回用户账户
	 * 
	 * @return userName String
	 */
	public static String getUserName() {
		return "18670388231";
	}


	/**
	 * 判断对象是否为空
	 * 
	 * @param obj
	 *            实例
	 * @return
	 */
	public static boolean isNull(Object obj) {
		if (obj instanceof JSONObject) {
			return JSONObject.NULL.equals(obj);
		}
		return obj == null;
	}

	/**
	 * 判断是否是值类型
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isSingle(Class<?> clazz) {
		return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
	}

	/**
	 * 是否布尔值
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isBoolean(Class<?> clazz) {
		return (clazz != null)
				&& ((Boolean.TYPE.isAssignableFrom(clazz)) || (Boolean.class
						.isAssignableFrom(clazz)));
	}

	/**
	 * 是否数值
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isNumber(Class<?> clazz) {
		return (clazz != null)
				&& ((Byte.TYPE.isAssignableFrom(clazz))
						|| (Short.TYPE.isAssignableFrom(clazz))
						|| (Integer.TYPE.isAssignableFrom(clazz))
						|| (Long.TYPE.isAssignableFrom(clazz))
						|| (Float.TYPE.isAssignableFrom(clazz))
						|| (Double.TYPE.isAssignableFrom(clazz)) || (Number.class
							.isAssignableFrom(clazz)));
	}

	/**
	 * 判断是否是字符串
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isString(Class<?> clazz) {
		return (clazz != null)
				&& ((String.class.isAssignableFrom(clazz))
						|| (Character.TYPE.isAssignableFrom(clazz)) || (Character.class
							.isAssignableFrom(clazz)));
	}

	/**
	 * 判断是否是对象
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isObject(Class<?> clazz) {
		return clazz != null && !isSingle(clazz) && !isArray(clazz)
				&& !isCollection(clazz) && !isMap(clazz);
	}

	/**
	 * 判断是否是数组
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isArray(Class<?> clazz) {
		return clazz != null && clazz.isArray();
	}

	/**
	 * 判断是否是集合
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isCollection(Class<?> clazz) {
		return clazz != null && Collection.class.isAssignableFrom(clazz);
	}

	/**
	 * 判断是否是Map
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isMap(Class<?> clazz) {
		return clazz != null && Map.class.isAssignableFrom(clazz);
	}

	/**
	 * 判断是否是列表
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isList(Class<?> clazz) {
		return clazz != null && List.class.isAssignableFrom(clazz);
	}

	/**
	 * 判断是否存在某属性的 get方法
	 * 
	 * @param methods
	 *            引用方法的数组
	 * @param fieldMethod
	 *            方法名称
	 * @return true或者false
	 */
	public static boolean haveMethod(Method[] methods, String fieldMethod) {
		for (Method met : methods) {
			if (fieldMethod.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 拼接某属性的 get或者set方法
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param methodType
	 *            方法类型
	 * @return 方法名称
	 */
	public static String parseMethodName(String fieldName, String methodType) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return methodType + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}

	public static Bitmap getLoacalBitmap(String url) {

		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据提供的Date对象生成标准字符串
	 * 
	 * @param date
	 *            java.util.Date
	 * @return timeStr String 转换后的字符串 yyyy-M-dd h:mm:ss 以东8为准
	 */
	public static String getTimeStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d h:mm:ss",
				Locale.CHINA);
		timeStr = sdf.format(date);
		return timeStr;
	}

	/**
	 * 根据提供的java.sql.Date对象生成标准字符串
	 * 
	 * @param date
	 *            java.sql.Date
	 * @return timeStr String 转换后的字符串 yyyy-M-dd h:mm:ss 以东8为准
	 */
	public static String getTimeStr(java.sql.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d h:mm:ss",
				Locale.CHINA);
		timeStr = sdf.format(date);
		return timeStr;
	}

	public static String convertNumberToMonth(int a) {
		String monthNum = "零";
		switch (a) {
		case 1:
			monthNum = "一";
			break;
		case 2:
			monthNum = "二";
			break;
		case 3:
			monthNum = "三";
			break;
		case 4:
			monthNum = "四";
			break;
		case 5:
			monthNum = "五";
			break;
		case 6:
			monthNum = "六";
			break;
		case 7:
			monthNum = "七";
			break;
		case 8:
			monthNum = "八";
			break;
		case 9:
			monthNum = "九";
			break;
		case 10:
			monthNum = "十";
			break;
		case 11:
			monthNum = "十一";
			break;
		case 12:
			monthNum = "十二";
			break;
		default:
			break;
		}

		return monthNum;
	}

	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	/*
	 * json 相关的static 方法
	 */
	/**
	 * Json 转成 Map<>
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> getMapForJson(JSONObject jsonObject) {
		try {

			Iterator<String> keyIter = jsonObject.keys();
			String key;
			Object value;
			Map<String, Object> valueMap = new HashMap<String, Object>();
			while (keyIter.hasNext()) {
				key = keyIter.next();
				value = jsonObject.get(key);
				valueMap.put(key, value);
			}
			return valueMap;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static void sendBroadcastNotify(Context mcontext,
			String broadcastName, String str) {
		Intent intent = new Intent();
		// 设置Action
		intent.setAction(broadcastName);
		// 发送广播
		mcontext.sendBroadcast(intent);
	}

	public static Bitmap getBitmapFromView(View view, int width, int height) {
		// Define a bitmap with the same size as the view
		Bitmap returnedBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// Bind a canvas to it
		Canvas canvas = new Canvas(returnedBitmap);
		// Get the view's background
		Drawable bgDrawable = view.getBackground();
		if (bgDrawable != null)
			// has background drawable, then draw it on the canvas
			bgDrawable.draw(canvas);
		else
			// does not have background drawable, then draw white background on
			// the canvas
			canvas.drawColor(Color.WHITE);
		// draw the view on the canvas
		view.draw(canvas);
		// return the bitmap
		return returnedBitmap;
	}

	public static void saveViewToFile(View view, String filePath, int width,
			int height) {
		view.setDrawingCacheEnabled(true);
		Bitmap b = Util.getBitmapFromView(view, width, height);
		try {
			b.compress(CompressFormat.JPEG, 95, new FileOutputStream(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
