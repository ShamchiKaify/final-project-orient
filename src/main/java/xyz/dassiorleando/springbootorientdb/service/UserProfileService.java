package xyz.dassiorleando.springbootorientdb.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.springframework.stereotype.Service;
import xyz.dassiorleando.springbootorientdb.models.Brand;
import xyz.dassiorleando.springbootorientdb.models.ROLE;
import xyz.dassiorleando.springbootorientdb.models.UserProfile;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserProfileService {
    private final ODatabaseDocumentTx db; // db instance

    public UserProfileService(ODatabaseDocumentTx db) {
        this.db = db;
    }

    /**
     * To create a Generic
     * @param userProfile
     * @return userProfile
     */
    public UserProfile save(UserProfile userProfile) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        // The Class will be automatically created into Orient Studio
        ODocument doc = new ODocument(UserProfile.class.getSimpleName()); // The entity name is provided as parameter
        doc.field("userName", userProfile.getUserName());
        doc.field("password", userProfile.getPassword());
        doc.field("fullName", userProfile.getFullName());
        doc.field("dateOfBirth", userProfile.getDateOfBirth());
        doc.field("nid", userProfile.getNid());
        doc.field("role", userProfile.getRole());

        UserProfile one = findOne(userProfile.getNid());
        if (one == null) {
            doc.save();
            return userProfile;
        }

        return new UserProfile();
    }

    /**
     * To update an userProfile
     * @param userProfile
     * @return boolean true if it was successfully updated
     */
    public boolean update(UserProfile userProfile) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        // Data
        String nid = userProfile.getNid();
        String userName = userProfile.getUserName();
        String password = userProfile.getPassword();
        String fullName = userProfile.getFullName();
        String dateOfBirth = userProfile.getDateOfBirth();
        ROLE role = userProfile.getRole();

        // The sql query
        String query = "update UserProfile set userName = '" + userName +
                "', password = '" + password +
                "', fullName = '" + fullName +
                "', dateOfBirth = '" + dateOfBirth +
                "', role = '" + role +
                "' where nid = '" + nid + "'";

        int resultInt = db.command(
                new OCommandSQL(query)).execute();

        if(resultInt != -1) return true;
        return false;
    }

    /**
     * Find a single GENERIC by nid
     * @param nid
     * @return GENERIC if found null else
     */
    public UserProfile findOne(String nid) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        // SQL query to have the ones that match
        List<ODocument> results = db.query(
                new OSQLSynchQuery<ODocument>("select from UserProfile where nid = '" + nid + "'"));

        // For the sake of the test, pick the first found result
        if(!results.isEmpty()) {
            ODocument oDocument = results.get(0);
            return UserProfile.fromODocument(oDocument);
        }

        return null;
    }

    /**
     * Find a single GENERIC by prefix
     * @param prefix
     * @return GENERIC if found null else
     */
    public List<UserProfile> findConditionallyNamePrefix(String prefix) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        // SQL query to have the ones that match
        List<ODocument> results = db.query(
                new OSQLSynchQuery<ODocument>("select * from UserProfile where fullName LIKE '" + prefix + "%'"));

        // For the sake of the test, pick the first found result
        if(!results.isEmpty()) {
            List<UserProfile> list = new ArrayList<>();
            results.forEach(item -> {
                list.add(UserProfile.fromODocument(item));
            });
            return list;
        }

        return null;
    }

    public void deleteConditionallyNamePrefix(String prefix) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        List<UserProfile> profileList = findConditionallyNamePrefix(prefix);
        profileList.forEach(item -> {
            System.out.println("NID: " + item.getNid());
            // SQL query to have the ones that match
            List<ODocument> results = db.query(
                    new OSQLSynchQuery<ODocument>("delete from UserProfile where nid = '" + item.getNid() + "'"));
        });


        return;
    }

    /**
     * Find all saved GENERICs so far
     * @return
     */
    public List<UserProfile> findAll() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);
        // List of resulting article
        List<UserProfile> userProfileList = new ArrayList<>();

        // Load all the userProfileList
        for (ODocument genericDocument : db.browseClass("UserProfile")) {
//            System.out.println(genericDocument);
            UserProfile userProfile = UserProfile.fromODocument(genericDocument);

            userProfileList.add(userProfile);
        }

        return userProfileList;
    }

    /**
     * Delete a single generic by its nid
     * @param nid
     * @return boolean true if it was deleted successfully
     */
    public boolean delete(String nid) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

//        nid = nid.trim(); // The nid of the article to delete
        int resultInt =  db.command(
                new OCommandSQL("delete from UserProfile where nid = '" + nid + "'")).execute();

        if(resultInt != -1) return true;
        return false;
    }

    /**
     * Trying to TRUNCATE ALL
     * @param
     * @return
     */
    public boolean deleteAll() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

//        genericId = genericId.trim(); // The genericId of the article to delete
        int resultInt =  db.command(
                new OCommandSQL("delete from UserProfile")).execute();

        if(resultInt != -1) return true;
        return false;
    }

    /**
     * Read All From CSV
     */
    public List<UserProfile> insertAllFromCSV() throws IOException, CsvValidationException {
        List<UserProfile> staticList = new ArrayList<>();

        String fileName = "/home/shamin/UserProfile.csv";

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            String[] line;
            while((line = reader.readNext())!=null) {
                String[] split = Arrays.toString(line).replaceAll("\\[", "").replaceAll("]", "").split(",");
//                System.out.println(Arrays.toString(split));
                if (!split[0].contains("Email")) {
                    int length = split.length;

                    UserProfile userProfile = new UserProfile();
                    userProfile.setFullName(split[0]);
                    userProfile.setUserName(split[1]);
                    userProfile.setNid(split[3]);
                    userProfile.setPassword("123");
                    userProfile.setDateOfBirth(split[2]);
                    staticList.add(userProfile);
                }
            }


        } catch (CsvException e) {
            e.printStackTrace();
        }

        return staticList;
    }
}
