package photoentities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PhotosLight {
    String name,description,url,date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PhotosLight(String name, String description, String url, String date) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.date = date;
    }
    
    public PhotosLight(){   
    }
    
    //A method to tranform Photos objects to PhotosLight objects
    public List<PhotosLight> makeLight(List<Photos> Photos) {
        List<PhotosLight> PhotosLight = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Photos.stream().forEach((Photo) -> { 
            PhotosLight.add(new PhotosLight(Photo.getName(), Photo.getDescription(), Photo.getUrl(), df.format(Photo.getDate())));
        });
        return PhotosLight;
    }
}
