package xyz.dassiorleando.springbootorientdb.service;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.dassiorleando.springbootorientdb.models.PrescribedMedicine;
import xyz.dassiorleando.springbootorientdb.models.Prescription;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class PrescriptionServiceTest {
    // The orientdb installation folder
    String orientDBFolder = "/opt/orientdb";
    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:" // or plocal
            + orientDBFolder + "/databases/prescription")
            .open("admin", "admin");


    PrescriptionService prescriptionService = new PrescriptionService(db);

    @Test
    public void testSavePrescription() {
        Prescription prescription = new Prescription();
        prescription.setDoctorUserName("shamin.asfaq");
        prescription.setPatientNid("2015010000443");

        List<PrescribedMedicine> list = new ArrayList<>();

        PrescribedMedicine medicine = new PrescribedMedicine();
        medicine.setMedicineId("Ziskavit");
        medicine.setMedicineDosage("1+0+1");

        list.add(medicine);

        medicine = new PrescribedMedicine();
        medicine.setMedicineId("Ace");
        medicine.setMedicineDosage("0+0+1");
        list.add(medicine);

        prescription.setListOfMedicine(list);

        Prescription save = prescriptionService.save(prescription);
        System.out.println(save);

    }

    @Test
    public void testFindAll() {
        List<Prescription> all = prescriptionService.findAll();
//        System.out.println(all.size());
        all.forEach(System.out::println);
    }

    @Test
    public void testDeleteAll() {
        boolean deleteAll = prescriptionService.deleteAll();
        System.out.println(deleteAll);
    }
}