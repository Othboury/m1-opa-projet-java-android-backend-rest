package com.example.rest.services;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.example.rest.models.UserRepository;
import com.example.rest.models.Utilisateur;
import com.example.rest.security.BasicAuth;
import com.example.rest.security.JWTAuth;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Path("/android")
public class RestService {

	public static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private UserRepository userRepository;

    public RestService(){
		userRepository = UserRepository.getInstance();
	}

	@GET
	@Path("/secure")
	@JWTAuth
	@Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	public String securedByJWTAdminOnly(@Context SecurityContext securityContext) {

		return "Access with JWT ok for " + securityContext.getUserPrincipal().getName();
	}
	@Path("/auth")
	@GET
	@BasicAuth
	@Produces({MediaType.APPLICATION_JSON ,MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON ,MediaType.APPLICATION_XML})
	public String authenticate(@Context SecurityContext securityContext){

		System.out.println(securityContext);
		System.out.println(securityContext.getUserPrincipal().getName());

		return Jwts.builder()
				.setIssuer("sample-jaxrs")
				.setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
				.setSubject(securityContext.getUserPrincipal().getName())
				.claim("login","pcisse200")
				.setExpiration(Date.from(LocalDateTime.now().plus(15, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(KEY).compact() ;
	}


	/**
     * Method handling HTTP GET requests. The returned livres will be sent
     * to the client as JSON or XML  media type.
     * @return Array of Livres  that will be returned as a JSON or XML response.
     */
	@Path("/utilisateurs")
    @GET
    @Produces({MediaType.APPLICATION_JSON ,MediaType.APPLICATION_XML})
    public List<Utilisateur> getUtilisateurs(){
        System.out.println(userRepository.findAll());
        return userRepository.findAll();
    }

	/**
	 * Method handling HTTP GET requests. The returned users will be sent
	 * to the client as JSON or XML media type.
	 *
	 * @return User that will be returned as a JSON or XML response.
	 */
	@Path("/utilisateurs/{idP}")
	@GET
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
    public Utilisateur getUtilisateur(@PathParam(value  = "idP")int idP){
		System.out.println("search by identity");
		System.out.println(userRepository.findById(idP)+ "par id ");
    	return userRepository.findById(idP);
	}

	/**
	 * Method handling HTTP PUT requests.
	 * to the client as JSON or XML media type.
	 *
	 */
	@Path("/utilisateurs")
    @POST
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
	public String create(Utilisateur utilisateur ){
    	userRepository.save(utilisateur);
    	return "POST done";
	}

	/**
	 * Method handling HTTP DELETE requests.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@Path("/utilisateurs/{id}")
	@DELETE
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
	public String delete(@PathParam(value  = "id")int id ){
		userRepository.delete(id);
		return id+"is deleted";
	}

	/**
	 * Method handling HTTP PUY requests.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@Path("/admin/{id}")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
	public String setAdmin(@PathParam(value  = "id")int id ){
		Utilisateur user = userRepository.findById(id);
		userRepository.setAdmin(user);
		return user.getLogin()+" is currently an admin.";
	}

	@Path("/noadmin/{id}")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
	public String removeAdmin(@PathParam(value  = "id")int id ){
		Utilisateur user = userRepository.findById(id);
		userRepository.RemoveAdmin(user);
		return user.getLogin()+" is no longer an admin.";
	}

	@Path("/utilisateurs/{id}/{fname}/{lname}/{login}")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
	public String updateUser(@PathParam(value  = "id")int id, @PathParam(value = "fname") String fname,
							 @PathParam(value ="lname") String lname, @PathParam(value = "login") String login){
		Utilisateur user = userRepository.findById(id);
		user.setFirstname(fname);
		user.setLastname(lname);
		user.setLogin(login);
		userRepository.update(user);
		return user.getLogin()+" is no longer an admin.";
	}



}

