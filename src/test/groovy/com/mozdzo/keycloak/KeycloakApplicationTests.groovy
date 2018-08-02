package com.mozdzo.keycloak

import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.keycloak.authorization.client.AuthzClient
import org.keycloak.authorization.client.Configuration
import org.keycloak.representations.idm.authorization.AuthorizationRequest
import org.keycloak.test.TestsHelper
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@RunWith(SpringRunner)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class KeycloakApplicationTests {

    @Test
    void testAccessToPathsMappedWithAdminResource() {
        HttpResponse response = makeRequest("http://localhost:8080/admin/hello", "modzo_admin", "modzo_admin_password", false, "Admin Resource");
        assertAccessGranted(response)
    }

    @Test
    void testForbidAccessForUserToAdminResource() {
        HttpResponse response = makeRequest("http://localhost:8080/admin/hello", "modzo_user", "modzo_user_password", false, "Admin Resource");
        assertAccessNotGranted(response)
    }

    @Test
    void testForbidAccessWithNoBearerToAdminResource() {
        HttpResponse response = makeRequestWithoutBearer("http://localhost:8080/admin/hello");
        assertEquals(401, response.getStatusLine().getStatusCode());
    }

    private HttpResponse makeRequest(String uri, String userName, String password, boolean sendRpt, String resourceId) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        String accessToken = TestsHelper.getToken(userName, password, TestsHelper.testRealm);
        String rpt;

        if (sendRpt) {
            rpt = obtainRequestingPartyToken(resourceId, accessToken);
        } else {
            rpt = accessToken;
        }

        request.addHeader("Authorization", "Bearer " + rpt);

        return client.execute(request);
    }

    private String obtainRequestingPartyToken(String resourceId, String accessToken) {
        Configuration configuration = new Configuration();

        configuration.setResource("app-authz-rest-springboot");
        configuration.setAuthServerUrl(TestsHelper.keycloakBaseUrl);
        configuration.setRealm(TestsHelper.testRealm);

        AuthzClient authzClient = AuthzClient.create(configuration);

        AuthorizationRequest request = new AuthorizationRequest();

        request.addPermission(resourceId);

        return authzClient.authorization(accessToken).authorize(request).getToken();
    }

    private HttpResponse makeRequestWithoutBearer(String uri) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);

        return client.execute(request);
    }

    private void assertAccessGranted(HttpResponse response) throws IOException {
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Hello Admin", EntityUtils.toString(response.getEntity()));
    }

    private void assertAccessNotGranted(HttpResponse response) throws IOException {
        assertEquals(403, response.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(response.getEntity()).contains('Forbidden'))
    }
}
