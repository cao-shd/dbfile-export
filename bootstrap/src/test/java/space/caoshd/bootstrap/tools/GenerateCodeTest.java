package space.caoshd.bootstrap.tools;


import org.junit.jupiter.api.Test;
import space.caoshd.common.code_gen.tools.GenerateCodeTools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class GenerateCodeTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void generateCode() {
        List<String> generateTables = Arrays.asList("t_file_export_instance");
        GenerateCodeTools.generate(dataSource, "space.caoshd", "dbexpt", "", generateTables);
    }

}
