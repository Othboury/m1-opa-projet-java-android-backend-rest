package com.example.rest.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.java.Log;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

import static com.example.rest.services.RestService.KEY;

/**
 * This class if a filter for JAX-RS to perform authentication via JWT.
 */
@JWTAuth
@Provider
@Priority(Priorities.AUTHENTICATION)
@Log
public class JsonWebTokenFilter implements ContainerRequestFilter {
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    //We inject the data from the acceded resource.
    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        //We get the authorization header from the request
        final String authorization = requestContext.getHeaderString(AUTHORIZATION_PROPERTY);

        //We check the credentials presence
        if (authorization == null || authorization.isEmpty()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Please provide your credentials").build());
            return;
        }

        //We get the token
        final String compactJwt = authorization.substring(AUTHENTICATION_SCHEME.length()).trim();
        if (!authorization.contains(AUTHENTICATION_SCHEME) || compactJwt.isEmpty()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Please provide correct credentials").build());
            return;
        }

        String username = null;

        //We check the validity of the token
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer("sample-jaxrs")
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(compactJwt);
            username = jws.getBody().getSubject();

            //We build a new securitycontext to transmit the security data to JAX-RS
            String finalUsername = username;
            requestContext.setSecurityContext(new SecurityContext() {
              @Override
              public Principal getUserPrincipal() {
                  return new Principal() {
                      @Override
                      public String getName() {
                          return finalUsername;
                      }
                  };
              }

              @Override
              public boolean isUserInRole(String role) {
                  return false;
              }

              @Override
              public boolean isSecure() {
                  return false;
              }

              @Override
              public String getAuthenticationScheme() {
                  return null;
              }
          });



        } catch (JwtException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Wrong JWT token. " + e.getLocalizedMessage()).build());
        }



    }
}