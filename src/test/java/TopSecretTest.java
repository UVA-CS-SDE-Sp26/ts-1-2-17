import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TopSecretTest {


    //Here I am trying to find a TopSecret test that mockito would be useful for, i.e. a test of a TopSecret functionality that is dependent on the performance of FileHandler
//    @Mock
//    FileHandler fileHandler;
//
//    @Test
//    public void printsGivenFileContents() {//checks if TopSecret will print
//        when(fileHandler.getAvailableFiles())
//                .thenReturn(List.of("a.txt"));
//
//        TopSecret.setFileHandler(fileHandler);
//        TopSecret.printAvailableFiles();
//
//        verify(fileHandler).getAvailableFiles();
//    }

    @Test
    void testCorrectListOfFiles() {//checking that when no args are given, the correct list of files is printed
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Act
        String[] mainArgs = {};
        TopSecret.main(mainArgs);

        // Restore original System.out
        System.setOut(originalOut);

        // Assert
        String expected = "01 empty.txt\n02 multiline.txt\n03 oneline.txt" + System.lineSeparator();
        assertEquals(expected, outputStream.toString(), "should be printing the correct list of files");
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
}