package keyCounter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigHandler {

    public static Logger LOGGER = LogManager.getLogger("keycounter");
    public static File configFile;
    private static JsonObject jsonConfig;

    public static int xPos;
    public static int yPos;

    public static void init(File file) {
        configFile = file;
        getConfig();
    }

    public static void getConfig() {
        if (configFile.exists()) {

            try {
                FileReader reader = new FileReader(configFile);

                JsonElement fileElement = new JsonParser().parse(reader);

                if (fileElement == null || fileElement.isJsonNull()) {
                    throw new JsonParseException("File is null!");
                }
                jsonConfig = fileElement.getAsJsonObject();
            } catch (Exception ex) {

                ex.printStackTrace();
                LOGGER.log(Level.FATAL, "keyCounter: There was an error loading the config. Resetting all settings to default.");
                addDefaultsAndSave();
                return;
            }

            try {
                if (jsonConfig.has("xPos")) {
                    xPos = jsonConfig.get("xPos").getAsInt();
                } else {
                    xPos = 120;
                }

                if (jsonConfig.has("yPos")) {
                    yPos = jsonConfig.get("yPos").getAsInt();
                } else {
                    yPos = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.log(Level.FATAL, "Error doing json config parse:" + e);
            }
        }
    }

    public static void addDefaultsAndSave() {
        xPos = 120;
        yPos = 0;
        saveConfig();
    }

    public static void saveConfig() {
        jsonConfig = new JsonObject();

        try {
            configFile.createNewFile();
            FileWriter writer = new FileWriter(configFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            jsonConfig.addProperty("xPos", xPos);
            jsonConfig.addProperty("yPos", yPos);

            bufferedWriter.write(jsonConfig.toString());
            bufferedWriter.close();
            writer.close();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Error saving config:" + e);
            e.printStackTrace();
        }
    }
}
