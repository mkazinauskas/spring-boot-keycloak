package com.mozdzo.keycloak

class TestUsers {

    static TestUser ADMIN = new TestUser('modzo_admin', 'modzo_admin_password')

    static TestUser USER = new TestUser('modzo_user', 'modzo_user_password')

    static class TestUser {
        final String username
        final String password

        TestUser(String username, String password) {
            this.username = username
            this.password = password
        }
    }
}
