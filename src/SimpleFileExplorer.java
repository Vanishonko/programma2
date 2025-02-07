import java.util.*;

// Composite Pattern (Structural)
interface FileSystemNode {
    String getName();
    void display();
}

class File implements FileSystemNode {
    private String name;
    private String content;

    public File(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getContent() { return content; }

    @Override
    public String getName() { return name; }

    @Override
    public void display() {
        System.out.println("File: " + name);
    }
}

class Directory implements FileSystemNode {
    private String name;
    private List<FileSystemNode> children = new ArrayList<>();

    public Directory(String name) {
        this.name = name;
    }

    public void addChild(FileSystemNode node) {
        children.add(node);
    }

    @Override
    public String getName() { return name; }

    @Override
    public void display() {
        System.out.println("Directory: " + name);
        for (FileSystemNode child : children) {
            child.display();
        }
    }

    public List<FileSystemNode> getChildren() { return children; }
}

// Factory Method Pattern (Creational)
class FileSystemFactory {
    public static File createFile(String name, String content) {
        return new File(name, content);
    }

    public static Directory createDirectory(String name) {
        return new Directory(name);
    }
}

// Command Pattern (Behavioral)
interface Command {
    void execute();
}

class ListCommand implements Command {
    private Directory directory;

    public ListCommand(Directory directory) {
        this.directory = directory;
    }

    @Override
    public void execute() {
        System.out.println("Contents of " + directory.getName() + ":");
        for (FileSystemNode child : directory.getChildren()) {
            child.display();
        }
    }
}

class ReadCommand implements Command {
    private File file;

    public ReadCommand(File file) {
        this.file = file;
    }

    @Override
    public void execute() {
        System.out.println("Content: " + file.getContent());
    }
}

// Main Application
public class SimpleFileExplorer {
    public static void main(String[] args) {
        // Create file structure using Factory
        Directory root = FileSystemFactory.createDirectory("Root");
        File file1 = FileSystemFactory.createFile("hello.txt", "Hello World!");
        Directory docs = FileSystemFactory.createDirectory("Documents");

        docs.addChild(FileSystemFactory.createFile("todo.txt", "Finish assignment"));
        root.addChild(file1);
        root.addChild(docs);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nCurrent directory: Root");
            System.out.println("Available commands:");
            System.out.println("1. list - List contents");
            System.out.println("2. read <filename> - Read file");
            System.out.println("3. exit");
            System.out.print("Enter command: ");

            String input = scanner.nextLine();
            String[] parts = input.split(" ");

            switch (parts[0].toLowerCase()) {
                case "list":
                    new ListCommand(root).execute();
                    break;

                case "read":
                    if (parts.length < 2) {
                        System.out.println("Please specify filename");
                        break;
                    }
                    File target = findFile(root, parts[1]);
                    if (target != null) {
                        new ReadCommand(target).execute();
                    } else {
                        System.out.println("File not found");
                    }
                    break;

                case "exit":
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid command");
            }
        }
    }

    private static File findFile(Directory dir, String filename) {
        for (FileSystemNode node : dir.getChildren()) {
            if (node instanceof File && node.getName().equalsIgnoreCase(filename)) {
                return (File) node;
            }
        }
        return null;
    }
}