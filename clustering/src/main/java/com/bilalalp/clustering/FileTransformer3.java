package com.bilalalp.clustering;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.stream.Stream;

@Service
public class FileTransformer3 implements Serializable{

    public void createFile() throws IOException {

        final String filePath = "C:\\patentdoc\\1070232701-10000.txt";

        final Stream<String> lines = Files.lines(Paths.get(filePath));


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
                    writeToFile(stringBuilder.toString());
                });

            lines.close();
    }

    private static void writeToFile(final String value) {

        try {

            File file = new File("C:\\patentdoc\\bilal12.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            Files.write(Paths.get("C:\\patentdoc\\bilal12.txt"), Collections.singletonList(value), Charset.forName("UTF-8"), StandardOpenOption.APPEND);

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
