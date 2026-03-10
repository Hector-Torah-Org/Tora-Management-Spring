package edu.kit.datamanager.hector25.tora_game_management_service.config;


import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Image;

public class CsvReaderService {

    public static List<Image> readImagesFromCsv(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> lines = csvReader.readAll();

        int i = 0;
        List<Image> images = new ArrayList<>();

        for (String[] line : lines) {
            i++;
            if (i == 1) { continue; }

            String datasetI = line[2];
            String datasetII = line[3];

            if (!datasetI.equals(datasetII) && !datasetII.isBlank() && !datasetI.isBlank()) {
                throw new Exception("Dataset I and II are not equal in line " + i);
            }

            if (datasetI.equals("1.0") || datasetII.equals("1.0")) {
                images.add(new Image(Boolean.TRUE, line[4], line[1].charAt(0)));
            } else if (datasetI.equals("0.0") || datasetII.equals("0.0")) {
                images.add(new Image(Boolean.FALSE, line[4], line[1].charAt(0)));
            } else {
                images.add(new Image(line[4], line[1].charAt(0)));
            }
        }

        return images;
    }

    public static List<Image> readImagesFromCsvs(List<Reader> fileReaders) throws Exception {
        List<Image> images = new ArrayList<>();
        for (Reader reader : fileReaders) {
            images.addAll(readImagesFromCsv(reader));
        }
        return images;
    }
}
