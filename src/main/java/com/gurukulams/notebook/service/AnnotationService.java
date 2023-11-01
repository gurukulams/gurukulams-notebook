package com.gurukulams.notebook.service;

import com.gurukulams.notebook.model.Annotation;
import com.gurukulams.notebook.store.AnnotationStore;
import com.gurukulams.notebook.util.NoteBookUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

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
                                   final String onInstance,
                                   final Annotation annotation,
                                   final Locale locale,
                                   final String onType)
            throws SQLException, IOException {
        annotation.setId(UUID.randomUUID());
        annotation.setOnType(onType);
        annotation.setOnInstance(onInstance);
        if (locale != null) {
            annotation.setLocale(locale.getLanguage());
        }
        return NoteBookUtil.getNoteBookUtil()
                .getAnnotationStore(userName)
                .insert()
                .values(annotation).returning();
    }


    /**
     * Read optional.
     * @param userName
     * @param id     the id
     * @param locale tha language
     * @return the optional
     */
    public final Optional<Annotation> read(final String userName,
                                           final UUID id,
                        final Locale locale)
            throws SQLException, IOException {
        if (locale == null) {
            return NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName)
                    .select(id, AnnotationStore
                    .locale().isNull());
        } else {
            return NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName)
                    .select(id, AnnotationStore
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
                    .select(AnnotationStore.onType().eq(onType)
                            .and().locale().isNull()
                            .and().onInstance().eq(onInstance)).execute();
        } else {
            return NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName)
                    .select(AnnotationStore.onType().eq(onType)
                            .and().locale().eq(locale.getLanguage())
                            .and().onInstance().eq(onInstance)).execute();
        }
    }

    /**
     * Update Annotation optional.
     * @param userName
     * @param id         the id
     * @param annotation the user Annotation
     * @param locale     tha language
     * @return the optional
     */
    public final Optional<Annotation> update(final String userName,
                         final UUID id,
                          final Locale locale,
                          final Annotation annotation)
            throws SQLException, IOException {

        if (id.equals(annotation.getId())) {
            if (locale == null) {
                NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName).update()
                    .set(AnnotationStore.note(annotation.getNote()))
                    .where(AnnotationStore.locale().isNull())
                    .execute();
            } else {
                NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName).update()
                        .set(AnnotationStore.note(annotation.getNote()))
                    .where(AnnotationStore.locale().eq(locale.getLanguage()))
                    .execute();
            }
            return read(userName, id, locale);
        } else {
            throw new IllegalArgumentException("Ids do not match");
        }
    }

    /**
     * Delete boolean.
     * @param userName
     * @param id     the id
     * @param locale tha language
     * @return the boolean
     */
    public final boolean delete(final String userName,
                                final UUID id,
                                final Locale locale)
            throws SQLException, IOException {
        if (locale == null) {
            return NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName).delete(
                    AnnotationStore.id().eq(id)
                            .and().locale().isNull()).execute() == 1;
        }
        return NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName).delete(
                AnnotationStore.id().eq(id)
                        .and().locale()
                        .eq(locale.getLanguage())).execute() == 1;
    }

    /**
     * Deletes all Annotations.
     * @param userName
     */
    public void delete(final String userName)
            throws SQLException, IOException {
        NoteBookUtil.getNoteBookUtil().getAnnotationStore(userName).delete().execute();
    }

    
}
