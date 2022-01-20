package xyz.dassiorleando.springbootorientdb.config;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Basic OrientDB configuration class
 * To configure and provide the bean to inject later for database interactions
 * @author dassiorleando
 */
@Configuration
public class OrientDBConfiguration {

    /**
     * Connect and build the OrientDB Bean for Document API
     * @return
     */
    @Bean
    public ODatabaseDocumentTx orientDBfactory() {
        // The orientdb installation folder
        String orientDBFolder = "/opt/orientdb";
        return new ODatabaseDocumentTx("plocal:" // or plocal
                + orientDBFolder + "/databases/medicine")
                .open("admin", "admin");

        // or

//        return new ODatabaseDocumentTx("remote:localhost/alibabacloudblog")
//                .open("admin", "admin"); // To avoid the concurrent process access with on the same local server as the administrator
    }

}

