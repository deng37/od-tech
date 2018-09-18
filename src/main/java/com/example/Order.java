package com.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;

/**
 * Root resource (exposed at "api/order" path)
 */
@Path("api/orders")
public class Order {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(String input) {
        JSONObject inputJson = new JSONObject(input);
        String customerId = inputJson.get("customerId").toString();
        String startDate = inputJson.get("startDate").toString();
        String endDate = inputJson.get("endDate").toString();
        String statusCode = inputJson.get("statusCode").toString();
        String startRecord = inputJson.get("startRecord").toString();
        String recordLimit = inputJson.get("recordLimit").toString();
        
        // parameter not given
        if (customerId == null || customerId.trim().length() == 0) {
            return Response.serverError().entity("customerId parameter must be given").build();
        }
        try {
            // getCustomer Request
            Client client = Client.create();
            WebResource webResource = client.resource("https://avocado.od-tech.my/api/orders");
            JSONObject bodyJson = new JSONObject().put("customerId", customerId);
            ClientResponse response = webResource.header("Content-Type","application/json;charset=UTF-8").post(ClientResponse.class, bodyJson.toString());
            JSONObject output = new JSONObject(response.getEntity(String.class));

            // return response
            return Response.ok(output.toString(), MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
