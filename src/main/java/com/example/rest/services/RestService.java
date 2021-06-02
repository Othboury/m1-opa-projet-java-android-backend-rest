package com.example.rest.services;

import java.net.URI;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.example.rest.models.*;

import com.example.rest.security.BasicAuth;
import com.example.rest.security.JWTAuth;
import com.sun.istack.NotNull;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import static org.hibernate.sql.InFragment.NULL;

@Path("/android")
public class RestService {

	/**  date : 04/2021
	 * A java  class for the localization resources.
	 * and users administration  ( JAX RS -  Jersey - JPA )
	 */
	public static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private UserRepository userRepository;
	private WapRepository wapRepository ;
	public RestService(){
		userRepository = UserRepository.getInstance();
		wapRepository  = WapRepository.getInstance() ;
	}

	/**
	 * Method handling HTTP GET requests.
	 * Initialize the Database
	 */
	@Path("/init")
	@GET
	@Produces({MediaType.APPLICATION_JSON ,MediaType.APPLICATION_XML})
	public String init() throws InvalidKeySpecException, NoSuchAlgorithmException {
		userRepository.init();
		return  " database initialized" ;
	}
	/**
	 * A GET method to obtain a JWT token with basic authentication for users.
	 * @param securityContext the security context
	 * @return the base64 encoded JWT Token.
	 * @see  com.example.rest.security.AuthenticationFilter
	 */
	@Path("/auth")
	@POST
	@BasicAuth
	@Produces({MediaType.APPLICATION_JSON ,MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	@Consumes({MediaType.APPLICATION_JSON ,MediaType.APPLICATION_XML})
	public String authenticate(@Context SecurityContext securityContext){

		return "{ Token : "+Jwts.builder()
				.setIssuer("sample-jaxrs")
				.setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
				.setSubject(securityContext.getUserPrincipal().getName())
				.claim("login","pcisse200")
				.setExpiration(Date.from(LocalDateTime.now().plus(15, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(KEY).compact()+'}';
	}
	/**
	 * Method handling HTTP GET requests. The returned users will be sent
	 * to the client as JSON or XML  media type.
	 * @return Array of Utilisateurs  that will be returned as a JSON or XML response.
	 *  No need authentication
	 */
	@Path("/utilisateurs")
	@GET
	@Produces({MediaType.APPLICATION_JSON ,MediaType.APPLICATION_XML})
	public List<Utilisateur> getUtilisateurs(){
		return userRepository.findAll();
	}
	/**
	 * Method handling HTTP POST method to add a new users with token. Secured with JWTAuth
	 * @return the base64 encoded JWT Token.
	 */
	@Path("/utilisateurs")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public String create(Utilisateur utilisateur){
		if(utilisateur.getLogin()!=null && utilisateur.getPassword()!=null){
			userRepository.save(utilisateur);
			return "l'utilisateur a été ajouté dans la base de données";
		}
		return  "login or password incorrect";
	}
	/**
	 * Method handling HTTP GET requests. The returned user will be sent
	 * to the client as JSON or XML media type.
	 * @return User will be returned as a JSON or XML response.
	 *
	 */
	@Path("/utilisateurs/{id}")
	@GET
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
	public Utilisateur getUtilisateur(@PathParam(value  = "id")int idP){
		return userRepository.findById(idP);
	}
	/**
	 * Method handling HTTP DELETE requests.
	 * @return String that will be returned as a text/plain response.
	 * Secured by JWTAuth
	 */
	@Path("/utilisateurs/{id}")
	@JWTAuth
	@RolesAllowed("ADMIN")
	@DELETE
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
	public String delete(@PathParam(value  = "id")int id ){
		userRepository.delete(id);
		return "user"+id+"is deleted";
	}
	/**
	 *Method handling HTTP UPDATE requests.
	 * update  users infos.
	 * Require token.
	 * @param id
	 * @param fname
	 * @param lname
	 * @param login
	 */
	@Path("/utilisateurs/{id}/{fname}/{lname}/{login}")
	@PUT
	@JWTAuth
	@RolesAllowed("ADMIN")
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
	/**
	 * Method handling HTTP POST requests.
	 * Add new scan data in the database
	 * @param wifiPoint
	 */
	@Path("/wap")
	@POST
	//@RolesAllowed("ADMIM")
	//@JWTAuth
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML , MediaType.TEXT_PLAIN , MediaType.APPLICATION_FORM_URLENCODED})
	public String find(WifiPoint wifiPoint){
		wapRepository.save(wifiPoint);
		return "wifi data added  successfully";
	}
	/**
	 * Method handling HTTP GET requests.
	 * fetch all wifi accees points datas.
	 */
	@Path("/wap")
	@GET
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML , MediaType.TEXT_PLAIN , MediaType.APPLICATION_FORM_URLENCODED})
	public List<WifiPoint> find(){
		return wapRepository.findAll();
	}

	/**
	 * Method handling HTTP POST requests.
	 * Entrypoint for the flask server
	 * Redirecting to the Flask server to predict thge room.
	 */
	@POST
	@Path("/predict")
	@Produces(MediaType.APPLICATION_JSON)
	public Response redirecting() throws Exception
	{
		System.out.println("IP" + HashService.getIpAdress());

		String str = "http://"+HashService.getIpAdress()+":6000/predict" ;
		System.out.println(str);
		URI targetURIForRedirection = new URI(str);
		return Response.temporaryRedirect(targetURIForRedirection).build();
	}
	/**
	 * Method handling HTTP GET requests.
	 * Get the current user
	 */
	@GET
	@Path("/current")
	@Produces(MediaType.APPLICATION_JSON)
	public Utilisateur getCurrent(){
		return  userRepository.getCurrentUser();
	}
}

