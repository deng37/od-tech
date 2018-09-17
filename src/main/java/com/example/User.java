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
import java.util.Date;

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
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        Date d = new Date();

        try {
            Client client = Client.create();
            WebResource webResource = client.resource("https://avocado.od-tech.my/api/customer?idType=PASSPORT&idNumber=A24098888");
            ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
            JSONObject output = new JSONObject(response.getEntity(String.class)); 
            JSONObject customerId = output.getJSONObject("customer").getJSONObject("id");

            System.out.println("POH!1");
            System.out.println(output);
            System.out.println(output.getJSONObject("meta"));

            webResource = client.resource("https://avocado.od-tech.my/api/customerRole?customerId="+customerId.toString());
            response = webResource.accept("application/json").get(ClientResponse.class);
            output = new JSONObject(response.getEntity(String.class)); 

            System.out.println("POH!2");
            System.out.println(output);
            // System.out.println(output.getJSONObject("roles"));
        } catch (Exception e) {
            System.out.println(e);
        }

        return "popoh!" + d.toString();
    }
}
