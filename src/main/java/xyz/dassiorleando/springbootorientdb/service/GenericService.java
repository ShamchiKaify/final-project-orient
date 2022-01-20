package xyz.dassiorleando.springbootorientdb.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import xyz.dassiorleando.springbootorientdb.SpringOrientDBApplication;
import xyz.dassiorleando.springbootorientdb.domain.Article;
import xyz.dassiorleando.springbootorientdb.models.Generic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GenericService {
    private final ODatabaseDocumentTx db; // db instance

    public GenericService(ODatabaseDocumentTx db) {
        this.db = db;
    }

    /**
     * To create an Generic
     * @param generic
     * @return generic
     */
    public Generic save(Generic generic) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        // The Class will be automatically created into Orient Studio
        ODocument doc = new ODocument(Generic.class.getSimpleName()); // The entity name is provided as parameter
        doc.field("genericId", generic.getGenericId());
        doc.field("genericName", generic.getGenericName());
        doc.field("precaution", generic.getPrecaution());
        doc.field("indication", generic.getIndication());
        doc.field("contraIndication", generic.getContraIndication());
        doc.field("dose", generic.getDose());
        doc.field("sideEffect", generic.getSideEffect());
        doc.field("pregnancyCategoryId", generic.getPregnancyCategoryId());
        doc.field("modeOfAction", generic.getModeOfAction());
        doc.field("interaction", generic.getInteraction());

        Generic one = findOne(generic.getGenericId());
        if (one == null) {
            doc.save();
            return generic;
        }

        return new Generic();
    }

    /**
     * To update an generic
     * @param generic
     * @return boolean true if it was successfully updated
     */
    public boolean update(Generic generic) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        // Data
        Long genericId = generic.getGenericId();
        String genericName = generic.getGenericName();
        String precaution = generic.getPrecaution();
        String indication = generic.getIndication();
        String contraIndication = generic.getContraIndication();
        String dose = generic.getDose();
        String sideEffect = generic.getSideEffect();
        String pregnancyCategoryId = generic.getPregnancyCategoryId();
        String modeOfAction = generic.getModeOfAction();
        String interaction = generic.getInteraction();

        // The sql query
        String query = "update Generic set genericName = '" + genericName +
                "', precaution = '" + precaution +
                "', indication = '" + indication +
                "', contraIndication = '" + contraIndication +
                "', dose = '" + dose +
                "', sideEffect = '" + sideEffect +
                "', pregnancyCategoryId = '" + pregnancyCategoryId +
                "', modeOfAction = '" + modeOfAction +
                "', interaction = '" + interaction +
                "' where genericId = '" + genericId + "'";

        int resultInt = db.command(
                new OCommandSQL(query)).execute();

        if(resultInt != -1) return true;
        return false;
    }

    /**
     * Find a single GENERIC by genericId
     * @param genericId
     * @return GENERIC if found null else
     */
    public Generic findOne(Long genericId) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        // SQL query to have the ones that match
        List<ODocument> results = db.query(
                new OSQLSynchQuery<ODocument>("select * from Generic where genericId = '" + genericId + "'"));

        // For the sake of the test, pick the first found result
        if(!results.isEmpty()) {
            ODocument oDocument = results.get(0);
            return Generic.fromODocument(oDocument);
        }

        return null;
    }

    /**
     * Find all saved GENERICs so far
     * @return
     */
    public List<Generic> findAll() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);
        // List of resulting article
        List<Generic> genericList = new ArrayList<>();

        // Load all the genericList
        for (ODocument genericDocument : db.browseClass("Generic")) {
            Generic generic = Generic.fromODocument(genericDocument);

            genericList.add(generic);
        }

        return genericList;
    }

    /**
     * Delete a single generic by its genericId
     * @param genericId
     * @return boolean true if it was deleted successfully
     */
    public boolean delete(Long genericId) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

//        genericId = genericId.trim(); // The genericId of the article to delete
        int resultInt =  db.command(
                new OCommandSQL("delete from Generic where genericId = '" + genericId + "'")).execute();

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
                new OCommandSQL("delete from Generic where genericId > '" + 0 + "'")).execute();

        if(resultInt != -1) return true;
        return false;
    }

    /**
     * Read All From CSV
     */
    public List<Generic> readAll() throws IOException {
        List<Generic> staticList = new ArrayList<>();

        String fileName = "/home/ums/IdeaProjects/springboot-orientdb/src/main/resources/generics.csv";

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            String[] line;
            while((line = reader.readNext())!=null) {
                String[] split = Arrays.toString(line).replaceAll("\\[", "").replaceAll("]", "").split(",");

                if (!split[0].contains("generic_id")) {
                    int length = split.length;

                    Generic generic = new Generic();
                    generic.setGenericId(Long.valueOf(split[0]));
                    generic.setGenericName(split[1]);
                    generic.setPrecaution(split[2]);
                    generic.setIndication(split[3]);
                    generic.setContraIndication(split[4]);
                    generic.setDose(split[5]);
                    generic.setSideEffect(split[6]);
                    generic.setPregnancyCategoryId(split[7]);
                    generic.setModeOfAction(split[8]);
                    generic.setInteraction(split[9]);

                    staticList.add(generic);
                }
            }


        } catch (CsvException e) {
            e.printStackTrace();
        }

        return staticList;
    }
}
