package com.bamzy.insurance.ws.caller;

import com.bamzy.insurance.ws.caller.input.*;
import com.bamzy.insurance.ws.caller.output.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;

/**
 * User: alireza ghassemi
 */
public class WebServiceCaller implements ServiceCaller{
	private static final String namespace = "example";
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
	private final KeyPair keyPair;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final String modulus;
	private final String exponent;
	private final String d;
	private final String brokerId;
	private final String username;
	private final String userServicesUrl;
	private final String userServicesNamespace;
	private final String brokersServiceUrl;
	private final String brokersServiceNamespace;
	public WebServiceCaller(String modulus, String exponent, String d, String brokerId, String username, String userServicesUrl, String userServicesNamespace, String brokersServiceUrl, String brokersServiceNamespace){
		this.modulus = modulus;
		this.exponent = exponent;
		this.d = d;
		this.brokerId = brokerId;
		this.username = username;
		this.userServicesUrl = userServicesUrl;
		this.userServicesNamespace = userServicesNamespace;
		this.brokersServiceUrl = brokersServiceUrl;
		this.brokersServiceNamespace = brokersServiceNamespace;
		KeyPair pair = null;
		try{
			doTrustToCertificates();
			pair = loadKeyPair();
		}catch(Exception e){
			logger.error("exception in WebServiceCaller constructor: ", e);
			System.exit(1);
		}
		keyPair = pair;
	}
	public static SimpleDateFormat getSimpleDateFormat(){
		return simpleDateFormat;
	}
	public static void main(String[] args) throws Exception{
		// key pair testing done
		/*Cipher cipher = Cipher.getInstance("RSA");
		String input = "test";
		KeyPair keyPair = loadKeyPair();
		cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
		byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
		System.out.println("encrypted: " + new String(encrypted));
		cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
		byte[] decrypted = cipher.doFinal(encrypted);
		System.out.println("decrypted: " + new String(decrypted));*/
		/*MessageDigest messageDigest;
		messageDigest = MessageDigest.getInstance("SHA1");
	    messageDigest.update(fanapDepositBalanceRequestString.getBytes());
	    messageDigest.digest();*/
		// signature test done
		/*Signature signature = Signature.getInstance("SHA1withRSA");
		KeyPair keyPair = loadKeyPair();
		signature.initSign(keyPair.getPrivate());
		signature.update("testString".getBytes());
		byte[] signatureBytes = signature.sign();
		System.out.println(new String(signatureBytes));
		signature.initVerify(keyPair.getPublic());
		signature.update("testString".getBytes());
		System.out.println(signature.verify(signatureBytes));*/
		// testing deposit invoice
		/*WebServiceCaller webServiceCaller = new WebServiceCaller();
		Date yesterday = new Date(new Date().getTime() - (1000L * 3600L * 24L));
		Date tenDaysAgo = new Date(yesterday.getTime() - (1000L * 3600L * 24L * 10L));
		GetDepositInvoice getDepositInvoice = new GetDepositInvoice("1700.301.1198864.1", tenDaysAgo, yesterday);
		String getDepositBalanceString = webServiceCaller.toJsonString(getDepositInvoice);
		SOAPMessage soapResponse = webServiceCaller.getSoapResponse(BasicConfig.userServicesUrl, BasicConfig.userServicesNamespace, "/GetDepositInvoice", getDepositBalanceString);
		soapResponse.getSOAPBody().getFirstChild().getFirstChild().getFirstChild().getNodeValue();*/
		// testing deposit block
//		WebServiceCaller webServiceCaller = new WebServiceCaller();
		/*ToIntermediateServiceResponse toIntermediateServiceResponse = new ToIntermediateServiceResponse();
		webServiceCaller.blockDepositAndTransfer(BasicConfig.INTERMEDIATE_SHEBA, 56L, "123456987321654", toIntermediateServiceResponse, "1234569873216541", OperationType.TO_INTERMEDIATE);*/
		/*BlockDepositServiceResponse blockDepositServiceResponse = new BlockDepositServiceResponse();
		webServiceCaller.handleBlockDepositRequest("987654321321654", BasicConfig.TEST_ACCOUNT_SHEBA, 56L, "alaki");
		SingularFromIntermediateServiceResponse singularFromIntermediateServiceResponse = new SingularFromIntermediateServiceResponse();
		webServiceCaller.transferFromBlockedDeposit(BasicConfig.TEST_ACCOUNT_NUMBER, BasicConfig.INTERMEDIATE_ACCOUNT_NUMBER, 60L, "987654321321654", singularFromIntermediateServiceResponse, "9876543213216541");*/
//		webServiceCaller.normalTransfer();
	}
	private KeyPair loadKeyPair() throws GeneralSecurityException{
		byte[] modulusBytes = Base64.decodeBase64(modulus);
		byte[] exponentBytes = Base64.decodeBase64(exponent);
		byte[] dBytes = Base64.decodeBase64(d);
		BigInteger modulus = new BigInteger(1, modulusBytes);
		BigInteger exponent = new BigInteger(1, exponentBytes);
		BigInteger d = new BigInteger(1, dBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
		PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
		RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, d);
		PrivateKey privateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);
		return new KeyPair(publicKey, privateKey);
	}
	private String getSignature(String json) throws GeneralSecurityException{
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(keyPair.getPrivate());
		signature.update(json.getBytes());
		return Base64.encodeBase64String(signature.sign());
	}
	private String removeSlashFromBeginning(String input){
		if(input.startsWith("/"))
			return input.substring(1);
		return input;
	}
	private SOAPMessage createSOAPRequest(String serverURI, String SOAPActionAfterServerURI, String json) throws Exception{
		String signature = getSignature(json);
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(namespace, serverURI);
		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(removeSlashFromBeginning(SOAPActionAfterServerURI), namespace);
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("request", namespace);
		soapBodyElem1.addTextNode(json);
		SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("signature", namespace);
		soapBodyElem2.addTextNode(signature);
		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", serverURI + SOAPActionAfterServerURI);
		soapMessage.saveChanges();
		/* Print the request message */
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		soapMessage.writeTo(outputStream);
		String request = outputStream.toString();
		logger.debug("soap request: " + request);
		return soapMessage;
	}
	private SOAPMessage getSoapResponse(String url, String serverURI, String SOAPActionAfterServerURI, String json) throws Exception{
		// Create SOAP Connection
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();
		// Send SOAP Message to SOAP Server
		SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(serverURI, SOAPActionAfterServerURI, json), url);
		// print SOAP Response
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		soapResponse.writeTo(outputStream);
		String response = outputStream.toString();
		logger.debug("soap response: " + response);
		soapConnection.close();
		return soapResponse;
	}
	private void doTrustToCertificates() throws Exception{
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager(){
					public X509Certificate[] getAcceptedIssuers(){
						return null;
				}
					public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException{
					}
					public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException{
					}
				}
		};
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HostnameVerifier hv = new HostnameVerifier(){
			public boolean verify(String urlHostName, SSLSession session){
				if(!urlHostName.equalsIgnoreCase(session.getPeerHost())){
					System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
				}
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}
	private String toJsonString(Object o) throws IOException{
		StringWriter stringWriter = new StringWriter();
		objectMapper.writeValue(stringWriter, o);
		return stringWriter.toString();
	}
	/*public void blockDepositAndTransfer(String depositNumber, Long amount, String referenceId, AbstractSingleTransferResponse result, String transactionNumber, String operationType) throws Exception{
		final String blockSuccess = "blocking customer deposit successful";
//		dataService.insertLog(operationType, depositNumber, amount, Status.BLOCKED, blockSuccess, transactionNumber, referenceId);
		BlockCustomerDeposit blockCustomerDeposit = new BlockCustomerDeposit();
		blockCustomerDeposit.setAmount(amount.toString());
		blockCustomerDeposit.setBlockNumber(transactionNumber);
		blockCustomerDeposit.setSheba(depositNumber);
		SOAPMessage soapResponse = getSoapResponse(BasicConfig.brokersServiceUrl, BasicConfig.brokersServiceNamespace, "BlockCustomerDeposit", toJsonString(blockCustomerDeposit));
		String jsonResponse = soapResponse.getSOAPBody().getFirstChild().getFirstChild().getFirstChild().getNodeValue();
		BlockCustomerDepositResult blockCustomerDepositResult = objectMapper.readValue(jsonResponse, BlockCustomerDepositResult.class);
	}*/
	public GetDepositBalanceResult handleBalanceRequest(String depositNumber) throws Exception{
		GetDepositBalance getDepositBalance = new GetDepositBalance(username, depositNumber);
		SOAPMessage soapResponse = getSoapResponse(userServicesUrl, userServicesNamespace, "/GetDepositBalance", toJsonString(getDepositBalance));
		String jsonResponse = soapResponse.getSOAPBody().getFirstChild().getFirstChild().getFirstChild().getNodeValue();
		return objectMapper.readValue(jsonResponse, GetDepositBalanceResult.class);
	}
	/**
	 * @param sourceDepositNumber
	 * @param destinationDepositNumber
	 * @param amount
	 * @param sourceComment            optional
	 * @param destComment              optional
	 * @param paymentId                this id must be unique in every day, must only contain english alphabet and numbers
	 * @return
	 * @throws Exception
	 */
	@Override
	public TransferMoneySetOrderResult transferMoneySetOrder(String sourceDepositNumber, String destinationDepositNumber, Long amount, String sourceComment, String destComment, String paymentId) throws Exception{
		TransferMoneySetOrder transferMoneySetOrder = new TransferMoneySetOrder(username, sourceDepositNumber, destinationDepositNumber, amount.toString(), sourceComment, destComment, paymentId);
		SOAPMessage soapResponse = getSoapResponse(userServicesUrl, userServicesNamespace, "/TransferMoneySetOrder", toJsonString(transferMoneySetOrder));
		String jsonResponse = soapResponse.getSOAPBody().getFirstChild().getFirstChild().getFirstChild().getNodeValue();
		return objectMapper.readValue(jsonResponse, TransferMoneySetOrderResult.class);
	}
	public UnblockDepositAndTransferResult transferFromBlockedDeposit(String unblockNumber, String sourceDepositNumber, String destinationDepositNumber, Boolean isUnblockRemainAmount, Long amount) throws Exception{
		UnblockDepositAndTransfer unblockDepositAndTransfer = new UnblockDepositAndTransfer(brokerId, unblockNumber, sourceDepositNumber, destinationDepositNumber, isUnblockRemainAmount.toString(), amount.toString());
		SOAPMessage soapResponse = getSoapResponse(brokersServiceUrl, brokersServiceNamespace, "UnblockDepositAndTransfer", toJsonString(unblockDepositAndTransfer));
		String jsonResponse = soapResponse.getSOAPBody().getFirstChild().getFirstChild().getFirstChild().getNodeValue();
		return objectMapper.readValue(jsonResponse, UnblockDepositAndTransferResult.class);
	}
	public BlockCustomerDepositResult handleBlockDepositRequest(String blockNumber, String sheba, Long amount, String comment) throws Exception{
		BlockCustomerDeposit blockCustomerDeposit = new BlockCustomerDeposit(brokerId, blockNumber, sheba, amount.toString(), comment);
		SOAPMessage soapResponse = getSoapResponse(brokersServiceUrl, brokersServiceNamespace, "BlockCustomerDeposit", toJsonString(blockCustomerDeposit));
		String jsonResponse = soapResponse.getSOAPBody().getFirstChild().getFirstChild().getFirstChild().getNodeValue();
		return objectMapper.readValue(jsonResponse, BlockCustomerDepositResult.class);
	}
	/**
	 * @param date      in yyyy/MM/dd format
	 * @param paymentId the same payment id that was given in transferMoneySetOrder request
	 * @return
	 * @throws Exception
	 */
	@Override
	public GetTransferMoneyStateResult getTransferMoneyState(String date, String paymentId) throws Exception{
		GetTransferMoneyState getTransferMoneyState = new GetTransferMoneyState(username, date, paymentId);
		SOAPMessage soapResponse = getSoapResponse(userServicesUrl, userServicesNamespace, "/GetTransferMoneyState", toJsonString(getTransferMoneyState));
		String jsonResponse = soapResponse.getSOAPBody().getFirstChild().getFirstChild().getFirstChild().getNodeValue();
		return objectMapper.readValue(jsonResponse, GetTransferMoneyStateResult.class);
	}
}
