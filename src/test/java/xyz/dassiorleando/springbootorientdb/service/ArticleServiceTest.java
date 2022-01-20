package xyz.dassiorleando.springbootorientdb.service;

import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.dassiorleando.springbootorientdb.domain.Article;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class ArticleServiceTest {
    // The orientdb installation folder
    String orientDBFolder = "/opt/orientdb";
    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:" // or plocal
            + orientDBFolder + "/databases/medicine")
            .open("admin", "admin");

    ArticleService articleService = new ArticleService(db);

    @Test
    public void testSave() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        Article article = new Article();
        article.setTitle("The Witcher");
        article.setContent("Fantasy");
        article.setAuthor("Some Polish");

        Article save = articleService.save(article);
//        System.out.println(save);
        assertEquals(article.toString(), save.toString());
    }

    @Test
    public void testGetAll() {
        // Specify to use the same db instance for this thread
        ODatabaseRecordThreadLocal.instance().set(db);

        List<Article> all = articleService.findAll();
        all.forEach(System.out::println);
    }

    @Test
    public void testDeleteOne() {
        String title = "The Witcher";
        boolean delete = articleService.delete(title);
        assertTrue(delete);
    }
}