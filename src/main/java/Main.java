import com.gutsolk.indexing.FileIndex;
import com.gutsolk.indexing.MapFileIndex;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Main {
    private static final String ADD = "add";
    private static final String GET = "get";
    private static final String HELP = "help";
    private static final String EXIT = "exit";

    public static void main(String... args) {
        FileIndex fileIndex = new MapFileIndex();
        Scanner in = new Scanner(System.in);
        while (true) {
            String next = in.nextLine();
            String[] input = next.split("\\s+");
            if (input.length == 0) {
                continue;
            }
            if (ADD.equals(input[0])) {
                add(fileIndex, next, input);
            } else if (GET.equals(input[0])) {
                get(fileIndex, next, input);
            } else if (EXIT.equals(input[0])) {
                break;
            } else if (HELP.equals(input[0])) {
                help();
            } else {
                invalidInput(next);
            }
        }
    }

    private static void get(FileIndex fileIndex, String next, String[] input) {
        if (input.length > 2) {
            invalidInput(next);
            return;
        }
        String key = input.length == 2 ? input[1] : "";
        List<String> files = fileIndex.find(key);
        System.out.println("Got: \n" +
                (files.isEmpty() ? "Nothing" : "") +
                files.stream()
                        .collect(Collectors.joining("\n")));
    }

    private static void add(FileIndex fileIndex, String next, String[] input) {
        if (input.length != 2) {
            invalidInput(next);
            return;
        }
        try {
            fileIndex.add(input[1]);
            System.out.println(
                    format("Successfully added file \"%s\"", input[1]));
        } catch (Exception e) {
            System.out.println(
                    format("Unable to add file cause: %s",
                            e.getMessage()));
        }
    }

    private static void invalidInput(String input) {
        System.out.println(format("Invalid input \"%s\"\n", input));
        help();
    }

    private static void help() {
        System.out.println("Possible commands:\n" +
                "add <filepath> -- adds file to index\n" +
                "get <key> -- list all files containing the string \"key\"\n" +
                "exit -- to exit the program\n" +
                "help -- to see help\n");
    }
}
