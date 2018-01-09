package com.socket;

import java.io.*;

import com.google.gson.JsonObject;



public class MoppPackage {

	/**
	 * ���ƣ�PACKAGE_HEAD_SIZE <br/>
	 * ����������ͷ��������ռ�ݵĴ�С��Э��ͷ+��Ϣͷ�� <br/>
	 * ���ͣ�int <br/>
	 */
	public static int PACKAGE_HEAD_SIZE = 9;
	private JsonParser moppParser = new JsonParser();
	private MessageHeader header = new MessageHeader();
	private JsonObject body = null;
	
	/**
	 * �����ƣ�ProtocalHeader<br/>
	 * ��������Э��ͷ <br/>
	 */
	/**
	 * �����ƣ�ProtocalHeader<br/>
	 * ��������TODO <br/>
	 */
	public class MessageHeader {
		/**
		 * ��������getReserve <br/>
		 * ������������ <br/>
		 * 
		 * @return byte <br/>
		 */
		public byte getCheckValue() {
			return checkValue;
		}

		/**
		 * ��������getProtocolType <br/>
		 * ������Э������ <br/>
		 * 
		 * @return byte <br/>
		 */
		public byte getVersion() {
			return version;
		}

		/**
		 * ��������getSequence <br/>
		 * ��������ǰ���ĵ����к� <br/>
		 * 
		 * @return int <br/>
		 */
		public int getSequence() {
			return sequence;
		}

		/**
		 * ��������setReserve <br/>
		 * ���������ñ����� <br/>
		 * 
		 * @param reserve
		 *            ָ����ֵ<br/>
		 */
		public void setCheckValue(byte checkValue) {
			this.checkValue = checkValue;
		}

		/**
		 * ��������setProtocolType <br/>
		 * ����������Э������ <br/>
		 * 
		 * @param protocolType
		 *            ָ����ֵ <br/>
		 */
		public void setVersion(byte version) {
			this.version = version;
		}

		/**
		 * ��������setSequence <br/>
		 * ���������ñ������к� <br/>
		 * 
		 * @param sequence
		 *            ָ�������к� <br/>
		 */
		public void setSequence(int sequence) {
			this.sequence = sequence;
		}

		private byte checkValue = 0; // [1]
		private byte version = 0; // 0TCP 1UDP 2HTTP
		private int sequence = 0; // [4]����ţ��������Ӧ������һ��
	}

	public MessageHeader getHeader() {
		return header;
	}

	public JsonObject getBody() {
		return body;
	}

	public void setBody(JsonObject body) {
		this.body = body;
	}
	
	/**
	 * ��������saveToStream <br/>
	 * �������Ѷ�������������С� <br/>
	 * 
	 * @param output
	 *            ָ���������
	 * @throws IOException
	 * <br/>
	 */
	public int saveToStream(OutputStream output) throws IOException{
		byte[] bodyBuff  =(body == null)?null:body.toString().getBytes(TcpLink.Encoding);
		ByteArrayOutputStream
				baos = new ByteArrayOutputStream();
		try{
			PkgUtil.writeByte(baos, header.getVersion());
			PkgUtil.writeInt(baos, (bodyBuff ==null)? PACKAGE_HEAD_SIZE:
				PACKAGE_HEAD_SIZE + bodyBuff.length);
			PkgUtil.writeInt(baos, header.getSequence());
		}finally{
			baos.close();
		}
		byte[] headerBuffer = baos.toByteArray();
		PkgUtil.writeTotalBytes(output, headerBuffer);
		if(null!=bodyBuff){
			PkgUtil.writeTotalBytes(output, bodyBuff);
		}
		if(bodyBuff ==null){
			return PACKAGE_HEAD_SIZE;
		}else{
			return PACKAGE_HEAD_SIZE + bodyBuff.length;
		}
	}
	

	/**
	 * ��������loadFromStream <br/>
	 * ���������������м��ض������ݡ� <br/>
	 * 
	 * @param input
	 *            ָ����������
	 * @throws IOException
	 * <br/>
	 * @throws IOException
	 */
	public int loadFromStream(InputStream input) throws IOException {
		byte[] headerBuffer = PkgUtil.readTotalBytes(input, PACKAGE_HEAD_SIZE);
		
		int bodyLen =0;
		ByteArrayInputStream bais = new ByteArrayInputStream(headerBuffer);
		try{
			header.setVersion(PkgUtil.readByte(bais));
			bodyLen = PkgUtil.readInt(bais) - PACKAGE_HEAD_SIZE;
			header.setSequence(PkgUtil.readInt(bais));
		}finally{
			bais.close();
		}
		
		// �������С��0����󣬳����˸ð汾�Ĵ��������������쳣
		if (bodyLen < 0 || bodyLen > 0xFFFFF) {
			throw new IOException("���ĳ��ȳ����˷�Χ, �����˸ð汾�Ĵ�������.");
		}
		if(bodyLen > 0){
			try{
				body = moppParser.parse(new String(PkgUtil
								.readTotalBytes(input, bodyLen), TcpLink.Encoding))
						.getAsJsonObject();
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}else {
			body = null;
		}
		return PACKAGE_HEAD_SIZE + bodyLen;
	}
}
