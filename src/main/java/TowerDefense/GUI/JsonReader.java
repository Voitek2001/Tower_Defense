package TowerDefense.GUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class JsonReader {

    private final JSONObject config;

    public JsonReader(String pathToConfig) throws IOException, JSONException {
        try {
            String contents = new String((Files.readAllBytes(Paths.get(pathToConfig))));
            this.config = new JSONObject(contents);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Plik do wczytania nie istnieje");
        }
        catch (IOException | JSONException e) {
            System.out.println("Problem podczas wczytywania pliku konfiguracyjnego json");
            System.out.println("Sprawdź dokładnie zawartość pliku i nazwe poszczególnych klas");
            throw new RuntimeException(e);
        }
    }

    public JSONObject getConfig(String nameOfClass) {
        return this.config.getJSONObject(nameOfClass);
    }

    public Optional<String> getElementOfConfig(String elementOfConfig) {
        if (!this.config.has(elementOfConfig)) {
            System.out.println("Żądany element" + elementOfConfig + "nie znajduje się w wczytanym pliku/klasie");
            return Optional.empty();
        }
        return Optional.of(this.config.getString(elementOfConfig));

    }

    public JSONObject getConfig() { // in case u need this to debug etc.
        return config;
    }

}
