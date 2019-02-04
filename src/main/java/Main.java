import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Main {

    public static final String FILE_PATH = "Test_Data.txt";

    public static void main(String[] args) throws IOException {

        List<Report> allReportedData = readData();

        severityByCharacterize(allReportedData, "driverName");
        severityByCharacterize(allReportedData, "roadType");
        severityByCharacterize(allReportedData, "country");
        severityByCharacterize(allReportedData, "illumination");
    }

    private static void severityByCharacterize(List<Report> allReportedData, String fieldName) {

        Class aClass = Report.class;
        Field field = null;

        try {
            field = aClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            System.out.println("Error getting declared  field: " + fieldName + " see exception: " + e.getStackTrace());
            return;
        }

        field.setAccessible(true);
        Map<String, Map<String, Integer>> mapCharacterToSeverityCount = Maps.newHashMap();

        for (Report report : allReportedData) {
            String fieldValue = null;
            try {
                fieldValue = (String) field.get(report);

                if (mapCharacterToSeverityCount.containsKey(fieldValue)) {
                    Map<String, Integer> severityCountMap = mapCharacterToSeverityCount.get(fieldValue);

                    if (severityCountMap.get(report.getSeverity()) != null) {
                        severityCountMap.put(report.getSeverity(), severityCountMap.get(report.getSeverity()) + 1);
                    } else {
                        severityCountMap.put(report.getSeverity(), 1);
                    }
                } else {
                    Map<String, Integer> severityCountMap = Maps.newHashMap();
                    severityCountMap.put(report.getSeverity(), 1);
                    mapCharacterToSeverityCount.put(fieldValue, severityCountMap);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        writeResultToFile(fieldName, mapCharacterToSeverityCount);

    }

    private static void writeResultToFile(String fieldName, Map<String, Map<String, Integer>> mapCharacterToSeverityCount) {

        PrintStream fileStream = null;
        try {
            fileStream = new PrintStream(new File(fieldName + ".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Map<String, Integer>> character : mapCharacterToSeverityCount.entrySet()) {
            fileStream.println(fieldName + ": " + character.getKey());
            for (Map.Entry<String, Integer> severity : character.getValue().entrySet()) {
                fileStream.println("\t" + severity.getKey() + " : " + severity.getValue());
            }
        }

        fileStream.close();
    }

    private static List<Report> readData() throws IOException {
        List<String> recordsLine = Files.readAllLines(Paths.get(FILE_PATH));
        List<Report> reports = Lists.newArrayList();

        recordsLine.remove(0);
        recordsLine.stream().forEach(record -> {

            Report report = new Report();
            String[] split = record.split("\t");
            report.setId(Integer.valueOf(split[0]));
            report.setCountry(split[1]);
            report.setIllumination(split[2].toLowerCase());
            report.setRoadType(split[3].toLowerCase());
            report.setSeverity(split[4].toLowerCase());
            report.setDriverName(split[5]);
            reports.add(report);

        });

        return reports;
    }
}
