package com.gurukulams.notebook.service;

import com.gurukulams.notebook.model.Annotation;
import com.gurukulams.notebook.store.AnnotationStore;
import com.gurukulams.notebook.util.NoteBookUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * The type User Annotation service.
 */
public class AnnotationService {

    /**
     * Create optional.
     *
     * @param userName   user name
     * @param onInstance
     * @param annotation the  annotation
     * @param locale     tha language
     * @param onType
     * @return the optional
     */
    public final Annotation create(final String userName,
                                   final String onType,
                                   final String onInstance,
                                   final Annotation annotation,
                                   final Locale locale
                                   )
            throws SQLException, IOException {
        Annotation annotationToBeCreated = annotation.withOnType(onType)
                .withOnInstance(onInstance);
        if (locale != null) {
            annotationToBeCreated = annotationToBeCreated
                    .withLocale(locale.getLanguage());
        }
        return NoteBookUtil.getNoteBookUtil()
                .getAnnotationStore(userName)
                .insert()
                .values(annotationToBeCreated).returning();
    }


    /**
     * Read optional.
     * @param userName
     * @param id     the id
     * @param onInstance
     * @param onType
     * @param locale tha language
     * @return the optional
     */
    public final Optional<Annotation> read(final String userName,
                                           final String id,
                                           final String onType,
                                           final String onInstance,
                        final Locale locale)
            throws SQLException, IOException {
        if (locale == null) {
            return NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName)
                    .select(id, onType, onInstance, AnnotationStore
                    .locale().isNull());
        } else {
            return NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName)
                    .select(id, onType, onInstance, AnnotationStore
                    .locale().eq(locale.getLanguage()));
        }
    }

    /**
     * List list.
     *
     * @param userName   user name
     * @param onInstance the on instance
     * @param onType
     * @param locale     tha language
     * @return the list
     */
    public final List<Annotation> list(final String userName,
                                        final Locale locale,
                                        final String onType,
                                        final String onInstance)
            throws SQLException, IOException {
        if (locale == null) {
            return NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName)
                    .select().where(AnnotationStore.onType().eq(onType)
                            .and().locale().isNull()
                            .and().onInstance().eq(onInstance)).execute();
        } else {
            return NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName)
                    .select().where(AnnotationStore.onType().eq(onType)
                            .and().locale().eq(locale.getLanguage())
                            .and().onInstance().eq(onInstance)).execute();
        }
    }

    /**
     * Update Annotation optional.
     * @param userName
     * @param onInstance the on instance
     * @param onType
     * @param id         the id
     * @param annotation the user Annotation
     * @param locale     tha language
     * @return the optional
     */
    public final Optional<Annotation> update(final String userName,
                         final String id,
                         final String onType,
                         final String onInstance,
                          final Locale locale,
                          final Annotation annotation)
            throws SQLException, IOException {

        if (id.equals(annotation.id())) {
            int updated;
            if (locale == null) {
                updated = NoteBookUtil.getNoteBookUtil()
                        .getAnnotationStore(userName).update()
                    .set(AnnotationStore.body(annotation.body()),
                            AnnotationStore.target(annotation.target()))
                    .where(AnnotationStore.id().eq(id)
                            .and().onType().eq(onType)
                            .and().onInstance().eq(onInstance)
                            .and().locale().isNull())
                    .execute();
            } else {
                updated = NoteBookUtil.getNoteBookUtil()
                        .getAnnotationStore(userName).update()
                        .set(AnnotationStore.body(annotation.body()),
                        AnnotationStore.target(annotation.target()))
                    .where(AnnotationStore.id().eq(id)
                            .and().onType().eq(onType)
                            .and().onInstance().eq(onInstance)
                            .and().locale().eq(locale.getLanguage()))
                    .execute();
            }

            return read(userName, id, onType, onInstance, locale);


        } else {
            throw new IllegalArgumentException("Ids do not match");
        }
    }

    /**
     * Delete boolean.
     * @param userName
     * @param id     the id
     * @param onInstance the on instance
     * @param onType
     * @param locale tha language
     * @return the boolean
     */
    public final boolean delete(final String userName,
                                final String id,
                                final String onType,
                                final String onInstance,
                                final Locale locale)
            throws SQLException, IOException {
        if (locale == null) {
            return NoteBookUtil.getNoteBookUtil()
                    .getAnnotationStore(userName).delete(
                    AnnotationStore.id().eq(id)
                            .and().onType().eq(onType)
                            .and().onInstance().eq(onInstance)
                            .and().locale().isNull()).execute() == 1;
        }
        return NoteBookUtil.getNoteBookUtil()
                .getAnnotationStore(userName).delete(
                AnnotationStore.id().eq(id)
                        .and().onType().eq(onType)
                        .and().onInstance().eq(onInstance)
                        .and().locale()
                        .eq(locale.getLanguage())).execute() == 1;
    }

    /**
     * Deletes all Annotations.
     * @param userName
     */
    public void delete(final String userName)
            throws SQLException, IOException {
        NoteBookUtil.getNoteBookUtil()
                .getAnnotationStore(userName).delete().execute();
    }
}
