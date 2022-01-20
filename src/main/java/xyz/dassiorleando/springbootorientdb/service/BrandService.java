package xyz.dassiorleando.springbootorientdb.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.springframework.stereotype.Service;
import xyz.dassiorleando.springbootorientdb.models.Brand;
import xyz.dassiorleando.springbootorientdb.models.Generic;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BrandService {
    private final ODatabaseDocumentTx db; // db instance

    public BrandService(ODatabaseDocumentTx db) {
        this.db = db;
    }


    /**
     * To create an Generic
     * @param brand
     * @return brand
     */
    public Brand save(Brand brand) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        // The Class will be automatically created into Orient Studio
        ODocument doc = new ODocument(Brand.class.getSimpleName()); // The entity name is provided as parameter
        doc.field("brandId", brand.getBrandId());
        doc.field("genericId", brand.getGenericId());
        doc.field("companyId", brand.getCompanyId());
        doc.field("brandName", brand.getBrandName());
        doc.field("form", brand.getForm());
        doc.field("strength", brand.getStrength());
        doc.field("price", brand.getPrice());
        doc.field("packSize", brand.getPackSize());

        Brand one = findOne(brand.getBrandId());
        if (one == null) {
            doc.save();
            return brand;
        }

        return new Brand();
    }

    /**
     * To update an brand
     * @param brand
     * @return boolean true if it was successfully updated
     */
    public boolean update(Brand brand) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        // Data
        Long brandId = brand.getBrandId();
        Long genericId = brand.getGenericId();
        Long companyId = brand.getCompanyId();
        String brandName = brand.getBrandName();
        String form = brand.getForm();
        String strength = brand.getStrength();
        String price = brand.getPrice();
        String packSize = brand.getPackSize();


        // The sql query
        String query = "update Brand set genericId = '" + genericId +
                "', companyId = '" + companyId +
                "', brandName = '" + brandName +
                "', form = '" + form +
                "', strength = '" + strength +
                "', price = '" + price +
                "', packSize = '" + packSize +
                "' where brandId = '" + brandId + "'";

        int resultInt = db.command(
                new OCommandSQL(query)).execute();

        if(resultInt != -1) return true;
        return false;
    }

    /**
     * Find a single GENERIC by brandId
     * @param brandId
     * @return GENERIC if found null else
     */
    public Brand findOne(Long brandId) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        // SQL query to have the ones that match
        List<ODocument> results = db.query(
                new OSQLSynchQuery<ODocument>("select * from Brand where brandId = '" + brandId + "'"));

        // For the sake of the test, pick the first found result
        if(!results.isEmpty()) {
            ODocument oDocument = results.get(0);
            return Brand.fromODocument(oDocument);
        }

        return null;
    }

    /**
     * Find all saved GENERICs so far
     * @return
     */
    public List<Brand> findAll() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);
        // List of resulting article
        List<Brand> brandList = new ArrayList<>();

        // Load all the brandList
        for (ODocument genericDocument : db.browseClass("Brand")) {
            Brand brand = Brand.fromODocument(genericDocument);

            brandList.add(brand);
        }

        return brandList;
    }

    /**
     * Delete a single generic by its brandId
     * @param brandId
     * @return boolean true if it was deleted successfully
     */
    public boolean delete(Long brandId) {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

//        brandId = brandId.trim(); // The brandId of the article to delete
        int resultInt =  db.command(
                new OCommandSQL("delete from Brand where brandId = '" + brandId + "'")).execute();

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
                new OCommandSQL("delete from Brand where brandId > '" + 0 + "'")).execute();

        if(resultInt != -1) return true;
        return false;
    }

    /**
     * Read All From CSV
     */
    public List<Brand> readAll() throws IOException {
        List<Brand> staticList = new ArrayList<>();

        String fileName = "/home/ums/IdeaProjects/springboot-orientdb/src/main/resources/brands.csv";

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            String[] line;
            while((line = reader.readNext())!=null) {
                String[] split = Arrays.toString(line).replaceAll("\\[", "").replaceAll("]", "").split(",");

                if (!split[0].contains("brand_id")) {
                    Brand brand = new Brand();
                    brand.setBrandId(Long.valueOf(split[0].trim()));
                    brand.setGenericId(Long.valueOf(split[1].trim()));
                    brand.setCompanyId(Long.valueOf(split[2].trim()));
                    brand.setBrandName(split[3]);
                    brand.setForm(split[4]);
                    brand.setStrength(split[5]);
                    String foundPrice = split[6];

                    foundPrice = foundPrice.replace("\\t", "");
                    brand.setPrice(foundPrice);

                    brand.setPackSize(split[7]);

                    staticList.add(brand);
                }
            }
        } catch (CsvException e) {
            e.printStackTrace();
        }

        return staticList;
    }
}
