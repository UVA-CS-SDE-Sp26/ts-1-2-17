import java.util.List;

/**
 * Commmand Line Utility
 */
public class TopSecret {
    private static FileHandler fileHandler = new FileHandler();
    private static String fileName;


    public static void main(String[] args) {
        if(args.length == 0){
            List<String> fileNames = fileHandler.getAvailableFiles();
            for(int i = 0; i < fileNames.size(); i++){
                if(i<10){
                    System.out.println("0"+i+" "+fileNames.get(i));//if the file index is a single digit number, add a zero to the front
                }
                else{
                    System.out.println(i+ " "+ fileNames.get(i));
                }
            }
        }
        else{
            /*
            Fal's part of the code, including command line UI and non empty args test case
             */
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
