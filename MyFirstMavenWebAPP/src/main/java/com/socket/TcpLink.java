package com.socket;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class TcpLink {
	private static final String TAG="TcpLink";
	
	/**
	 * 名称：ERR_SUCCESS <br/>
	 * 类型：int <br/>
	 * 描述：通讯成功 <br/>
	 */
	public static final int ERR_SUCCESS = 0;

	/**
	 * 名称：ERR_NETWORK <br/>
	 * 类型：int <br/>
	 * 描述：网路故障,或调用网络模块。 <br/>
	 */
	public static final int ERR_NETWORK = 1;

	/**
	 * 名称：ERR_HOST_REFUSE <br/>
	 * 类型：int <br/>
	 * 描述：被远端主机积极拒绝 <br/>
	 */
	public static final int ERR_HOST_REFUSE = 2;

	/**
	 * 名称：ERR_RECV_TIMEOUT <br/>
	 * 类型：int <br/>
	 * 描述：接收超时 <br/>
	 */
	public static final int ERR_RECV_TIMEOUT = 3;

	/**
	 * 名称：ERR_IO <br/>
	 * 类型：int <br/>
	 * 描述：IO异常 <br/>
	 */
	public static final int ERR_IO = 4;

	/**
	 * 名称：ERR_SYS <br/>
	 * 类型：int <br/>
	 * 描述：系统故障 <br/>
	 */
	public static final int ERR_SYS = 9;
	
	private static final int CONNECT_TIMEOUT=10000;//连接超时时间
	private static final int RESPONSE_TIMEOUT=60000;//接收返回超时时间
	private static final int HEARTBEAT_PERIOD=300000;//心跳周期 
	private static final int MIN_RECONNECT_PERIOD = 5000; // 失败后重连周期
	private static final int MAX_RECONNECT_PERIOD = 60000; // 失败后重连周期
	private static final int RECONNECT_PERIOD_INCREMENT = 5000; // 重连增量 

	private static final int WARING_CONNECT_TIME = 3000; // 连接时间大于此值时报警
	private static final int WARING_SPEED = 1500; // 当实际传输速度小于此值时报警
	private static final int WARING_ABSOLUTE_TRANSPORT_TIME = 1000;
	public static String Encoding ="UTF-8";
	private JsonParser moppParser = new JsonParser();
	private Gson moppGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").excludeFieldsWithoutExposeAnnotation().create();;
	
	private static ExecutorService executor;
	private Socket socket =null;
	private InputStream in =null;
	private OutputStream out =null;
	private String ip= null;
	private int port = 0;
	private Thread recvThread = null;
	
	private ConcurrentMap<Integer, ActionInfo> rounds = new ConcurrentHashMap<Integer, ActionInfo>();
	private int curSerialNo = 1;
	
	private int reconnectPeriod = MIN_RECONNECT_PERIOD;
	private Timer timer = new Timer();
	
	/**
	 * 构造函数：TcpLLink <br/>
	 * 描述：构造Tcp长连接 <br/>
	 * 
	 * @param uri
	 *            服务端URI，格式为"服务端地址:服务方端口"。 <br/>
	 */
	protected TcpLink(String uri){
		String[] add = uri.split(":");
		this.ip = add[0];
		this.port = Integer.parseInt(add[1]);
	}
	
	public static TcpLink createInstance(String uri){
		final TcpLink link = new TcpLink(uri);
		executor = Executors.newSingleThreadExecutor();
		new Thread(){//通过线程去创建链接
			@Override
			public void run() {
				try{
					link.connect();
				}catch(Exception e){
					System.out.println("无法连接到服务端");
				}
				
			}
		};
		return link;
	}
	/**
	 * 方法名：connect <br/>
	 * 描述：连接服务器，如果对象不在连接状态，发送报文前会自动连接。 <br/>
	 * 
	 * @throws ExceptionWithCode
	 * <br/>
	 */
	protected synchronized void connect() throws Exception {
		if (recvThread != null) {
			return;
		}
		
		boolean connected = false;
		try{
			socket = createSocket();
			socket.setSoTimeout(HEARTBEAT_PERIOD);// read 方法的读超时  超时后 socket不关闭
			
			in = new ReceiveAndKeepAliveStream(socket.getInputStream());
			out = socket.getOutputStream();
			recvThread = new RecvThread();
			recvThread.start();		//后台起一个线程不断的去读取结果信息 该线程一直存在 直到socket关闭	
			
			connected = true;	
			
		}catch(Exception e){
			if(recvThread == null) {
				closeSocket();				
				this.notifyAll();
			} else {
				disconnect();
			}
		}finally{
			if(!connected) { //自动重连
				//Application.getInstance().writeBlackBox(TAG, String.format("连接失败, [%d]毫秒后重连", reconnectPeriod));	
				System.out.println(String.format("连接失败, [%d]毫秒后重连", reconnectPeriod));	
				
				timer.schedule(new TimerTask() {
						@Override
						public void run() {
							try {
								connect();
							} catch (Exception e) {
								System.out.println("无法连接服务器");
							}
						}
					}, reconnectPeriod);			
				if(reconnectPeriod < MAX_RECONNECT_PERIOD) {
					reconnectPeriod += RECONNECT_PERIOD_INCREMENT;
				}
				
			}
		}
	}
	/**
	 * 方法名：createSocket <br/>
	 * 描述：创建socket对象，并建立连接 <br/>
	 * 
	 * @return Socket对象
	 * @throws ExceptionWithCode
	 * <br/>
	 */
	protected Socket createSocket() throws Exception {
		try {
			InetSocketAddress address = new InetSocketAddress(ip, port);
			Socket result = new Socket();
			result.connect(address, CONNECT_TIMEOUT);//超時時間
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("连接失败");
		}
	}

	/**
	 * 方法名：close <br/>
	 * 描述：TODO <br/>
	 * <br/>
	 */
	protected void closeSocket(){
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("无法关闭输出流");
			}
			out = null;
		}
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("无法关闭输入流");
			}
			in = null;
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("无法关闭连接");
			}
			socket = null;
		}
	}	
	
	/**
	 * 重连接
	 */
	protected synchronized void disconnect() {
		try {
			if(socket != null) {
				socket.close();	
				socket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("无法关闭连接");
			return;
		}
	}
		
	private static class ActionInfo {
		public MoppPackage request = null;
		public long reqPkgLen = 0;
		public long reqWhen = 0;
		public MoppPackage response = null;
		public long rspPkgLen;
		public long rspWhen = 0;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	private class RecvThread extends Thread {
		@Override
		public void run() {
			int i=0;
			do{
				i++;
				System.out.println(String.format("测试接收结果进程是否还在循环：%d", i));
				MoppPackage pkg = new MoppPackage();
				int rspPkgLen = 0;
				try {
					rspPkgLen = pkg.loadFromStream(in);
				} catch (Exception e) {// 一般这个地方不会抛异常的 in 是通过 ReceiveAndKeepAliveStream 包了一层 一旦读超时则发起心跳包（此场景无限循环）
					 //如果其他异常就直接抛出（ 比如socket关闭了）重新连接
					//Application.getInstance().writeBlackBox(TAG, "接收失败[" + e.getMessage() + "]");
					System.out.println( "接收失败[" + e.getMessage() + "]");
					e.printStackTrace();
					break;
				}
				if(pkg.getHeader().getSequence() != 0){
					long rspWhen = System.currentTimeMillis();
					synchronized (TcpLink.this) {
						ActionInfo actionInfo = rounds.get(pkg.getHeader().getSequence());
						if (actionInfo == null) {
							continue;
						}
						actionInfo.response = pkg;
						actionInfo.rspPkgLen = rspPkgLen;
						actionInfo.rspWhen = rspWhen;
						TcpLink.this.notifyAll();
					}
				}
				
				
			}while(true);//改线程一直存在 不断的去后台读
		}
	}
	
	/**
	 * 方法名：sendToServer <br/>
	 * 描述：发送报文到服务器，并获得响应对象。如果不在连接状态，发送报文前会自动连接。 如果报文为服务端主动推送的请求，响应对象返回null。 <br/>
	 * 
	 * @param request
	 *            请求对象 <br/>
	 * @return 响应对象 <br/>
	 * @throws ExceptionWithCode
	 * <br/>
	 */
	public synchronized MoppPackage sendToServer(MoppPackage request)
			throws Exception{
		try{
			System.out.println("确保连接状态");
			connect();
	
			if (curSerialNo == 0) {
				++curSerialNo;
			}
			request.getHeader().setSequence(curSerialNo++);
			
			try{
				ActionInfo actionInfo = new ActionInfo();
				actionInfo.request = request;
				actionInfo.reqWhen = System.currentTimeMillis();
				
				try {
					actionInfo.reqPkgLen = actionInfo.request.saveToStream(out);
					out.flush();					
					rounds.put(request.getHeader().getSequence(), actionInfo);		
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception("发送失败", e);
				}
				System.out.println(String.format("开始等待时间戳[%d]毫秒" ,System.currentTimeMillis()));
				System.out.println(String.format("等待响应[%d]毫秒", RESPONSE_TIMEOUT));
				long endTime = System.currentTimeMillis() + RESPONSE_TIMEOUT;
				long remainTime = RESPONSE_TIMEOUT;
				
				do{
					if(rounds.size()==0){
						throw new Exception("未接收到响应前异常断开");
					}
					
					if(actionInfo.response != null){
						break; //说明已经接收到了请求  通过 recvThread 线程异步接收到请求
					}
					try {
						this.wait(remainTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
						throw new Exception("等待响应被中断");
					}
					
					remainTime = endTime - System.currentTimeMillis();
					
					if (remainTime <= 0) {	
						System.out.println(String.format("结束等待时间戳[%d]毫秒" ,System.currentTimeMillis()));
						throw new Exception("接收响应超时");
					}
				}while(true);
				
				ResponseObject rspObj = null;				
				try {					
					if(actionInfo.response.getBody() == null || 
							"success".equals((rspObj = moppGson.fromJson(
							actionInfo.response.getBody(), ResponseObject.class)).getResult())) {
						long transTime = actionInfo.rspWhen - actionInfo.reqWhen;
						if(rspObj != null) {
							transTime -= rspObj.procTime;
						}
						final boolean speedWarning = transTime > WARING_ABSOLUTE_TRANSPORT_TIME &&
								(actionInfo.reqPkgLen + actionInfo.rspPkgLen) * 1000 / transTime < WARING_SPEED;
						if(speedWarning) {
							System.out.println(String.format("传输字节：%d,传输时间:%d毫秒", actionInfo.reqPkgLen
									+ actionInfo.rspPkgLen, transTime));
						} 
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return actionInfo.response;
			} finally {
				rounds.remove(request.getHeader().getSequence());
			}
		}catch(Exception e){
			if(rounds.size() == 0) {
				disconnect();
			}
			throw e;
		}
	}
	

	/**
	 * 方法名：postToServer <br/>
	 * 描述：异步方式发送请求到服务器，返回时在发送者线程中回调接口。 <br/>
	 * 
	 * @param request
	 *            请求对象。 <br/>
	 * @param callback
	 *            收到响应时回调接口。 <br/>
	 */
	public  void postToServer(final MoppPackage request, Callback callback) {
	
		//异步完成通讯
		FutureTask<Object> task = new FutureTask<Object>(new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				MoppPackage result = sendToServer((MoppPackage) request);
				return result;
			}
		});
		executor.submit(task);
		//executor.shutdown();
		
		Object result =null;
		try{
			result = task.get(RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
		}catch(TimeoutException e){
			result = e;
		}catch(InterruptedException e){
			result = e;
		}catch(ExecutionException e){
			result = e;
		}
		
		if(result instanceof MoppPackage) {
			callback.onReceive((MoppPackage) result);
		}else if(result instanceof TimeoutException){
			callback.onExcept(1 ,"交易处理超时");
		}else if(result instanceof InterruptedException){
			callback.onExcept(2 ,"交易异常中断");
		}else{
			callback.onExcept(3 ,"交易处理失败");
		}
		
	}
	
	/**
	 * 类名称：Callback<br/>
	 * <br/>
	 * 类描述：异步发送时回调接口。 <br/>
	 */
	public interface Callback {
		/**
		 * 方法名：onReceive <br/>
		 * 描述：从服务端获取报文 <br/>
		 * 
		 * @param response
		 *            响应报文，如果服务端主动推送的请求，该值为null。 <br/>
		 */
		public void onReceive(MoppPackage response);

		/**
		 * 方法名：onExcept <br/>
		 * 描述：通讯时发生异常。 <br/>
		 * 
		 * @param errCode
		 *            异常码。 <br/>
		 * @param errMessage
		 *            异常信息 <br/>
		 */
		public void onExcept(int errCode, String errMessage);
	}

	@SuppressWarnings("unused")
	private static class ResponseObject {
	
		@Expose
		private String result;
	
		@Expose
		private long procTime;
	
		public String getResult() {
			return result;
		}
	
		public void setResult(String result) {
			this.result = result;
		}
	
		public long getProcTime() {
			return procTime;
		}
	
		public void setProcTime(long procTime) {
			this.procTime = procTime;
		}
	}

	
	private class ReceiveAndKeepAliveStream extends FilterInputStream{

		protected ReceiveAndKeepAliveStream(InputStream in) {
			super(in);
		}
		
		public int read() throws IOException {
			do {
				try {
					return super.read();//如果读到则返回 退出while循环  如果读不到直到超时那么就发起一次心跳包直到读完退出循环  socket.setSoTimeout(HEARTBEAT_PERIOD);
				} catch (SocketTimeoutException e) {
					heartBeat();
				}
			} while (true);
		}

		public int read(byte b[]) throws IOException {
			do {
				try {
					return super.read(b);
				} catch (SocketTimeoutException e) {
					heartBeat();
				}
			} while (true);
		}

		public int read(byte b[], int offset, int length) throws IOException {
			do {
				try {
					System.out.println("a");
					return super.read(b, offset, length);
				} catch (SocketTimeoutException e) {
					heartBeat();
				}
			} while (true);
		}
		private void heartBeat() {
			System.out.println("b");
			new Thread() {
				@Override
				public void run() {
					MoppPackage heartbeatPackage = new MoppPackage();
					try {
						//Application.getInstance().writeBlackBox(TAG, "自动发起心跳");
						System.out.println("自动发起心跳");
						MoppPackage responsePackage = sendToServer(heartbeatPackage);
						System.out.println("服务端收到心跳包并返回。");
					} catch (Exception e) {
						e.printStackTrace();
					}							
				}						
			}.start();
		}
	}
}
