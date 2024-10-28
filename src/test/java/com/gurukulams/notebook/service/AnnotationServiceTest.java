package com.gurukulams.notebook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gurukulams.notebook.model.Annotation;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

class AnnotationServiceTest {

    public static final String USER_NAME = "mani";
    public static final String USER_NAME2 = "mani2";
    private static final String TYPE = "TYPE";
    private static final String INSTANCE = "INSTANCE";
    private final AnnotationService annotationService;

    ObjectMapper mapper = new ObjectMapper();

    AnnotationServiceTest() {
        this.annotationService = new AnnotationService();
    }

    /**
     * Before.
     *
     * @throws SQLException the io exception
     */
    @BeforeEach
    void before() throws SQLException, IOException {
        cleanUp();
    }

    /**
     * After.
     */
    @AfterEach
    void after() throws SQLException, IOException {
        cleanUp();
    }

    private void cleanUp() throws SQLException, IOException {
        annotationService.delete(USER_NAME);
    }

    @Test
    void create() throws SQLException, IOException {
        final Annotation annotation = annotationService.create(
                USER_NAME, TYPE,INSTANCE, anAnnotation(), null
        );
        Assertions.assertTrue(annotationService.read(USER_NAME,annotation.id(),TYPE,INSTANCE,
                        null).isPresent(),
                "Created Annotation");
    }

    @Test
    void read() throws SQLException, IOException {
        final Annotation annotation = annotationService.create(
                USER_NAME,TYPE,INSTANCE, anAnnotation(), null
        );
        final String newAnnotationId = annotation.id();
        Assertions.assertTrue(annotationService.read(USER_NAME,newAnnotationId,TYPE,INSTANCE, null).isPresent(),
                "Annotation Created");

        Assertions.assertTrue(annotationService.read(USER_NAME,newAnnotationId,TYPE,INSTANCE,
                        Locale.GERMAN).isEmpty(),
                "Annotation Unavailable for Locale");
    }

    @Test
    void readLocalized() throws SQLException, IOException {
        final Annotation annotation = annotationService.create(
                USER_NAME,TYPE,INSTANCE, anAnnotation(), Locale.GERMAN
        );
        final String newAnnotationId = annotation.id();
        Assertions.assertTrue(annotationService.read(USER_NAME,newAnnotationId,TYPE,INSTANCE, null).isEmpty(),
                "Annotation Unavailable for English");

        Assertions.assertTrue(annotationService.read(USER_NAME,newAnnotationId,TYPE,INSTANCE,
                        Locale.GERMAN).isPresent(),
                "Annotation Available for Locale");
    }

    @Test
    void list() throws SQLException, IOException {

        testList(null);
        testList(Locale.GERMAN);

    }

    void testList(Locale locale) throws SQLException, IOException {
        annotationService.create(
                USER_NAME,TYPE,INSTANCE, anAnnotation(), locale
        );
        Annotation newAnnotation = anAnnotation();
        JsonNode jsonObject = mapper.readTree("""
                {
                "a": "a",
                "b": "b2"
                }
                """);
        newAnnotation = newAnnotation.withBody(jsonObject);
        annotationService.create(USER_NAME,TYPE,INSTANCE, newAnnotation, locale
        );
        List<Annotation> listofannotation = annotationService.list(USER_NAME,
                locale, TYPE,
                INSTANCE
        );
        Assertions.assertEquals(2, listofannotation.size());
    }
    @Test
    void update() throws SQLException, IOException {
        testUpdate(null);
        testUpdate(Locale.GERMAN);
    }

    void testUpdate(Locale locale) throws SQLException, IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", "a");
        jsonObject.put("b", "b2");

        final Annotation annotation = annotationService.create(
                USER_NAME,TYPE,INSTANCE, anAnnotation(), locale
        ).withBody(mapper.readTree(jsonObject.toString()));

        Optional<Annotation> updatedAnnotation = annotationService
                .update(USER_NAME, annotation.id(), TYPE,INSTANCE,locale, annotation);

        Assertions.assertEquals("b2", updatedAnnotation.get()
                .body().get("b").asText(), "Updated");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            annotationService
                    .update(USER_NAME, UUID.randomUUID().toString(), TYPE,INSTANCE,locale, annotation);
        });
    }
    @Test
    void delete() throws SQLException, IOException {
        testDelete(null);
        testDelete(Locale.GERMAN);
    }

    void testDelete(Locale locale) throws SQLException, IOException {
        final Annotation annotation = annotationService.create(
                USER_NAME,TYPE,INSTANCE, anAnnotation(), locale
        );
        annotationService.delete(USER_NAME, annotation.id(), TYPE, INSTANCE, locale);
        Assertions.assertFalse(annotationService.read(USER_NAME,annotation.id(),TYPE,INSTANCE, locale).isPresent(),
                "Deleted Annotation");
    }

    @Test
    void testMultiUser() throws SQLException, IOException {
        annotationService.create(
                USER_NAME, TYPE, INSTANCE, anAnnotation(), null
        );

        annotationService.create(USER_NAME, TYPE, INSTANCE, anAnnotation(), null
        );

        annotationService.create(USER_NAME2, TYPE, INSTANCE, anAnnotation(), null
        );

        List<Annotation> listofannotation = annotationService.list(USER_NAME,
                null, TYPE,
                INSTANCE
        );
        Assertions.assertEquals(2, listofannotation.size());
    }

    private Annotation anAnnotation() throws JsonProcessingException {
        Annotation annotation = new Annotation(UUID.randomUUID().toString(),null,null
                ,"Annotation",
        "Linking",null,
                mapper.readTree("""
                [
                  {
                    "purpose": "commenting",
                    "type": "TextualBody",
                    "value": "Note 1"
                  }
                ]
                """),mapper.readTree("""
                {
                      "selector": [
                        {
                          "exact": "Growth increases",
                          "type": "TextQuoteSelector"
                        },
                        {
                          "start": 0,
                          "end": 16,
                          "type": "TextPositionSelector"
                        }
                      ]
                    }
                """));
        return annotation;
    }

}