package com.bilalalp.clustering;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.stream.Stream;

public class FileTransformer {

    public static void main(final String[] args) throws IOException {

        final String sourceFilePath = "C:\\patentdoc\\1070525572-1000.txt";
        final String destinationFilePath = "C:\\patentdoc\\right-values-7.txt";

        final Stream<String> lines = Files.lines(Paths.get(sourceFilePath));

        final StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append("@Relation Patent_Documents").append("\n");
        headerBuilder.append("@Attribute PatentId\tNUMERIC\n");

        for (int i = 0; i < 1000; i++) {
            headerBuilder.append("@Attribute Word").append(i + 1).append("\t").append("NUMERIC").append("\n");
        }

        headerBuilder.append("\n@Data");
        writeToFile(headerBuilder.toString(),destinationFilePath);

        lines.map(k -> k.split("::")).forEach(p ->
        {
            final String patentId = p[0];
            final String[] wordArray = p[1].split("\\$");

            final StringBuilder stringBuilder = new StringBuilder(patentId).append(",");

            for (int i = 0; i < wordArray.length; i++) {
                stringBuilder.append(wordArray[i].split(":")[1]);

                if (wordArray.length - 1 != i) {
                    stringBuilder.append(",");
                }
            }
            writeToFile(stringBuilder.toString(),destinationFilePath);
        });

        lines.close();
    }

    private static void writeToFile(final String value,final String destinationFilePath) {

        try {

            File file = new File(destinationFilePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            Files.write(Paths.get(destinationFilePath), Collections.singletonList(value), Charset.forName("UTF-8"), StandardOpenOption.APPEND);

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
