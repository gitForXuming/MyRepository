package com.socket;

import it.sauronsoftware.base64.Base64;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.servlet.ServletContext;


public class SSLTrustContext {
    public static SSLTrustManager load(ServletContext context, String clientKeyFile,
                                       String clientCertFile, String[] caChainFiles) {
        RSAPrivateKey clientPrivateKey = (clientKeyFile == null) ? null : loadPrivateKey(context, clientKeyFile);
        X509Certificate clientCert = (clientCertFile == null) ? null : loadCert(context, clientCertFile);
        X509Certificate caChains[] = new X509Certificate[caChainFiles.length];
        for (int i = 0; i < caChainFiles.length; i++) {
            caChains[i] = loadCert(context, caChainFiles[i]);
        }
        return new SSLTrustManager(clientPrivateKey, clientCert, caChains);
    }

    private static X509Certificate loadCert(ServletContext context, String path) {
        final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
        final String END_CERTIFICATE = "-----END CERTIFICATE-----";

        try {
            InputStream inputStream = new FileInputStream(context.getRealPath("/") + path);
            byte[] buff = new byte[inputStream.available()];
            inputStream.read(buff);
            inputStream.close();
            String strContent = new String(buff);
            int posBegin = strContent.indexOf(BEGIN_CERTIFICATE);
            if (posBegin >= 0) {
                int posEnd = strContent.indexOf(END_CERTIFICATE) + END_CERTIFICATE.length();
                strContent = strContent.substring(posBegin, posEnd);
            } else {
                strContent = BEGIN_CERTIFICATE + "\n" + strContent + "\n" + END_CERTIFICATE;
            }
            inputStream = new ByteArrayInputStream(strContent.getBytes());
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                return (X509Certificate) certificateFactory.generateCertificate(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } catch (CertificateException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    private static RSAPrivateKey loadPrivateKey(ServletContext context, String path) {
        final String BEGIN_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----";
        final String END_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----";

        try {
            InputStream inputStream = new FileInputStream(context.getRealPath("/") + path);
            byte[] buff = new byte[inputStream.available()];
            inputStream.read(buff);
            inputStream.close();
            String strContent = new String(buff);
            int posBegin = strContent.indexOf(BEGIN_PRIVATE_KEY);
            if (posBegin >= 0) {
                int posEnd = strContent.indexOf(END_PRIVATE_KEY);
                strContent = strContent.substring(posBegin + BEGIN_PRIVATE_KEY.length(), posEnd);
            }

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec ks = new PKCS8EncodedKeySpec(Base64.decode((strContent.getBytes())));
            return (RSAPrivateKey) keyFactory.generatePrivate(ks);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace(System.err);
        }
        return null;

    }

}
