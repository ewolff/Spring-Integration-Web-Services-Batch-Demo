package com.ewolff.orderhandling.test;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/application-config.xml" })
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public abstract class AbstractSpringBasedTest {

}
