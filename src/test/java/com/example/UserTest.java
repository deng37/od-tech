package com.example;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.example.User;

public class UserTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(User.class);
    }

    /**
     * Test User
     */
    @Test
    public void testGet() {
        Response output = target("/api/user").queryParam("idType","PASSPORT").queryParam("idNumber","A24098888").request().get();
        assertEquals("should return status 200", 200, output.getStatus());
    }
}
