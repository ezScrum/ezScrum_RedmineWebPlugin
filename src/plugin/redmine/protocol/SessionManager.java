package plugin.redmine.protocol;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

public class SessionManager {

	/**
	 * 從request中取回PID
	 * @param request
	 * @return
	 */
	public static final String getProjectID(HttpServletRequest request) {
		// 拿到request header的URL parameter
		String projectID = getURLParameter(request, "PID");
		return projectID;
	}

	/**
	 * 傳入request與參數名稱，拿到request header的URL parameter
	 * @param request client端傳上來的request
	 * @param paramName 要取得的參數名稱
	 */
	private static String getURLParameter(HttpServletRequest request, String paramName) {
		try {
			/**
			 * 拿到request header的URL parameter
			 * 如果拿不到referer資訊此時回傳null
			 */
			String referer = request.getHeader("Referer");
			if (referer == null) 
				return null;
			
			String url = URLDecoder.decode(referer, "UTF-8");	// 將URL轉換成沒有萬用字元符號
			
			/**
			 * 如果用 "?"拆除出來的params是null或只有一個urlParams 代表網址後面沒有帶參數
			 * 此時回傳null
			 */
			String[] urlParams = url.split("\\?");
			if (urlParams == null || urlParams.length <= 1) 
				return null;
			
			
			url = urlParams[1];		// 取得URL 問號後面的Parameter參數
			String[] params = url.split("&");	// params 裡面存的大概像這樣 [{id=15}, {op=55}]，為各個parameter 
			String[] value;			// value 用來接params的 parameter的key跟value  ex: [id, 15]
			
			/**
			 * 找到對應的parameter name並把value回傳給使用者
			 */
			for (int i = 0; i < params.length; i++) {
				value = params[i].split("=");
				if (value[0].equals(paramName)) {
					return value[1];
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return paramName;
	}
}
