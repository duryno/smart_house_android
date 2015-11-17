package orszag.smarthouse.objectClasses;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;


/**
 * Created by Patrik Glendell on 02/10/15.
 */
@Element(name = "User")
//@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    @Attribute
    private int id;
    @Element
    private String userName;
    @Element
    private String email;
    @Element
    private String profile;

    public User(String userName, String email, String profile) {
        this.userName = userName;
        this.email = email;
        this.profile = profile;
    }
    public User() {
    }


    protected int getId() {
        return id;
    }
    protected void setId(int id) {
        this.id = id;
    }
    protected String getUserName() {
        return userName;
    }
    protected String getEmail() {
        return email;
    }
    protected String getProfile() {
        return profile;
    }
}
