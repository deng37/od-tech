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
import java.util.Date;
import java.text.SimpleDateFormat;

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
        // parameter not given
        if (input.isEmpty() || input == null) {
            return Response.serverError().entity("empty parameter, please give minimum customerId parameter").build();
        }

        try {
            JSONObject inputJson = new JSONObject(input);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            long DAY_IN_MS = 1000 * 60 * 60 * 24;

            // parsing input parameter
            String customerId = input.contains("customerId") ? inputJson.get("customerId").toString() : "";
            String tempStartDate = input.contains("startDate") ? inputJson.get("startDate").toString() : "";
            String tempEndDate = input.contains("endDate") ? inputJson.get("endDate").toString() : "";
            Date endDate = new Date();
            Date startDate = new Date(endDate.getTime() - (7 * DAY_IN_MS));
            if (!tempStartDate.isEmpty() && !tempEndDate.isEmpty()) {
                startDate = format.parse(tempStartDate);
                endDate = format.parse(tempEndDate);
            }
            String statusCode = input.contains("statusCode") ? inputJson.get("statusCode").toString() : "";
            String startRecord = input.contains("startRecord") ? inputJson.get("startRecord").toString() : "";
            String recordLimit = input.contains("recordLimit") ? inputJson.get("recordLimit").toString() : "";

            // no customer id
            if (customerId.isEmpty()) {
                return Response.serverError().entity("customerId parameter must be given").build();
            }

            // searchOrder
            Client client = Client.create();
            WebResource webResource = client.resource("https://avocado.od-tech.my/api/orders");
            JSONObject bodyJson = new JSONObject().put("customerId", customerId).put("startDate", startDate).put("endDate", endDate).put("statusCode", statusCode).put("startRecord", startRecord).put("recordLimit", recordLimit);
            System.out.println(bodyJson);
            ClientResponse response = webResource.header("Content-Type","application/json;charset=UTF-8").post(ClientResponse.class, bodyJson.toString());
            JSONObject output = new JSONObject(response.getEntity(String.class));

            // return response
            return Response.ok(output.toString(), MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
