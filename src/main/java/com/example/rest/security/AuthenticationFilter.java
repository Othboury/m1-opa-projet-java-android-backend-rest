package com.example.rest.security;

import lombok.SneakyThrows;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.security.Principal;
import java.util.Base64;
import java.util.List;


@BasicAuth
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Context
    UriInfo uriInfo;

    private static final String AUTHORIZATION_HEADED_KEY = "Authorization";
    private static final String AUTHENTICATION_HEADER_PREFIX = "Basic";


    @SneakyThrows
    @Override
    public void filter(ContainerRequestContext requestContext) {

        List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADED_KEY);
        if(authHeader.size()>0){
            String authToken  = authHeader.get(0);
            final String encodedUserPassword = authToken.substring(AUTHENTICATION_HEADER_PREFIX.length()).trim();

            //We Decode username and password (username:password)
            String[] usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes())).split(":");

            final String username = usernameAndPassword[0];
            final String password = usernameAndPassword[1];
            System.out.println("username" + username);
            System.out.println("password" + password);

            if ("admin".equals(username) && "admin".equals(password)){
                //final SecurityContext securityContext = requestContext.getSecurityContext();
                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return new Principal() {
                            @Override
                            public String getName() {
                                return username;
                            }
                        };
                    }
                    /*
                          public Principal getUserPrincipal() {
                        Utilisateur userPrincipal = new Utilisateur();
                        userPrincipal.setFirstname(username);
                        return (Principal) userPrincipal;
                    }

                     */

                    @Override
                    public boolean isUserInRole(String role) {
                        return true ;
                    }

                    @Override
                    public boolean isSecure() {
                        return uriInfo.getAbsolutePath().toString().startsWith("http");
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return "Token-Based-Auth-Scheme";
                    }
                });
                return;
            }
        }
        Response unauhorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access the ressource").build();
        requestContext.abortWith((unauhorizedStatus));
            }

        }

