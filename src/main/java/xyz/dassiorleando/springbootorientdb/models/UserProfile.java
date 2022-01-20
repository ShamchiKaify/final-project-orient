package xyz.dassiorleando.springbootorientdb.models;

import com.orientechnologies.orient.core.record.impl.ODocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private String userName;
    private String password;
    private String fullName;
    private String dateOfBirth;
    private String nid;
    private ROLE role;

    public static UserProfile fromODocument(ODocument oDocument) {
        UserProfile userProfile = new UserProfile();
        userProfile.userName = oDocument.field("userName");
        userProfile.password = oDocument.field("password");
        userProfile.fullName = oDocument.field("fullName");
        userProfile.dateOfBirth = oDocument.field("dateOfBirth");
        userProfile.nid = oDocument.field("nid");
        userProfile.role = ROLE.valueOf(oDocument.field("role") == null ? "PATIENT" : "DOCTOR");

        return userProfile;
    }
}

