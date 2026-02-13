import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileHandler {

    private final String dataDirectory;

    /**
     * Constructor for FileHandler
     * @param dataDirectory Path to the directory containing data files
     * @throws IllegalArgumentException if directory does not exist
     */
    public FileHandler(String dataDirectory) {
        File dir = new File(dataDirectory);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IllegalArgumentException("Directory does not exist or is invalid: " + dataDirectory);
        }
        this.dataDirectory = dataDirectory;
    }

    /**
     * Default constructor - uses "data" folder in current directory
     */
    public FileHandler() {
        this("data");
    }

    /**
     * Get list of all available files in the data directory
     * @return List of filenames (sorted alphabetically)
     */
    public List<String> getAvailableFiles() {
        File folder = new File(dataDirectory);

        File[] fileList = folder.listFiles();
        List<String> files = new ArrayList<>();

        for (File file : fileList) {
            if (file.isFile()) {
                files.add(file.getName());
            }
        }

        Collections.sort(files);
        return files;
    }



    /**
     * Read the contents of a specific file
     * @param filename Name of the file to read
     * @return Contents of the file as a string
     * @throws IOException if file cannot be read or does not exist
     */
    public String readFile(String filename) throws IOException {
        Path filePath = Paths.get(dataDirectory, filename);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new IOException("File not found: " + filename);
        }

        if (!file.isFile()) {
            throw new IOException("Not a valid file: " + filename);
        }

        // Read all content and return as string
        return Files.readString(filePath);
    }

    /**
     * Get the data directory path
     * @return Path to data directory
     */
    public String getDataDirectory() {
        return dataDirectory;
    }
}