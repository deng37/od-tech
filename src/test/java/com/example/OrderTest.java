package com.example;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import org.json.JSONObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.example.Order;

public class OrderTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(Order.class);
    }

    /**
     * Test Order
     */
    @Test
    public void testPost() {
        JSONObject bodyJson = new JSONObject().put("customerId", "1001");
        Response output = target("/api/orders").request(MediaType.APPLICATION_JSON).post(Entity.json(bodyJson.toString()));
        assertEquals("should return status 200", 200, output.getStatus());
    }
}
