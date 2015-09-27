package photoentities.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import photoentities.Photos;
import photoentities.PhotosLight;

@Stateless
@Path("photoentities.photos")
public class PhotosFacadeREST extends AbstractFacade<Photos> {
    @PersistenceContext(unitName = "PhotoApp3PU")
    private EntityManager em;
    
    //The ip of the server that the project runs on
    public final String serverURL="http://83.212.112.140/";
    
    public PhotosFacadeREST() {
        super(Photos.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    //Method for inserting to database
    @POST
    @Path("newPhotoData")
    @Consumes("application/json")
    public JsonObject newPhotoData(Photos entity){
       //Insert new photo in database
       super.create(entity);
       //Get the last id that was created
       Query res = em.createNativeQuery("SELECT id FROM photos WHERE id=LAST_INSERT_ID() ;");
       List <Integer> resIds = (List<Integer>)res.getResultList();
       //If there was any problem with LAST_INSERT_ID() due to any reason (ex new session created) try to find the photo with the exact same attributes
       if(resIds.isEmpty() || resIds.get(0)==0){
           res = em.createNativeQuery("SELECT id FROM photos WHERE latitude="+ entity.getLatitude() +" AND longitude="+ entity.getLongitude() + " AND direction="+ entity.getDirection() + " AND name=" + entity.getName() + " AND description=" + entity.getDescription() + " AND date=" + entity.getDate() + " AND user_id=" + entity.getUserId() +";");
           resIds = (List<Integer>)res.getResultList();
       }
       //Save the id of the photo
       int photoId = resIds.get(0);
       //Storing the extension of the image file
       String extension;
       try{
            extension = entity.getName().substring(entity.getName().lastIndexOf("."));
            //Discard the extension from the name
            res = em.createNativeQuery("UPDATE photos SET name='"+ entity.getName().substring(0,entity.getName().lastIndexOf(".")) +"' WHERE id="+ photoId +";");
            res.executeUpdate();
       }catch(Exception e){
            //If the name of the image has no extension for any reason assume it is jpg
            //The android application only uses jpg for new photos
            extension=".jpg";        
       }
       //Creating the url of the new photo
       String url = serverURL + photoId + extension;
       //Url saved to database
       res = em.createNativeQuery("UPDATE photos SET url='"+ url +"' WHERE id="+ photoId +";");
       res.executeUpdate();
       //Matching the updated entity from the database with the cached one
       em.refresh(entity);
       //Creation of the json object to be returned so the photo on the phone is renamed with the id before uploading it
       JsonObject json = Json.createObjectBuilder().add("photoId",photoId).build();
       return json;
    }
    
    //Method that searches for a photo using the coordinates that were sent
    @GET
    @Path("location/{latitude}/{longitude}/{direction}")
    @Produces("application/json")
    public List<PhotosLight> findbylocation(@PathParam("latitude") Double latitude,@PathParam("longitude") Double longitude,@PathParam("direction") Float direction) {
        //Each query has the id even though it's not needed as it is the primary key and needs to be present
        //Not using SELECT * is to decrease transfered data size
        //First query search photos with the criteria given
        Query res = em.createNativeQuery("SELECT id,name,description,date,url FROM photos WHERE longitude=" + longitude + " AND latitude=" + latitude + " AND direction=" + direction + ";",Photos.class);
        List <Photos> resPhotos = (List<Photos>)res.getResultList();
        //If no photos found search again in a broader direction
        if (resPhotos.isEmpty()) {
            //Setting direction range
            float mindirection=direction-10.0f;
            if (mindirection<0.0f) mindirection=mindirection+359.0f;
            float maxdirection=direction+10.0f;
            if (maxdirection>360.0f) maxdirection=maxdirection-359.0f;
            res = em.createNativeQuery("SELECT id,name,description,date,url FROM photos WHERE longitude=" + longitude + " AND latitude=" + latitude + " AND direction BETWEEN " + mindirection + " AND " + maxdirection + " ORDER BY date ASC " + ";",Photos.class);
            resPhotos = (List<Photos>)res.getResultList();
            //If no photos found again search in broader latitude or longitude
            if(resPhotos.isEmpty()){
                //Setting longitude range
                double minlongitude=longitude-0.001d;
                if (minlongitude<-179d) minlongitude=minlongitude+180d+180d;
                double maxlongitude=longitude+0.001d;
                if(maxlongitude>180d) maxlongitude=maxlongitude-180d+(-180d);
                //Setting latitude range
                double minlatitude=latitude-0.001d;
                if(minlatitude<-90.0d) minlatitude=-90.0d;
                double maxlatitude=latitude+0.001d;
                if(maxlatitude>90.0d) maxlatitude=maxlatitude+90;
                //Check if diretion is primarely Weast or East, if it is then search in broader latitude
                if((direction>=45 && direction<135) || (direction>=225 && direction<315)){
                    res = em.createNativeQuery("SELECT id,name,description,date,url FROM photos WHERE longitude BETWEEN " + minlongitude + " AND " + maxlongitude + " AND latitude=" + latitude + " AND direction =" + direction + " ORDER BY date ASC " + ";",Photos.class);
                    resPhotos = (List<Photos>)res.getResultList();
                    //if empty search again in broader latitude and direction
                    if(resPhotos.isEmpty()){
                        res = em.createNativeQuery("SELECT id,name,description,date,url FROM photos WHERE longitude BETWEEN " + minlongitude + " AND " + maxlongitude + " AND latitude=" + latitude + " AND direction BETWEEN " + mindirection + " AND " + maxdirection + " ORDER BY date ASC " + ";",Photos.class);
                        resPhotos = (List<Photos>)res.getResultList();
                    }
                //else the direction is primarely North or South, so search again in broader longitude   
                }else {
                    //if(((direction>=0 && direction<45) || (direction>=315 && direction<=359)) || (direction>=135 && direction<225))
                    res = em.createNativeQuery("SELECT id,name,description,date,url FROM photos WHERE longitude =" + longitude + " AND latitude BETWEEN " + minlatitude + " AND " + maxlatitude + " AND direction =" + direction + " ORDER BY date ASC " + ";",Photos.class);
                    resPhotos = (List<Photos>)res.getResultList();
                    //if empty search again in broader longitude and direction
                    if(resPhotos.isEmpty()){
                        res = em.createNativeQuery("SELECT id,name,description,date,url FROM photos WHERE longitude =" + longitude + " AND latitude BETWEEN " + minlatitude + " AND " + maxlatitude + " AND direction  BETWEEN " + mindirection + " AND " + maxdirection + " ORDER BY date ASC " + ";",Photos.class);
                        resPhotos = (List<Photos>)res.getResultList();
                    }
                }
                //If again no photos found, search in all posible ranges
                if(resPhotos.isEmpty()){
                    res = em.createNativeQuery("SELECT id,name,description,date,url FROM photos WHERE longitude BETWEEN " + minlongitude + " AND " + maxlongitude + " AND latitude BETWEEN " + minlatitude + " AND " + maxlatitude + " AND direction BETWEEN " + mindirection + " AND " + maxdirection + " ORDER BY date ASC " + ";",Photos.class);
                    resPhotos = (List<Photos>)res.getResultList();
                }
            }
        }
        //Transform the photos to photoslight (less data) and return it empty or not
        PhotosLight Pl = new PhotosLight();
        return Pl.makeLight(resPhotos);
    }  
}
