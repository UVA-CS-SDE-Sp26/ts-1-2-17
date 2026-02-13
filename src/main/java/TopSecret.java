import java.util.List;

/**
 * Command Line Utility
 */
public class TopSecret {
    private static FileHandler fileHandler = new FileHandler();
    private static String fileName;


    public static void main(String[] args) {
        if (args.length == 0) {
            List<String> fileNames = fileHandler.getAvailableFiles();

            for (int i = 0; i < fileNames.size(); i++) {
                if (i < 9) {
                    System.out.println("0" + (i + 1) + " " + fileNames.get(i));//if the file index is a single digit number, add a zero to the front
                } else {
                    System.out.println((i + 1) + " " + fileNames.get(i));
                }
            }
        } else {
            if (args[0].isEmpty()) {//if file number is an empty string, throw exception
                throw new IllegalArgumentException("Error: Invalid command");
            }

            String input = args[0];
            int index;
            try {
                index = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Error: File number must be numeric", e);
            }


            List<String> fileNames = fileHandler.getAvailableFiles();

            if (index < 1 || index > fileNames.size()) {
                throw new IllegalArgumentException("Error: File number out of range");
            }

            fileName = fileNames.get(index-1);

            try {
                System.out.println(fileHandler.readFile(fileName));
            } catch (Exception e) {
                throw new RuntimeException("Error: File not found or cannot be read", e);
            }
        }
    }


    public static String getFileName() {
        return fileName;
    }
    public static void setFileHandler(FileHandler fileHandler) {
        TopSecret.fileHandler = fileHandler;
    }


}
