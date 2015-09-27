package userentities.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import userentities.Users;

@Stateless
@Path("userentities.users")
public class UsersFacadeREST extends AbstractFacade<Users> {
    @PersistenceContext(unitName = "PhotoApp3PU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
     
    //Method for getting the Id of the user by the email. If user doesn't exist a new entry in database is being created.
    @GET
    @Path("getId/{email}")
    @Produces({"text/plain"})
    public int getId(@PathParam("email") String email) {
        int id;
        //Setting up the query
        Query res = em.createNativeQuery("SELECT id,revoked FROM users WHERE email= '" + email + "' ;",Users.class);
        //Setting the results in the users list
        List<Users> users=(List<Users>)res.getResultList();
        //Checking if list is empty because use does not exist in database
        if(users.isEmpty()){
            //Inserting the user to the database
            res = em.createNativeQuery("INSERT INTO users(email) VALUES('" + email + "') ;");
            res.executeUpdate();
            //Getting the new user's id
            res = em.createNativeQuery("SELECT id FROM users WHERE email= '" + email + "' ;");
            //Setting the results in the ids list
            List<Integer> ids=(List<Integer>)res.getResultList();
            //Getting the first (and only) result
            id=ids.get(0);
        }else{
            //If user exists but has previously revoked access, it's being granted again
            if(users.get(0).getRevoked()){
                res = em.createNativeQuery("UPDATE users SET revoked = 0 WHERE email = '" + email + "' ;");
                res.executeUpdate();
            }
            //Getting the result from the first request which returned the user with the requested email
            id=users.get(0).getId();
        }
        //Returning the value to client
        return id;
    }
    
    //Method for updating the database when user revokes access from their profile
    @PUT
    @Path("setRevoked/{email}")
    public void seRevoked(@PathParam("email") String email) {
        //Setting up the query for updating the database
        Query res = em.createNativeQuery("UPDATE users SET revoked = 1 WHERE email = '" + email + "' ;");
        //Executing the update
        res.executeUpdate();
    }
}
