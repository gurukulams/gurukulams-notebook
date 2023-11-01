module gurukulams.notebook {
    requires java.base;
    requires java.sql;
    requires java.naming;
    requires org.json;
    requires org.postgresql.jdbc;
    requires jakarta.validation;
    requires org.hibernate.validator;
    requires com.h2database;

    opens com.gurukulams.notebook.service;
    opens db.migration;

    exports com.gurukulams.notebook.service;
}