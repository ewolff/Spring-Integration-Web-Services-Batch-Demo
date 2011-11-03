package com.ewolff.orderhandling.wsclient;

import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

import junit.framework.Assert;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringResult;

public class WSClient {

	public static void main(String[] args) throws Exception {
		sendFile("test-request.xml");
		sendFile("test-request-1.xml");
	}

	private static void sendFile(String fileName) throws IOException {
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		Resource resource = new ClassPathResource(fileName);
		StringResult stringResult = new StringResult();
		webServiceTemplate.sendSourceAndReceiveToResult(
				"http://localhost:8080/orderhandling/services",
				new StreamSource(resource.getInputStream()), stringResult);
		Assert.assertTrue(stringResult.toString().contains("success"));
		System.out.println("success "+fileName);
	}

}
