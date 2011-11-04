package com.ewolff.orderhandling.wsclient;

import static org.springframework.ws.test.server.RequestCreators.withPayload;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.ResponseMatcher;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.TransformerHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-ws-servlet.xml")
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class WSTest {

	@Autowired
	private ApplicationContext applicationContext;

	private MockWebServiceClient mockClient;

	@Before
	public void createClient() {
		mockClient = MockWebServiceClient.createClient(applicationContext);
	}

	@Test
	public void testRequest() throws IOException {
		sendFile("test-request.xml");
	}

	@Test
	public void testRequest1() throws IOException {
		sendFile("test-request-1.xml");
	}

	private void sendFile(String fileName) throws IOException {
		ClassPathResource resource = new ClassPathResource(fileName);

		mockClient.sendRequest(withPayload(resource)).andExpect(
				new ResponseMatcher() {

					private final TransformerHelper transformerHelper = new TransformerHelper();

					@Override
					public void match(WebServiceMessage request,
							WebServiceMessage response) throws IOException,
							AssertionError {
						StringResult result = new StringResult();
						try {
							transformerHelper.transform(
									response.getPayloadSource(), result);
						} catch (TransformerException e) {
							throw new RuntimeException(e);
						}
						Assert.assertTrue(result.toString(), result.toString()
								.contains("success"));
					}
				});
	}
}
