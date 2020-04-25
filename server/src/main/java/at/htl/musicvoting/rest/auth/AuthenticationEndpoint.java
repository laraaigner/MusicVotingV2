package at.htl.musicvoting.rest.auth;

import org.apache.commons.codec.digest.DigestUtils;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

@Stateless
@Path("auth")
public class AuthenticationEndpoint {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response login(String token){
        String password = ResourceBundle.getBundle("config").getString("password");
        String sha256hex = DigestUtils.sha256Hex(password);
        if(token.equals(sha256hex)){
            NewCookie cookie = new NewCookie("token", sha256hex, "/", null, null, 36000, false, true);
            return Response.ok().cookie(cookie).build();
        }else{
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }

    @Path("/validate")
    @GET
    public Response validate(@CookieParam("token") String value) {
        String password = ResourceBundle.getBundle("config").getString("password");
        String sha256hex = DigestUtils.sha256Hex(password);
        if (value != null && value.equals(sha256hex)) {
            return Response.ok(password).build();
        } else {
            return Response.status(403).build();
        }
    }


}
