package c.n.t.a.s.j.a.client;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;

public class SafeTrustManager implements X509TrustManager {

    private final X509TrustManager defaultTm;
    private String caPath=null;
    /**
     * @param caPath 指定本地 CA 链文件 (PEM 或 CRT)，例如 "chain.crt"
     */
    public SafeTrustManager(String caPath, String url) throws Exception {
        this.caPath=caPath;
        InputStream inputStream = getClass().getResourceAsStream(caPath);
        //创建空的keystore用来存储证书
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, "123456".toCharArray());

        //加载证书
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(inputStream);
        if(certificates == null || certificates.isEmpty()){
            System.out.println("certificates == null || certificates.isEmpty()");
        }

        //将证书加载到keystore中
        int index = 0;
        for(Certificate certificate: certificates){
            String certificateAlias = Integer.toString(index++);
            System.out.println("certificateAlias = "+certificateAlias);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        //用keyStore实例初始化TrustManagerFactory，这样trustManagerFactory就会信任keyStore中的证书
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if(trustManagers == null || trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)){
            System.out.println("trustManagers == null || trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)");
        }
        defaultTm= (X509TrustManager)trustManagers[0];
    }

    private X509TrustManager getX509TrustManager(TrustManagerFactory tmf) {
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                return (X509TrustManager) tm;
            }
        }
        throw new IllegalStateException("No X509TrustManager found");
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // 一般客户端不需要此逻辑
        //localTm.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        System.out.println("checkServerTrusted:");
        if(chain == null ){
            throw new CertificateException("Invalid certificate chain or authType!");
        }
        System.out.println("chain size:"+chain.length);
        if(defaultTm != null){
            defaultTm.checkServerTrusted(chain, authType);
        }

        X509Certificate x509Certificate = chain[0];
        Date notAfter = x509Certificate.getNotAfter();
        System.out.println("notAfter:"+notAfter.toGMTString());
        Date notBefore = x509Certificate.getNotBefore();
        System.out.println("notBefore:"+notBefore.toGMTString());
        Date current = new Date();
        if(current.after(notAfter)){
            throw new CertificateException("Server certificate has expired!");
        }

        //获取本地证书中的信息
        String clientEncoded = "";
        String clientSubject = "";
        String clientIssUser = "";
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            InputStream inputStream = getClass().getResourceAsStream(caPath);
            X509Certificate clientCertificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
            clientEncoded = new BigInteger(1, clientCertificate.getPublicKey().getEncoded()).toString(16);
            clientSubject = clientCertificate.getSubjectDN().getName();
            clientIssUser = clientCertificate.getIssuerDN().getName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取网络中的证书信息
        X509Certificate certificate = chain[0];
        PublicKey publicKey = certificate.getPublicKey();
        String serverEncoded = new BigInteger(1, publicKey.getEncoded()).toString(16);

//        CoreLog.d("clientEncoded:"+clientEncoded);
//        CoreLog.d("serverEncoded:"+serverEncoded);
//        if (!clientEncoded.equals(serverEncoded)) {
//            throw new CertificateException("Server certificate exception!");
//        }


        // 证书拥有者
        String subject = certificate.getSubjectDN().getName();
        System.out.println("clientSubject:"+clientSubject);
        System.out.println("subject:"+subject);
        //if (!clientSubject.equals(subject)) {
        //    throw new CertificateException("Server certificate exception!");
        //}
//        try {
//            String host = new URL(Constants.REMOTE_URL).getHost();
////            String host = new URL("https://idcenter.newposstore.com:8099/").getHost();
//            System.out.println("certificate host:"+host);
////            if(!subject.contains(host)){
////                throw new CertificateException("Server certificate exception!");
////            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        // 证书颁发者
        String issuser = certificate.getIssuerDN().getName();
        System.out.println("clientIssUser:"+clientIssUser);
        System.out.println("issuser:"+issuser);
        //if (!clientIssUser.equals(issuser)) {
        //    throw new CertificateException("Server certificate exception!");
        //}
    }


    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}

