import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TopSecretTest {

    @Mock
    FileHandler fileHandler;
    //note that since we are using a mocked fileHandler object, we reduced the code coverage from these tests of the FileHandler class
    //down to only using its constructor. we are not calling any FileHandler methods, we are mocking all of them. We do this so these tests
    //dont depend on the performance of the FileHandler class, only that of the TopSecret class


    @BeforeEach
    void setUp(){//each of these tests will use a mock FileHandler that returns this list of 10 fileNames
        lenient().when(fileHandler.getAvailableFiles()).thenReturn(List.of( //lenient() makes mockito not freak out when we do this when.thenReturn statement, and proceed to not use it for that specific test
                "firstfile.txt", // the alternative would be to only include this line in the methods that actually use it. That would be more efficient, but this is cleaner code-wise
                "secondfile.txt",
                "thirdfile.txt",
                "fourthfile.txt",
                "fifthfile.txt",
                "sixthfile.txt",
                "seventhfile.txt",
                "eighthfile.txt",
                "ninthfile.txt",
                "tenthfile.txt"
        ));

        TopSecret.setFileHandler(fileHandler);
    }

    @AfterAll
    static void tearDown() {//at the end of testing, reset TopSecret's fileHandler to a real one, not a mock one
        TopSecret.setFileHandler(new FileHandler());
    }

    @Test
    void printsTenFilesWithCorrectFormatting() { //tests 0 arg case, using mockito to mock 10 files being in our folder, and checking that the file names and numbers are printed correctly
        // Capture System.out
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Act
        String[] args = {};
        TopSecret.main(args);

        // Restore System.out
        System.setOut(originalOut);

        // Expected output
        String expected =
                "01 firstfile.txt\n" +
                        "02 secondfile.txt\n" +
                        "03 thirdfile.txt\n" +
                        "04 fourthfile.txt\n" +
                        "05 fifthfile.txt\n" +
                        "06 sixthfile.txt\n" +
                        "07 seventhfile.txt\n" +
                        "08 eighthfile.txt\n" +
                        "09 ninthfile.txt\n" +
                        "10 tenthfile.txt" +
                        System.lineSeparator();

        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void fileOne(){//checks that when the first file is picked, the resulting fileName is "firstfile.txt"
        String[] mainArgs = {"01"};
        TopSecret.main(mainArgs);
        assertEquals("firstfile.txt", TopSecret.getFileName(), "1st file name should be firstfile.txt");
    }

    @Test
    public void fileOneWithCipher(){//checks the same thing, but confirming that program still works when a cipher argument is passed
        String[] mainArgs = {"1", "cipherkey"};
        TopSecret.main(mainArgs);
        assertEquals("firstfile.txt", TopSecret.getFileName(), "1st file name should be firstfile.txt");
    }

    @Test
    public void fileTen(){//checks that when the tenth file is picked, the resulting fileName is "tenthfile.txt"
        String[] mainArgs = {"10"};
        TopSecret.main(mainArgs);
        assertEquals("tenthfile.txt", TopSecret.getFileName(), "10th file name should be tenthfile.txt");
    }

    @Test
    public void fileTenWithCipher(){//checks the same thing, but confirming that program still works when a cipher argument is passed
        String[] mainArgs = {"0010", "cipherkey"};
        TopSecret.main(mainArgs);
        assertEquals("tenthfile.txt", TopSecret.getFileName(), "10th file name should be tenthfile.txt");
    }



    //tests for the inputs that should throw exceptions
    @Test
    void nonNumericInputThrowsException() {
        String[] args = {"123abc"};

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> TopSecret.main(args));

        assertEquals("Error: File number must be numeric",
                exception.getMessage());
    }

    @Test
    void outOfRangeSmallInputThrowsException() {
        String[] args = {"-1"};

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> TopSecret.main(args));

        assertEquals("Error: File number out of range",
                exception.getMessage());
    }

    @Test
    void outOfRangeBigInputThrowsException() {
        String[] args = {"14"};

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> TopSecret.main(args));

        assertEquals("Error: File number out of range",
                exception.getMessage());
    }

    @Test
    void emptyStringInputThrowsNumericException() {
        String[] args = {""};

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> TopSecret.main(args));

        assertEquals("Error: File number must be numeric",
                exception.getMessage());
    }


    @Test
    public void invalidFileNameThrowsRuntimeException() throws IOException { //mocks a fileHandler object to give our TopSecret class the wrong list of fileNames. When the fileHandler's readFile method throws an exception, TopSecret should throw the exception "Error: File not found or cannot be read"
        // Create a fresh mock just for this test
        FileHandler localMock = mock(FileHandler.class);

        // Stub it
        when(localMock.getAvailableFiles())
                .thenReturn(List.of("invalid_name.txt"));

        when(localMock.readFile("invalid_name.txt"))
                .thenThrow(new RuntimeException("Simulated failure"));

        // Inject into TopSecret
        TopSecret.setFileHandler(localMock);

        String[] args = {"1"};

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> TopSecret.main(args));

        assertEquals("Error: File not found or cannot be read",
                exception.getMessage());
    }
}
