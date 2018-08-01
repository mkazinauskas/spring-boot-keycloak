package com.mozdzo.keycloak

import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.keycloak.test.TestsHelper
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class KeycloakApplicationTests {

	@BeforeClass
	public static void setup() throws IOException {
		TestsHelper.baseUrl = "http://localhost:8080";
		TestsHelper.testRealm="spring-boot-quickstart";
		TestsHelper.importTestRealm("admin","admin","/quickstart-realm.json");
		TestsHelper.createDirectGrantClient();
	}

	@Test
	void contextLoads() {
	}

}
