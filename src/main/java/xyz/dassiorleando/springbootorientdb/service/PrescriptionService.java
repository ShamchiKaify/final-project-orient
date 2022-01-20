package xyz.dassiorleando.springbootorientdb.service;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.springframework.stereotype.Service;
import xyz.dassiorleando.springbootorientdb.models.Generic;
import xyz.dassiorleando.springbootorientdb.models.Prescription;
import xyz.dassiorleando.springbootorientdb.models.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {
    // The orientdb installation folder
    ODatabaseDocumentTx db;

    public PrescriptionService(ODatabaseDocumentTx db) {
        System.out.println("Found DB: " + db.getName());
        String orientDBFolder = "/opt/orientdb";
        this.db = new ODatabaseDocumentTx("plocal:" // or plocal
                + orientDBFolder + "/databases/prescription")
                .open("admin", "admin");
        System.out.println("New DB: " + this.db.getName());
//        this.db = db;
    }

    public Prescription save(Prescription prescription) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        // The Class will be automatically created into Orient Studio
        ODocument doc = new ODocument(Prescription.class.getSimpleName()); // The entity name is provided as parameter
        doc.field("id", prescription.getId());
        doc.field("patientNid", prescription.getPatientNid());
        doc.field("doctorUserName", prescription.getDoctorUserName());
        doc.field("appointmentDate", prescription.getAppointmentDate());
        doc.field("listOfMedicine", prescription.getListOfMedicine());

        doc.save();
        return prescription;
    }

    public List<Prescription> findAll() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);
        // List of resulting article
        List<Prescription> prescriptionList = new ArrayList<>();

        // Load all the prescriptionList
        for (ODocument genericDocument : db.browseClass("Prescription")) {
//            System.out.println(genericDocument);
            Prescription prescription = Prescription.fromODocument(genericDocument);

            prescriptionList.add(prescription);
        }

        return prescriptionList;
    }

    public List<Prescription> getPrescriptionListByNid(String nid) {
        List<Prescription> found = findAll();
        List<Prescription> collect = found.stream().filter(prescription -> prescription.getPatientNid().equals(nid)).collect(Collectors.toList());
        System.out.println(collect);
        return collect;
    }

    public boolean deleteAll() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

//        genericId = genericId.trim(); // The genericId of the article to delete
        int resultInt =  db.command(
                new OCommandSQL("delete from Prescription")).execute();

        if(resultInt != -1) return true;
        return false;
    }
}
