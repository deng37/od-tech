package com.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;
import javax.ws.rs.HeaderParam;

/**
 * Root resource (exposed at "api/user" path)
 */
@Path("api/user")
public class User {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("idType") String idType, @QueryParam("idNumber") String idNumber, @HeaderParam("authorization") String authString) {
        // not authenticate
        if (authString == null || authString.compareTo("peekaboo") == 0) {
            return Response.serverError().entity("authentication failed").build();
        }

        // parameter not given
        if ((idType == null || idType.trim().length() == 0) || (idNumber == null || idNumber.trim().length() == 0)) {
            return Response.serverError().entity("idType and idNumber parameter must be given").build();
        }

        try {
            // getCustomer Request
            Client client = Client.create();
            WebResource webResource = client.resource("https://avocado.od-tech.my/api/customer?idType="+ idType +"&idNumber="+ idNumber);
            ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
            JSONObject output = new JSONObject(response.getEntity(String.class));
            JSONObject customerMeta = output;
            String customerId = output.getJSONObject("customer").get("id").toString();

            // getCustomerProfile Request
            webResource = client.resource("https://avocado.od-tech.my/api/customerRole?customerId="+customerId);
            response = webResource.accept("application/json").get(ClientResponse.class);
            output = new JSONObject(response.getEntity(String.class));
            String roles = output.get("roles").toString();
            JSONArray rolesArray = new JSONArray(roles);

            // custom key and return response
            JSONObject result = new JSONObject();
            for(int i = 0; i<customerMeta.names().length(); i++){
                String key = customerMeta.names().getString(i);
                result.put(key, customerMeta.getJSONObject(key));
                if (key.compareTo("customer") == 0) {
                    result.getJSONObject(key).put("roles", rolesArray);
                    result.getJSONObject(key).getJSONObject("details").put("displayName", result.getJSONObject(key).getJSONObject("details").get("name"));
                    result.getJSONObject(key).getJSONObject("details").remove("name");
                    result.getJSONObject(key).getJSONObject("details").remove("salutation");
                }
            }
            String resultString = result.toString();
            return Response.ok(resultString, MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
