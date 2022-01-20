package xyz.dassiorleando.springbootorientdb.models;

import com.orientechnologies.orient.core.exception.OSerializationException;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.serialization.ODocumentSerializable;
import com.orientechnologies.orient.core.serialization.OSerializableStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Arrays;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrescribedMedicine implements OSerializableStream {
    private Long id;
    private String medicineId;
    private String medicineDosage;

    public static PrescribedMedicine fromODocument(ODocument oDocument) {
        PrescribedMedicine prescribedMedicine = new PrescribedMedicine();
        prescribedMedicine.id = oDocument.field("id");
        prescribedMedicine.medicineId = oDocument.field("medicineId");
        prescribedMedicine.medicineDosage = oDocument.field("medicineDosage");

        return prescribedMedicine;
    }

    @Override
    public byte[] toStream() throws OSerializationException {
        PrescribedMedicine prescribedMedicine = new PrescribedMedicine();
        prescribedMedicine.setId(getId());
        prescribedMedicine.setMedicineId(getMedicineId());
        prescribedMedicine.setMedicineDosage(getMedicineDosage());

        return SerializationUtils.serialize(prescribedMedicine);
    }

    @Override
    public OSerializableStream fromStream(byte[] bytes) throws OSerializationException {
        Object deserialize = SerializationUtils.deserialize(new ByteArrayInputStream(bytes));
        System.out.println("Hi: " + deserialize);
        return SerializationUtils.deserialize(new ByteArrayInputStream(bytes));
    }
}
