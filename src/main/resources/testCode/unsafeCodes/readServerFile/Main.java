import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{
        String file = System.getProperty("user.dir") + File.separator + "src/main/resources/application.yml";
        List<String> lines = Files.readAllLines(Paths.get(file));
        System.out.println(String.join("\n", lines));
    }
}