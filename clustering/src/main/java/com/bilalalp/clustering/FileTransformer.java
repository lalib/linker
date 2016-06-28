package com.bilalalp.clustering;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileTransformer {

    public static void main(final String[] args) throws IOException {

        final Stream<String> lines = Files.lines(Paths.get("C:\\patentdoc\\1068295060-1000.txt"));

        lines.parallel().map(k -> k.split("::")).forEach(p -> Arrays.stream(p[1].split("\\$")).parallel().map(d -> d.split(":"))
                .collect(Collectors.toMap(e -> Long.valueOf(e[0]), e -> Double.parseDouble(e[1])))
                .entrySet().parallelStream().map(d ->
                        Long.valueOf(p[0]).toString().concat(" ").concat(d.getKey().toString()).concat(" ").concat(d.getValue().toString())
                ).forEach(FileTransformer::writeToFile));

        lines.close();
    }

    private static void writeToFile(final String value) {

        try {

            File file = new File("C:\\patentdoc\\bilal.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            Files.write(Paths.get("C:\\patentdoc\\bilal.txt"), Collections.singletonList(value), Charset.forName("UTF-8"), StandardOpenOption.APPEND);

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
