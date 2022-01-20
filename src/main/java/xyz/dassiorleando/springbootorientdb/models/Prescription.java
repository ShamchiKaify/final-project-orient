package xyz.dassiorleando.springbootorientdb.models;


import com.orientechnologies.orient.core.exception.OSerializationException;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.serialization.OSerializableStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription implements OSerializableStream {
    private Long id;
    private String patientNid;
    private String doctorNid;
    private String doctorUserName;
    private String appointmentDate;
    private LocalDateTime convertedAppointmentDateTime;
    private List<PrescribedMedicine> listOfMedicine;

    public static Prescription fromODocument(ODocument oDocument) {
        Prescription prescription = new Prescription();
        prescription.id = oDocument.field("id");
        prescription.patientNid = oDocument.field("patientNid");
        prescription.doctorNid = oDocument.field("doctorNid");
        prescription.doctorUserName = oDocument.field("doctorUserName");
        prescription.appointmentDate = oDocument.field("appointmentDate");
        prescription.convertedAppointmentDateTime = oDocument.field("convertedAppointmentDateTime");
        prescription.listOfMedicine = oDocument.field("listOfMedicine");

        return prescription;
    }

    @Override
    public byte[] toStream() throws OSerializationException {
        Prescription prescription = new Prescription();
        prescription.setId(getId());
        prescription.setPatientNid(getPatientNid());
        prescription.setDoctorNid(getDoctorNid());
        prescription.setDoctorUserName(getDoctorUserName());
        prescription.setAppointmentDate(getAppointmentDate());
        prescription.setConvertedAppointmentDateTime(getConvertedAppointmentDateTime());
        prescription.setListOfMedicine(getListOfMedicine());

        return SerializationUtils.serialize(prescription);
    }

    @Override
    public OSerializableStream fromStream(byte[] bytes) throws OSerializationException {
        return SerializationUtils.deserialize(new ByteArrayInputStream(bytes));
    }
}
