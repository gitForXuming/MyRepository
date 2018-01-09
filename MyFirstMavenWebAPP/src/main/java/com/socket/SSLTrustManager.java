package com.socket;

import java.math.BigInteger;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

import com.util.StringUtil;

public class SSLTrustManager {
	public SSLTrustManager(RSAPrivateKey clientPrivateKey, X509Certificate clientCert,  X509Certificate[] caChains) {
		this.clientPrivateKey = clientPrivateKey;
		this.clientCert = clientCert;
		this.caChainList = caChains.clone();
	}
	
	private class CertTrustManager implements X509TrustManager {

		private CertTrustManager() {
		}
		
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
			//System.out.println(String.format("SSLTrustManager.CertTrustManager.checkClientTrusted:arg0[%s];arg1[%s]" , 
			//		StringUtil.arrayToString(arg0), arg1));
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
			//System.out.println(String.format("SSLTrustManager.CertTrustManager.checkServerTrusted:arg0[%s];arg1[%s]" , 
			//		StringUtil.arrayToString(arg0), arg1));

			for(X509Certificate cert : arg0) {	
				if(cert == null) {
					throw new CertificateException("服务端证书为空");
				}

				boolean verified = false;
				
				for(int i = 0; i < caChainList.length; i++) {
					X509Certificate caCert = caChainList[i];
					
					if(!caCert.getSubjectDN().equals(cert.getIssuerDN())) {
						continue;
					}
					
					try {
						cert.verify(caCert.getPublicKey());	
						if(i == 0) {
							if(!BigInteger.valueOf(0x1003).equals(cert.getSerialNumber())) {
								System.out.println(String.format("服务端证书[%s]无效", cert.getSerialNumber()));
								break;
							}
						}
						verified = true;
						break;
					} catch (Exception e) {
						System.err.println("服务端证书校验失败");
						throw new CertificateException(e);
					}			
				}
				if(!verified) {
					throw new CertificateException(String.format("证书[%s]的CA[%s]无效", cert.getSubjectDN(), cert.getIssuerDN()));
				}
			}
			return;
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return new X509Certificate[] { clientCert };
		}

	}

	private class KeyManager implements X509KeyManager {

		private KeyManager()
		{
		}
		
		@Override
		public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
			// TODO Auto-generated method stub
			System.out.println(String.format("SSLTrustManager.KeyManager.chooseClientAlias:arg0[%s];arg1[%s]" , 
					StringUtil.arrayToString(arg0), StringUtil.arrayToString(arg1)));
			return "SoftbankTerminal";
		}

		@Override
		public String chooseServerAlias(String keyType, Principal[] issuers,
				Socket socket) {
			// TODO Auto-generated method stub
			System.out.println(String.format("SSLTrustManager.KeyManager.chooseServerAlias:keyType[%s];Principal[%s]" , 
					keyType, StringUtil.arrayToString(issuers)));
			return null;
		}

		@Override
		public X509Certificate[] getCertificateChain(String alias) {
			// TODO Auto-generated method stub
			System.out.println(String.format("SSLTrustManager.KeyManager.getCertificateChain:alias[%s]" , alias));
			return new X509Certificate[]{ clientCert };
		}

		@Override
		public String[] getClientAliases(String keyType, Principal[] issuers) {
			// TODO Auto-generated method stub
			System.out.println(String.format("SSLTrustManager.KeyManager.getClientAliases:keyType[%s];Principal[%s]" , 
					keyType, StringUtil.arrayToString(issuers)));
			return null;
		}

		@Override
		public PrivateKey getPrivateKey(String alias) {
			// TODO Auto-generated method stub
			System.out.println(String.format("SSLTrustManager.KeyManager.getPrivateKey:alias[%s]" , alias));
			return clientPrivateKey;
		}

		@Override
		public String[] getServerAliases(String keyType, Principal[] issuers) {
			// TODO Auto-generated method stub
			System.out.println(String.format("SSLTrustManager.KeyManager.getServerAliases:keyType[%s];Principal[%s]" , 
					keyType, StringUtil.arrayToString(issuers)));
			return null;
		}

	}
	
	public X509TrustManager x509TrustManager()
	{
		return this.certTrustManager;
	}

	public X509KeyManager x509KeyManager()
	{
		return this.keyManager;
	}
	
	private CertTrustManager certTrustManager = new CertTrustManager();
	private KeyManager keyManager = new KeyManager();

	private RSAPrivateKey clientPrivateKey = null;
	private X509Certificate clientCert = null;
	private X509Certificate caChainList[] = null;
}
