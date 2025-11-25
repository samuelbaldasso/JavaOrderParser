import entities.LineItem;
import entities.User;
import services.OrderAggregator;
import utils.JsonExporter;
import utils.LineParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        Path inputDir;
        if (args.length > 0) {
            inputDir = Paths.get(args[0]);
        } else {
            inputDir = Paths.get("json");
        }

        if (!Files.isDirectory(inputDir)) {
            System.err.println("Diretório de entrada não encontrado: " + inputDir.toAbsolutePath());
            return;
        }

        LineParser lineParser = new LineParser();
        List<LineItem> allItems = new ArrayList<>();

        try (Stream<Path> paths = Files.list(inputDir)) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".txt"))
                    .forEach(path -> {
                        try {
                            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                            for (String line : lines) {
                                if (line == null || line.isBlank()) {
                                    continue;
                                }
                                LineItem item = lineParser.parse(line);
                                allItems.add(item);
                            }
                        } catch (IOException e) {
                            System.err.println("Erro ao ler arquivo: " + path + " -> " + e.getMessage());
                        } catch (RuntimeException e) {
                            System.err.println("Erro ao parsear linha em " + path + ": " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Erro ao listar arquivos em: " + inputDir + " -> " + e.getMessage());
            return;
        }

        OrderAggregator aggregator = new OrderAggregator();
        List<User> users = aggregator.aggregate(allItems);

        JsonExporter exporter = new JsonExporter();
        String json = exporter.toJson(users);

        Path outputFile = Paths.get("output.json");
        try {
            Files.writeString(outputFile, json, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
            System.out.println("Arquivo JSON gerado em: " + outputFile.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo JSON: " + e.getMessage());
        }
    }
}
