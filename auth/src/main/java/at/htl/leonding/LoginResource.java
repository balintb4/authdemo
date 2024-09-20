package at.htl.leonding;

import at.htl.leonding.auth.Credentials;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.Base64;

@Path("/login")
@PermitAll
public class LoginResource {
    public static record LoginResponse(String token) {}
    @POST
    public Response login(Credentials credentials) {

        var cred = String.format("%s%s",credentials.username(),credentials.password());
        var encoded = Base64.getEncoder().encodeToString(cred.getBytes());
        var response = new LoginResponse(encoded);

        return Response.ok(response).build();

    }
}
