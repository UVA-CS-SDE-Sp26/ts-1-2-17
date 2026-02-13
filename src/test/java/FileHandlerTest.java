import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FileHandlerTest {

    private FileHandler fileHandler;

    @TempDir
    Path tempDir; // JUnit will create a temporary directory for testing

    @BeforeEach
    public void setUp() throws IOException {
        // Create test files in the temp directory
        File dataDir = tempDir.resolve("data").toFile();
        dataDir.mkdirs();

        // Create sample test files
        createTestFile(dataDir, "filea.txt", "This is file A content.");
        createTestFile(dataDir, "fileb.txt", "This is file B content.");
        createTestFile(dataDir, "filec.txt", "This is file C content.");

        fileHandler = new FileHandler(dataDir.getAbsolutePath());
    }

    private void createTestFile(File dir, String filename, String content) throws IOException {
        File file = new File(dir, filename);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    @Test
    public void testGetAvailableFiles_ReturnsAllFiles() {
        List<String> files = fileHandler.getAvailableFiles();

        assertNotNull(files, "File list should not be null");
        assertEquals(3, files.size(), "Should have 3 files");
        assertTrue(files.contains("filea.txt"), "Should contain filea.txt");
        assertTrue(files.contains("fileb.txt"), "Should contain fileb.txt");
        assertTrue(files.contains("filec.txt"), "Should contain filec.txt");
    }

    @Test
    public void testGetAvailableFiles_EmptyDirectory() throws IOException {
        File emptyDir = tempDir.resolve("empty").toFile();
        emptyDir.mkdirs();

        FileHandler emptyHandler = new FileHandler(emptyDir.getAbsolutePath());
        List<String> files = emptyHandler.getAvailableFiles();

        assertNotNull(files, "File list should not be null");
        assertTrue(files.isEmpty(), "File list should be empty");
    }

    @Test
    public void testReadFile_ValidFile() throws IOException {
        String content = fileHandler.readFile("filea.txt");

        assertNotNull(content, "Content should not be null");
        assertEquals("This is file A content.", content, "Content should match");
    }

    @Test
    public void testReadFile_FileNotFound() {
        Exception exception = assertThrows(IOException.class, () -> {
            fileHandler.readFile("nonexistent.txt");
        });

        assertTrue(exception.getMessage().contains("not found") ||
                        exception.getMessage().contains("does not exist"),
                "Exception message should indicate file not found");
    }

    @Test
    public void testReadFile_EmptyFile() throws IOException {
        File dataDir = new File(fileHandler.getDataDirectory());
        createTestFile(dataDir, "empty.txt", "");

        String content = fileHandler.readFile("empty.txt");

        assertNotNull(content, "Content should not be null");
        assertEquals("", content, "Content should be empty string");
    }

    @Test
    public void testReadFile_MultilineContent() throws IOException {
        File dataDir = new File(fileHandler.getDataDirectory());
        String multilineContent = "Line 1\nLine 2\nLine 3";
        createTestFile(dataDir, "multiline.txt", multilineContent);

        String content = fileHandler.readFile("multiline.txt");

        assertNotNull(content, "Content should not be null");
        assertEquals(multilineContent, content, "Multiline content should match");
    }

    @Test
    public void testConstructor_InvalidDirectory() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new FileHandler("/this/path/does/not/exist");
        });

        assertTrue(exception.getMessage().contains("not exist") ||
                        exception.getMessage().contains("invalid"),
                "Exception should indicate invalid directory");
    }

    @Test
    public void testGetAvailableFiles_SortedAlphabetically() {
        List<String> files = fileHandler.getAvailableFiles();

        // Verify files are sorted
        assertEquals("filea.txt", files.get(0));
        assertEquals("fileb.txt", files.get(1));
        assertEquals("filec.txt", files.get(2));
    }

    @Test
    public void testDefaultConstructor() {
        File folder = new File("data");
        folder.mkdir();

        FileHandler handler = new FileHandler();
        String path = handler.getDataDirectory();
        assertEquals("data", path, "Default constructor should use data folder");

        folder.delete();
    }


    @Test
    public void testReadFile_Directory() throws IOException {
        File dataDir = new File(fileHandler.getDataDirectory());
        File subFolder = new File(dataDir, "subfolder");
        subFolder.mkdir();

        Exception exception = assertThrows(IOException.class, () -> {
            fileHandler.readFile("subfolder");
        });

        assertTrue(exception.getMessage().contains("Not a valid file"),
                "Should throw exception when trying to read a directory");
    }


    @Test
    public void testConstructor_PathIsFile() throws IOException {
        File file = tempDir.resolve("notDir.txt").toFile();
        file.createNewFile();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new FileHandler(file.getAbsolutePath());
        });

        assertTrue(exception.getMessage().contains("Directory does not exist")
                        || exception.getMessage().contains("invalid"),
                "Constructor should throw exception when path is a file");
    }


    @Test
    public void testGetAvailableFiles_IgnoresDirectories() throws IOException {
        File dataDir = new File(fileHandler.getDataDirectory());

        File subFolder = new File(dataDir, "folder");
        subFolder.mkdir();

        List<String> files = fileHandler.getAvailableFiles();

        assertNotNull(files, "File list should not be null");
        assertFalse(files.contains("folder"), "Should not include directory names");
    }


}