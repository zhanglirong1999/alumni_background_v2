package cn.edu.seu.alumni_background.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class WebResponse {

	private Integer status;
	private String message;
	private Object data;

	public WebResponse() {
		setAll(WebResponseStatus.DEFAULT_SUCCESS, SuccessMessage.DEFAULT_SUCCESS_MESSAGE, null);
	}

	public WebResponse setAll(Integer s, String m, Object d) {
		status = s;
		message = m;
		data = d;
		return this;
	}

	public WebResponse success(String m, Object d) {
		return setAll(WebResponseStatus.DEFAULT_SUCCESS, m, d);
	}

	public WebResponse success(Object d) {
		return setAll(WebResponseStatus.DEFAULT_SUCCESS, SuccessMessage.DEFAULT_SUCCESS_MESSAGE, d);
	}

	public WebResponse failure(Integer s, String m, Object d) {
		return setAll(s, m, d);
	}

	public WebResponse failure(Integer s, String m) {
		return setAll(s, m, "");
	}

	public WebResponse failure(String m) {
		return setAll(WebResponseStatus.DEFAULT_ERROR, m, null);
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SuccessMessage {
		public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功!";
		private String successMessage = DEFAULT_SUCCESS_MESSAGE;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class FailureMessage {
		public static final String DEFAULT_FAILURE_MESSAGE = "请求失败!";
		private String failureMessage = DEFAULT_FAILURE_MESSAGE;
	}

	public static class WebResponseStatus {
		public static final Integer DEFAULT_SUCCESS = 200;
		public static final Integer DEFAULT_ERROR = 400;
		public static final Integer LOGIN_ERROR = 401;
		public static final Integer AUTH_ERROR = 403;
		public static final Integer NOT_FOUND_ERROR = 404;
		public static final Integer SERVER_ERROR = 500;
	}

	public static WebResponse wrap(Object object) {
		if (object instanceof WebResponse) {
			return (WebResponse) object;
		} else if (object instanceof FailureMessage) {
			return new WebResponse().failure(((FailureMessage) object).failureMessage);
		} else if (object instanceof SuccessMessage) {
			return new WebResponse().success(((SuccessMessage) object).successMessage, null);
		} else {
			// 如果返回一个 Object 默认为成功的 data
			return new WebResponse().success(object);
		}
	}
}
