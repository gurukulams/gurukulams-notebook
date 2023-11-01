package com.gurukulams.notebook.util;


import com.gurukulams.notebook.NoteBookManager;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.util.UUID;

public class TestUtil {
    public static NoteBookManager NotebookManager() {
        DataSource dataSource = getH2DataSource();

        ClassLoader classLoader = TestUtil.class.getClassLoader();
        File file = new File(classLoader.getResource("db/ddl").getFile());

        return NoteBookManager.getManager(dataSource);
    }

    private static JdbcDataSource getH2DataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:file:./target/"+UUID.randomUUID().getLeastSignificantBits());
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }

}
