package photoentities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-09-12T22:41:53")
@StaticMetamodel(Photos.class)
public class Photos_ { 

    public static volatile SingularAttribute<Photos, Date> date;
    public static volatile SingularAttribute<Photos, Double> latitude;
    public static volatile SingularAttribute<Photos, String> name;
    public static volatile SingularAttribute<Photos, String> description;
    public static volatile SingularAttribute<Photos, Integer> id;
    public static volatile SingularAttribute<Photos, Integer> userId;
    public static volatile SingularAttribute<Photos, String> url;
    public static volatile SingularAttribute<Photos, Double> longitude;
    public static volatile SingularAttribute<Photos, Float> direction;

}