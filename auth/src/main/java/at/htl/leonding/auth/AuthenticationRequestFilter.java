package at.htl.leonding.auth;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.io.IOException;

@Provider
public class AuthenticationRequestFilter implements ContainerRequestFilter {
    @Inject
    Logger logger;
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        containerRequestContext.getHeaders().forEach((key, value) -> {
            logger.infof("HEADERS sind da: %s=%s", key, value);

        });
        var auth = containerRequestContext.getHeaders().getFirst("Authorization");


        if (auth == null){
            containerRequestContext.abortWith(Response.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED).build());
        }
        else{
            if (!checkBearerToken(auth)){
                containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());

            }
        }
        logger.info("HIER BIN ICH");
    }

    public boolean checkBearerToken(String auth){

        if (auth.equals("Bearer Ich bins") )
        return true;
        else
            return false;

    }
}
