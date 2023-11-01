package com.gurukulams.notebook.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CodeGen {
    private CodeGen() { }

    /**
     * Main.
     * @param args
     */
    public static void main(final String[] args) {
        ClassLoader classLoader = TestUtil.class.getClassLoader();
        File file = new File("target/generated-sources/notebook/com/gurukulams/notebook/NoteBookManager.java");
        System.out.println("Hello \n\n\n\n\n\n\n\n\n\n\n\n\n\n " + file.getAbsolutePath());

        replaceAll(file,"return jsonText == null ? null : new JSONObject(jsonText);","return jsonText == null ? null : new JSONObject(jsonText.substring(1,jsonText.length() - 1).replace(\"\\\\\", \"\"));");
    }

    private static void replaceAll(File file, String text, String replacement) {

        Path path = file.toPath();
        // Get all the lines
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            // Do the replace operation
            List<String> list = stream.map(line -> line.replace(text, replacement)).collect(Collectors.toList());
            // Write the content back
            Files.write(path, list, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
