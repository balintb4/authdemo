package at.htl.leonding.auth;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Base64;

@Provider
public class AuthenticationRequestFilter implements ContainerRequestFilter {
    @Inject
    Logger logger;
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

       var annot = Arrays.stream(containerRequestContext.getRequest().getClass()
                .getAnnotations()).filter(annotation -> annotation.getClass().equals("asd"))
                .findFirst().orElse(null);
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
        var cred  = auth.replace("Basic ","");
        var userNameAndPwd = Base64.getDecoder().decode(cred.getBytes());

        var newString = new String(userNameAndPwd);

        var splittedString = newString.split(":");

        var user = splittedString[0];
        var pwd = splittedString[1];
        logger.infof("User: %s Password %s ",user,pwd);

       return  user.equals("admin") && pwd.equals("password");

    }
}
