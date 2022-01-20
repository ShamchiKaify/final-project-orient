package xyz.dassiorleando.springbootorientdb.service;

import com.opencsv.exceptions.CsvValidationException;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import xyz.dassiorleando.springbootorientdb.models.ROLE;
import xyz.dassiorleando.springbootorientdb.models.UserProfile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserProfileServiceTest {
    // The orientdb installation folder
    String orientDBFolder = "/opt/orientdb";
    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:" // or plocal
            + orientDBFolder + "/databases/userProfile")
            .open("admin", "admin");

    UserProfileService userProfileService = new UserProfileService(db);

    @Test
    public void testSave() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        UserProfile userProfile = new UserProfile();
        userProfile.setUserName("kaify");
        userProfile.setFullName("Shamchi Kaify");
        userProfile.setNid("125474");
        userProfile.setRole(ROLE.DOCTOR);
        userProfile.setPassword("123");

        UserProfile save = userProfileService.save(userProfile);

        assertEquals(userProfile.toString(), save.toString());
    }

    @Test
    public void testInsertAll() throws CsvValidationException, IOException {
        long startTime = System.currentTimeMillis();

        List<UserProfile> all = userProfileService.insertAllFromCSV();
        all.forEach(item -> {
            userProfileService.save(item);
//            System.out.println(item);
        });
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed Time: " + estimatedTime + " ms");
    }

    @Test
    public void testGetAll() {
        long startTime = System.currentTimeMillis();

        List<UserProfile> all = userProfileService.findAll();
//        all.forEach(System.out::println);
        System.out.println(all.size());
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed Time: " + estimatedTime + " ms");
    }

    @Test
    public void testFindConditionally() {
        long startTime = System.currentTimeMillis();

        List<UserProfile> list = userProfileService.findConditionallyNamePrefix("M");
        System.out.println(list.size());
        list.forEach(System.out::println);

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed Time: " + estimatedTime + " ms");
    }

    @Test
    public void testDeleteConditionally() {
        long startTime = System.currentTimeMillis();

        userProfileService.deleteConditionallyNamePrefix("M");

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed Time: " + estimatedTime + " ms");
    }

    @Test
    public void testDeleteAll() {
        userProfileService.deleteAll();
    }

    @Test
    public void testImportAllFromOtherDB() {
        String uri = "http://localhost:8081/user_profile/get_all_by_role/DOCTOR";

        RestTemplate restTemplate = new RestTemplate();
        UserProfile[] foundArrayDoctor = restTemplate.getForObject(uri, UserProfile[].class);
        List<UserProfile> doctorList = Arrays.asList(foundArrayDoctor);
        System.out.println("Total Doctors: " + doctorList.size());

        doctorList.forEach(doctor -> {
            userProfileService.save(doctor);
        });

        uri = "http://localhost:8081/user_profile/get_all_by_role/PATIENT";

        restTemplate = new RestTemplate();
        UserProfile[] foundArrayPatient = restTemplate.getForObject(uri, UserProfile[].class);
        List<UserProfile> patientList = Arrays.asList(foundArrayPatient);
        System.out.println("Total Patients: " + patientList.size());

        patientList.forEach(patient -> {
            userProfileService.save(patient);
        });
    }
}