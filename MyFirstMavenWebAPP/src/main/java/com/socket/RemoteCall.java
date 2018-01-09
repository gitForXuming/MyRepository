package com.socket;

import com.base.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.listener.AppContextInit;

public class RemoteCall {
	private static Gson moppGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").excludeFieldsWithoutExposeAnnotation().create();;
	
	private static TcpLink tcpLink = null;
	private static SSLTrustManager sslTrustManager = null;
	static{
		try{
			tcpLink =TcpLink.createInstance("127.0.0.1:3600");
		}catch(Exception e){
			System.out.println("连接服务端失败");
		}
	}
	
	public static void main(String[] args) {
		JsonObject json = new JsonObject();
		json.addProperty("cstNo",  "123456");
		json.addProperty("accType",  "1");
		json.addProperty("pageNo",  "2");
		execute("mbank", "mBank/queryPayeeList", json, new Callback() {
			
			@Override
			public void onExcept(int errCode, String errMessage) {
				System.out.println(String.format("交易处理失败，错误码：%d,错误信息：%s", errCode ,errMessage));
				
			}
			
			@Override
			public void onCompleted(JsonObject response) {
				System.out.println(String.format("交易处理成功,返回信息：%s",response.toString()));
				
			}
		});
	}
	
	/**
	 * 异步调用后台服务的回调对象。
	 * 
	 * @author Administrator
	 * 
	 */
	public static interface Callback {
		/**
		 * 从服务端调用成功，正常获得响应。注意：这个成功只代表了客户端正确获取了服务端的响应信息，并不代表真正的交易成功。
		 * 该方法总是运行于调用者（即RemoteCall.execute）所在的线程中。
		 * 
		 * @param response
		 *            用json封装好的的服务端响应数据。
		 */
		public void onCompleted(JsonObject response);

		/**
		 * 调用服务端过程中出现异常，请参阅class com.softbank.base.ExceptionWithCode。
		 * 该方法总是运行于调用者（即RemoteCall.execute）所在的线程中。
		 * 
		 * @param errCode
		 *            错误代码。
		 * @param message
		 *            错误描述。
		 */
		public void onExcept(int errCode, String errMessage);
	}
	
	@SuppressWarnings("unused")
	private static class RequestObject {

		@Expose
		@SerializedName("class")
		private String className;

		@Expose
		private String action;

		@Expose
		private String session;

		@Expose
		private JsonObject body;

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getSession() {
			return session;
		}

		public void setSession(String session) {
			this.session = session;
		}

		public JsonObject getBody() {
			return body;
		}

		public void setBody(JsonObject body) {
			this.body = body;
		}
	}

	@SuppressWarnings("unused")
	private static class ResponseObject {

		@Expose
		private String result;

		@Expose
		private String session;

		@Expose
		private JsonObject body;

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public String getSession() {
			return session;
		}

		public void setSession(String session) {
			this.session = session;
		}

		public JsonObject getBody() {
			return body;
		}

		public void setBody(JsonObject body) {
			this.body = body;
		}
	}
	
	/**
	 * 异步调用后台服务。
	 * 
	 * @param className
	 *            服务类别名称
	 * 
	 * @param actionName
	 *            action名称，在服务端对应请求的url
	 * @param request
	 *            请求参数，为封装好的json对象
	 * @param callback
	 *            回调接口，见Callback定义
	 * @throws
	 */
	public static void execute(String className, final String actionName,
			JsonObject request, final Callback callback) {
		try {
			if (actionName == null) {
				throw new Exception("only test");
			}

			if (request == null) {
				request = new JsonObject();
			}
			
			request.addProperty("$aapId", "1");
			RequestObject reqObj = new RequestObject();
			reqObj.setClassName(className);
			reqObj.setAction(actionName);
			reqObj.setSession("123");
			reqObj.setBody((JsonObject) request);
			MoppPackage reqPkg = new MoppPackage();
			
			reqPkg.setBody((JsonObject)moppGson
					.toJsonTree(reqObj));
			
			tcpLink.postToServer(reqPkg, new TcpLink.Callback() {
				
				@Override
				public void onReceive(MoppPackage response) {
					callback.onCompleted(response.getBody());
				}
				
				@Override
				public void onExcept(int errCode, String errMessage) {
					callback.onExcept(errCode, errMessage);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}	
