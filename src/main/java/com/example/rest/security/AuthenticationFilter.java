package com.example.rest.security;

import com.example.rest.models.UserRepository;
import lombok.SneakyThrows;

import javax.annotation.Priority;
import javax.annotation.security.RolesAllowed;
import javax.jws.soap.SOAPBinding;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Base64;
import java.util.List;

import java.util.*;
@BasicAuth
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    UriInfo uriInfo;

    private static final String AUTHORIZATION_HEADED_KEY = "Authorization";
    private static final String AUTHENTICATION_HEADER_PREFIX = "Basic";
    private UserRepository userRepository = UserRepository.getInstance() ;

    //We inject the data from the acceded resource.
    @Context
    private ResourceInfo resourceInfo;


    @SneakyThrows
    @Override
    public void filter(ContainerRequestContext requestContext){
        //We use reflection on the acceded method to look for security annotations.
        Method method = resourceInfo.getResourceMethod();

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

            //We check to login/password
            if(!userRepository.exist(username, password)){
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Wrong username or password").build());
            }


            if (userRepository.exist(username, password)){
                //We check to the role
               /* if(!userRepository.getCurrentUser().isAdmin()){
                    System.out.println("current"+ userRepository.getCurrentUser().toString());
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                            .entity("you have not right access").build());
                }*/
                //checkons le role
                if (method.isAnnotationPresent(RolesAllowed.class)) {
                    RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                    String[] rolesSet = rolesAnnotation.value() ;
                    System.out.println("les roles" + rolesSet[0]);

                    if(!userRepository.getCurrentUser().isAdmin()){
                        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                                .entity("you have not right access").build());

                    }




                }

                System.out.println(userRepository.exist(username, password));
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
        /*Response unauhorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access the ressource").build();
        requestContext.abortWith((unauhorizedStatus));*/
            }

        }

