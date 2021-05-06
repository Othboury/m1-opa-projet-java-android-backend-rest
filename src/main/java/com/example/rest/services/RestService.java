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

import com.example.rest.models.WapRepository;
import com.example.rest.models.WifiPoint;
import com.example.rest.security.BasicAuth;
import com.example.rest.security.JWTAuth;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Path("/android")
public class RestService {
	public static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private UserRepository userRepository;
	private WapRepository wapRepository ;
	public RestService(){
		userRepository = UserRepository.getInstance();
		wapRepository  = WapRepository.getInstance() ;
	}
	/**
	 * a GET method to obtain a JWT token with basic authentication for users.
	 *
	 * @param securityContext the security context
	 * @return the base64 encoded JWT Token.
	 */
	@Path("/auth")
	@GET
	@BasicAuth
	@Produces({MediaType.APPLICATION_JSON ,MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	@Consumes({MediaType.APPLICATION_JSON ,MediaType.APPLICATION_XML})
	public String authenticate(@Context SecurityContext securityContext){
		System.out.println("value of entity manager"+userRepository);
		System.out.println(securityContext);
		System.out.println(securityContext.getUserPrincipal().getName());

		return "{ Token : "+Jwts.builder()
				.setIssuer("sample-jaxrs")
				.setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
				.setSubject(securityContext.getUserPrincipal().getName())
				.claim("login","pcisse200")
				.setExpiration(Date.from(LocalDateTime.now().plus(15, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(KEY).compact()+'}' ;
	}
	/**
	 * Method handling HTTP GET requests. The returned users will be sent
	 * to the client as JSON or XML  media type.
	 * @return Array of Utilisateurs  that will be returned as a JSON or XML response.
	 */
	@Path("/utilisateurs")
	@GET
	@Produces({MediaType.APPLICATION_JSON ,MediaType.APPLICATION_XML})
	public List<Utilisateur> getUtilisateurs(){
		System.out.println(userRepository.findAll());
		return userRepository.findAll();
	}
	/**
	 * Method handling HTTP POST method to obtain add a new users with token. Secured with JWTAuth
	 * @return the base64 encoded JWT Token.
	 */
	@Path("/utilisateurs")
	@POST
	@JWTAuth
	@Consumes({MediaType.APPLICATION_JSON})
	public String create(Utilisateur utilisateur ){
		userRepository.save(utilisateur);
		return "l'utilisateur a été ajouté dans la base de données";
	}
	/**
	 * Method handling HTTP GET requests. The returned users will be sent
	 * to the client as JSON or XML media type.
	 *
	 * @return User that will be returned as a JSON or XML response.
	 */
	@Path("/utilisateurs/{id}")
	@GET
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
	public Utilisateur getUtilisateur(@PathParam(value  = "id")int idP){
		return userRepository.findById(idP);
	}
	/**
	 * Method handling HTTP DELETE requests.
	 *
	 * @return String that will be returned as a text/plain response. Secured by JWTAuth
	 */
	@Path("/utilisateurs/{id}")
	@JWTAuth
	@DELETE
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
	public String delete(@PathParam(value  = "id")int id ){
		userRepository.delete(id);
		return "user"+id+"is deleted";
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

	/**
	 *
	 * mettre à jour les informations d'un utilisateur
	 * @param id
	 * @param fname
	 * @param lname
	 * @param login
	 * @return
	 */
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
	/**
	 *
	 * add new scan data in the database
	 * @param wifiPoint
	 * @return
	 */
	@Path("/wap")
	@POST
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML , MediaType.TEXT_PLAIN , MediaType.APPLICATION_FORM_URLENCODED})
	public String find(WifiPoint wifiPoint ){
		wapRepository.save(wifiPoint);
		return "wifi data added  successfully";
	}

	/**
	 *
	 * get All wifi Accees Point data
	 * @return
	 */
	@Path("/wap")
	@GET
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML , MediaType.TEXT_PLAIN , MediaType.APPLICATION_FORM_URLENCODED})
	public List<WifiPoint> find(){
		return wapRepository.findAll();
	}
}

