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
                if (args.length < 1) {
                    System.out.println("Error: Invalid command");
                    return;
                }

                String input = args[0];
                int index = 0;

                for (int i = 0; i < input.length(); i++) {
                    char c = input.charAt(i);

                    if (c < '0' || c > '9') {
                        System.out.println("Error: File number must be numeric");
                        return;
                    }

                    index = index * 10 + (c - '0');
                }


                List<String> fileNames = fileHandler.getAvailableFiles();

                if (index < 0 || index >= fileNames.size()) {
                    System.out.println("Error: File number out of range");
                    return;
                }

                fileName = fileNames.get(index);

                try {
                    System.out.println(fileHandler.readFile(fileName));
                } catch (Exception e) {
                    System.out.println("Error: File not found or cannot be read");
                }
            }



        }


    public static FileHandler getFileHandler() {
        return fileHandler;
    }

    public static void setFileHandler(FileHandler fileHandler) {
        TopSecret.fileHandler = fileHandler;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        TopSecret.fileName = fileName;
    }

}
