package com.style.registry;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RegistryApplication.class)
@RunWith(SpringRunner.class)
class RegistryApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("111");
	}

}
