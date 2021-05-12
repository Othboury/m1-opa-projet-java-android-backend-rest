package com.example.rest.security;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;


@CorsFilter
@Provider
@Priority(Priorities.AUTHENTICATION)
public class MyFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
       // requestContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, X-Request-With, Content-Type, Accept");
                //getHeaders.add("Access-Control-Allow-Headers", "Origin, X-Request-With, Content-Type, Accept")

        MultivaluedMap<String, Object> headers = responseContext.getHeaders();

        headers.add("Access-Control-Allow-Origin", "*");
        //headers.add("Access-Control-Allow-Origin", "https://podcastpedia.org"); //allows CORS requests only coming from podcastpedia.org
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
    }

}
