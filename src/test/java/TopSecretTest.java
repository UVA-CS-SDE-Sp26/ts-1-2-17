import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @AfterEach
    void tearDown() {//make sure that the test methods that use the mock fileHandler don't prevent other tests from using a real fileHandler
        TopSecret.setFileHandler(new FileHandler());
    }

    @Test
    void printsTenFilesWithCorrectFormatting() { //tests 0 arg case, using mockito to mock 10 files being in our folder, and checking that the file names and numbers are printed correctly

        // Arrange: mock 10 file names
        when(fileHandler.getAvailableFiles()).thenReturn(List.of(
                "firstfile.txt",
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
    public void fileZero(){//checks that when the 0'th file is picked, the resulting fileName is "empty.txt"
        String[] mainArgs = {"01"};
        TopSecret.main(mainArgs);
        assertEquals("empty.txt", TopSecret.getFileName(), "0'th file name should be empty.txt");
    }

    @Test
    public void fileZeroWithCipher(){//checks the same thing, but confirming that program still works when a cipher argument is passed
        String[] mainArgs = {"1", "cipherkey"};
        TopSecret.main(mainArgs);
        assertEquals("empty.txt", TopSecret.getFileName(), "0'th file name should be empty.txt");
    }

    @Test
    public void fileOne(){//checks that when the 1'th file is picked, the resulting fileName is "multiline.txt"
        String[] mainArgs = {"2"};
        TopSecret.main(mainArgs);
        assertEquals("multiline.txt", TopSecret.getFileName(), "1st file name should be multiline.txt");
    }

    @Test
    public void fileOneWithCipher(){//checks the same thing, but confirming that program still works when a cipher argument is passed
        String[] mainArgs = {"02", "cipherkey"};
        TopSecret.main(mainArgs);
        assertEquals("multiline.txt", TopSecret.getFileName(), "1st file name should be multiline.txt");
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
        String[] args = {"0"};

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> TopSecret.main(args));

        assertEquals("Error: File number out of range",
                exception.getMessage());
    }

    @Test
    void outOfRangeBigInputThrowsException() {
        String[] args = {"9999"};

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

        assertEquals("Error: Invalid command",
                exception.getMessage());
    }



    @Test
    public void invalidFileNameThrowsRuntimeException() throws IOException { //mocks a fileHandler object to give our TopSecret class the wrong list of fileNames. When the fileHandler's readFile method throws an exception, TopSecret should throw the exception "Error: File not found or cannot be read"

        when(fileHandler.getAvailableFiles())
                .thenReturn(List.of("invalid_name.txt"));

        when(fileHandler.readFile("invalid_name.txt"))
                .thenThrow(new RuntimeException("Simulated failure"));

        TopSecret.setFileHandler(fileHandler);

        String[] args = {"1"};

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> TopSecret.main(args));

        assertEquals("Error: File not found or cannot be read",
                exception.getMessage());

        verify(fileHandler).getAvailableFiles();
        verify(fileHandler).readFile("invalid_name.txt");
    }

}
