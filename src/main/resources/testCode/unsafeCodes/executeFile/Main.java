import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{
        String file = System.getProperty("user.dir") + File.separator + "src/main/resources/dan.sh";
        String dangerousCmd = "java -version 2>&1";
        Files.write(Paths.get(file), Arrays.asList(dangerousCmd));
        Process process = Runtime.getRuntime().exec(file);
        process.waitFor();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String lines;
        while ((lines = bufferedReader.readLine()) != null) {
            System.out.println(lines);
        }

    }
}