package xyz.dassiorleando.springbootorientdb.models;

import com.orientechnologies.orient.core.record.impl.ODocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dassiorleando.springbootorientdb.domain.Article;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Generic {
    private Long genericId;
    private String genericName;
    private String precaution;
    private String indication;
    private String contraIndication;
    private String dose;
    private String sideEffect;
    private String pregnancyCategoryId;
    private String modeOfAction;
    private String interaction;

    public static Generic fromODocument(ODocument oDocument) {
        Generic generic = new Generic();
        generic.genericId = oDocument.field("genericId");
        generic.genericName = oDocument.field("genericName");
        generic.precaution = oDocument.field("precaution");
        generic.indication = oDocument.field("indication");
        generic.contraIndication = oDocument.field("contraIndication");
        generic.dose = oDocument.field("dose");
        generic.sideEffect = oDocument.field("sideEffect");
        generic.pregnancyCategoryId = oDocument.field("pregnancyCategoryId");
        generic.modeOfAction = oDocument.field("modeOfAction");
        generic.interaction = oDocument.field("interaction");

        return generic;
    }
}
