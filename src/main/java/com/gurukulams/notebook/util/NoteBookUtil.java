package com.gurukulams.notebook.util;

import com.gurukulams.notebook.NoteBookManager;
import com.gurukulams.notebook.service.AnnotationService;
import com.gurukulams.notebook.store.AnnotationStore;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;

public class NoteBookUtil {

    /**
     * Notebooks Data Storage Directory.
     */
    private static final String DATA_NOTEBOOK = "./data/notebooks/";
    /**
     * File Extension of H2.
     */
    private static final String H2_DB_EXT = ".mv.db";

    /**
     * NoteBook Util.
     */
    private static NoteBookUtil noteBookUtil;

    /**
     * gets NoteBookUtil.
     * @return noteBookUtil
     */
    public static NoteBookUtil getNoteBookUtil() {
        if (noteBookUtil == null) {
            noteBookUtil = new NoteBookUtil();
        }
        return noteBookUtil;
    }

    /**
     * Get Annotation Store for User.
     * @param userName
     * @return getAnnotationService(userName)
     */
    public AnnotationStore getAnnotationStore(final String userName)
            throws SQLException, IOException {
        return NoteBookManager.getManager(getDataSource(userName))
                .getAnnotationStore();
    }

    /**
     * Get DataSource Store for User.
     * @param userName
     * @return ds
     * @throws IOException
     * @throws SQLException
     */
    private static DataSource getDataSource(final String userName)
            throws IOException, SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        String dbFile = DATA_NOTEBOOK + userName;
        ds.setURL("jdbc:h2:" + dbFile);
        ds.setUser("sa");
        ds.setPassword("sa");
        if (!new File(dbFile + H2_DB_EXT).exists()) {
            Reader reader = new InputStreamReader(AnnotationService.class
                    .getModule()
                    .getResourceAsStream("db/migration/V1__notes.sql"));
            RunScript.execute(ds.getConnection(), reader);
        }
        return ds;
    }

}
