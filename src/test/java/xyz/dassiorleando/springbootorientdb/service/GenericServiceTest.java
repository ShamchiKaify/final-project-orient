package xyz.dassiorleando.springbootorientdb.service;

import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.dassiorleando.springbootorientdb.SpringOrientDBApplication;
import xyz.dassiorleando.springbootorientdb.models.Generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GenericServiceTest {
    // The orientdb installation folder
    String orientDBFolder = "/opt/orientdb";
    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:" // or plocal
            + orientDBFolder + "/databases/generic")
            .open("admin", "admin");

    GenericService genericService = new GenericService(db);

    @Test
    public void testSave() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        Generic generic = new Generic();
        generic.setGenericId(1L);
        generic.setGenericName("Isomeprazole");
        generic.setIndication("Gastric");
        generic.setModeOfAction("Powder");

        Generic save = genericService.save(generic);
//        System.out.println(generic);
//        System.out.println(save);
        assertEquals(generic.toString(), save.toString());
    }

    @Test
    public void testDeleteOne() {
        Long genericId = 2L;
        boolean delete = genericService.delete(genericId);
        assertTrue(delete);
    }

    @Test
    public void testGetAll() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        List<Generic> all = genericService.findAll();
        all.forEach(System.out::println);
    }

    @Test
    public void testFineOne() {
        Long genericId = 2L;

        Generic one = genericService.findOne(genericId);
        System.out.println(one);
    }

    @Test
    public void deleteAll() {
        boolean generic = genericService.deleteAll();
        assertTrue(generic);
    }

    @Test
    @DisplayName("INSERT ALL")
    public void insertAll() throws IOException {
        long startTime = System.currentTimeMillis();

        List<Generic> staticList = genericService.readAll();

        System.out.println(staticList.size());

        staticList.forEach(item -> {
            genericService.save(item);
        });

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed Time: " + estimatedTime + " ms");
    }
}























