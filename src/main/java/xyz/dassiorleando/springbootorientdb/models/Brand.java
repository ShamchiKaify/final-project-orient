package xyz.dassiorleando.springbootorientdb.models;

import com.orientechnologies.orient.core.record.impl.ODocument;
import lombok.Data;

import javax.persistence.Id;

@Data
public class Brand {
    private Long brandId;
    private Long genericId;
    private Long companyId;
    private String brandName;
    private String form;
    private String strength;
    private String price;
    private String packSize;

    public static Brand fromODocument(ODocument oDocument) {
        Brand brand = new Brand();
        brand.brandId = oDocument.field("brandId");
        brand.genericId = oDocument.field("genericId");
        brand.companyId = oDocument.field("companyId");
        brand.brandName = oDocument.field("brandName");
        brand.form = oDocument.field("form");
        brand.strength = oDocument.field("strength");
        brand.price = oDocument.field("price");
        brand.packSize = oDocument.field("packSize");

        return brand;
    }
}
