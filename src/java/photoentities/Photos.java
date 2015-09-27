/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoentities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "photos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Photos.findAll", query = "SELECT p FROM Photos p"),
    @NamedQuery(name = "Photos.findById", query = "SELECT p FROM Photos p WHERE p.id = :id"),
    @NamedQuery(name = "Photos.findByName", query = "SELECT p FROM Photos p WHERE p.name = :name"),
    @NamedQuery(name = "Photos.findByDate", query = "SELECT p FROM Photos p WHERE p.date = :date"),
    @NamedQuery(name = "Photos.findByDescription", query = "SELECT p FROM Photos p WHERE p.description = :description"),
    @NamedQuery(name = "Photos.findByUserId", query = "SELECT p FROM Photos p WHERE p.userId = :userId"),
    @NamedQuery(name = "Photos.findByLatitude", query = "SELECT p FROM Photos p WHERE p.latitude = :latitude"),
    @NamedQuery(name = "Photos.findByLongitude", query = "SELECT p FROM Photos p WHERE p.longitude = :longitude"),
    @NamedQuery(name = "Photos.findByDirection", query = "SELECT p FROM Photos p WHERE p.direction = :direction"),
    @NamedQuery(name = "Photos.findByUrl", query = "SELECT p FROM Photos p WHERE p.url = :url")})
public class Photos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    @Size(max = 200)
    @Column(name = "description")
    private String description;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    
    @Basic(optional = false)
    @NotNull
    @Min(-85)
    @Max(85)
    @Column(name = "latitude")
    private double latitude;
    
    @Basic(optional = false)
    @NotNull
    @Min(-179)
    @Max(180)
    @Column(name = "longitude")
    private double longitude;
    
    @Basic(optional = false)
    @NotNull
    @Min(0)
    @Max(360)
    @Column(name = "direction")
    private float direction;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "url")
    private String url;

    public Photos() {
    }

    public Photos(Integer id) {
        this.id = id;
    }

    public Photos(Integer id, String name,String description, Date date, int userId, double latitude, double longitude, float direction, String url) {
        this.id = id;
        this.name = name;
        this.description=description;
        this.date = date;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.url = url;
    }
    
    public Photos(Integer id, String name, Date date, int userId, double latitude, double longitude, float direction, String url) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Photos)) {
            return false;
        }
        Photos other = (Photos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "photoentities.Photos[ id=" + id + " ]";
    }
    
}
