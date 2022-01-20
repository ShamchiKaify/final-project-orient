package xyz.dassiorleando.springbootorientdb.service;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.dassiorleando.springbootorientdb.models.Brand;
import xyz.dassiorleando.springbootorientdb.models.Generic;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BrandServiceTest {
    // The orientdb installation folder
    String orientDBFolder = "/opt/orientdb";
    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:" // or plocal
            + orientDBFolder + "/databases/brand")
            .open("admin", "admin");

    BrandService brandService = new BrandService(db);

    @Test
    public void testSave() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        Brand brand = new Brand();
        brand.setBrandId(1L);
        brand.setBrandName("Square Pharma");
        brand.setForm("Liquid");

        Brand save = brandService.save(brand);
        assertEquals(brand.toString(), save.toString());
    }

    @Test
    public void testFineOne() {
        Long brandId = 28438L;

        Brand one = brandService.findOne(brandId);
        System.out.println(one);
    }

    @Test
    public void testGetAll() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        List<Brand> all = brandService.findAll();
        System.out.println(all.size());
    }

    @Test
    public void testDeleteAll() {
        brandService.deleteAll();
    }

    @Test
    @DisplayName("INSERT ALL")
    public void insertAll() throws IOException {
        long startTime = System.currentTimeMillis();
        List<Brand> staticList = brandService.readAll();

        System.out.println(staticList.size());
        staticList.forEach(item -> {
            brandService.save(item);
        });

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed Time: " + estimatedTime + " ms");
    }
}