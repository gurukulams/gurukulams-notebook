package com.gurukulams.notebook.util;

import com.gurukulams.notebook.GurukulamsManager;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.util.UUID;

public class TestUtil {
    public static GurukulamsManager gurukulamsManager() {
        DataSource dataSource = getH2DataSource();

        ClassLoader classLoader = TestUtil.class.getClassLoader();
        File file = new File(classLoader.getResource("db/migration").getFile());

        Flyway.configure()
                .locations("filesystem:"+file.getAbsolutePath())
                .dataSource(dataSource)
                .load().migrate();

        return GurukulamsManager.getManager(dataSource);
    }

    private static JdbcDataSource getH2DataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:file:./target/"+UUID.randomUUID().getLeastSignificantBits());
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }

}
