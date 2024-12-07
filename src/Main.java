import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    private static ArrayList<String> list = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFile = "";

    public static void main(String[] args)
    {
        Scanner pipe = new Scanner(System.in);
        boolean quit = false;

        while (!quit)
        {
            printMenu();
            String command = SafeInput.getRegExString(pipe, "Choose an option", "[AaDdIiPpQqMSVvCOc]");

            try
            {
                switch (command.toUpperCase())
                {
                    case "A":
                        addItem(pipe);
                        break;
                    case "D":
                        deleteItem(pipe);
                        break;
                    case "I":
                        insertItem(pipe);
                        break;
                    case "V":
                        printList();
                        break;
                    case "M":
                        moveItem(pipe);
                        break;
                    case "O":
                        openFile(pipe);
                        break;
                    case "S":
                        saveFile(pipe);
                        break;
                    case "C":
                        clearList();
                        break;
                    case "Q":
                        quit = confirmQuit(pipe);
                        break;
                    default:
                        System.out.println("Invalid option try again.");
                }
            } catch (IOException e) {
                System.out.println("An error has occurred: " + e.getMessage());
            }
        }

        System.out.println("byebye!");
        pipe.close();
    }

    private static void printMenu()
    {
        if (list.isEmpty())
        {
            System.out.println("The list is empty.");
        }
        else
        {
            for (int C = 0; C < list.size(); C++)
            {
                System.out.println((C + 1) + ". " + list.get(C));
            }
        }

        System.out.println("\nMenu Options:");
        System.out.println("A - Add an item");
        System.out.println("D - Delete an item");
        System.out.println("I - Insert an item");
        System.out.println("V - View the list");
        System.out.println("M - Move an item");
        System.out.println("O - Open a list file");
        System.out.println("S - Save the current list");
        System.out.println("C - Clear the list");
        System.out.println("Q - Quit");
    }

    private static void addItem(Scanner pipe)
    {
        String newItem = SafeInput.getNonZeroLenString(pipe, "Enter the item to add");
        list.add(newItem);
        needsToBeSaved = true;
        System.out.println("Item added.");
    }

    private static void deleteItem(Scanner pipe)
    {
        if (list.isEmpty())
        {
            System.out.println("The list is empty so their is nothing that can be deleted.");
            return;
        }

        int itemToDelete = SafeInput.getRangedInt(pipe, "Enter the number of the item to delete", 1, list.size());
        list.remove(itemToDelete - 1);
        needsToBeSaved = true;
        System.out.println("Item deleted.");
    }

    private static void insertItem(Scanner pipe)
    {
        String newItem = SafeInput.getNonZeroLenString(pipe, "Enter the item to insert");
        int insertAt = SafeInput.getRangedInt(pipe, "Enter the position to insert the item", 1, list.size() + 1);
        list.add(insertAt - 1, newItem);
        needsToBeSaved = true;
        System.out.println("Item inserted.");
    }

    private static void moveItem(Scanner pipe)
    {
        if (list.isEmpty())
        {
            System.out.println("The list is empty.");
            return;
        }

        int itemToMove = SafeInput.getRangedInt(pipe, "Enter the number of the item to move", 1, list.size());
        int newPosition = SafeInput.getRangedInt(pipe, "Enter the new position for the item", 1, list.size());

        String item = list.remove(itemToMove - 1);
        list.add(newPosition - 1, item);
        needsToBeSaved = true;
        System.out.println("Item moved.");
    }

    private static void printList()
    {
        if (list.isEmpty())
        {
            System.out.println("The list is empty.");
        }
        else
        {
            System.out.println("Current List:");
            for (int i = 0; i < list.size(); i++)
            {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        }
    }

    private static boolean confirmQuit(Scanner pipe) throws IOException
    {
        if (needsToBeSaved)
        {
            boolean save = SafeInput.getYNConfirm(pipe, "You have unsaved changes. Do you want to save before quitting?");
            if (save)
            {
                saveFile(pipe);
            }
        }
        return true;
    }

    private static void openFile(Scanner pipe) throws IOException
    {
        if (needsToBeSaved)
        {
            boolean save = SafeInput.getYNConfirm(pipe, "You have unsaved changes. Do you want to save before opening a new file?");
            if (save)
            {
                saveFile(pipe);
            }
        }

        String fileName = SafeInput.getNonZeroLenString(pipe, "Enter the file name to open") + ".txt";
        Path path = Paths.get(fileName);

        if (Files.exists(path))
        {
            list = new ArrayList<>(Files.readAllLines(path));
            currentFile = fileName;
            needsToBeSaved = false;
            System.out.println("File loaded successfully.");
        }
        else
        {
            System.out.println("File not found.");
        }
    }

    private static void saveFile(Scanner pipe) throws IOException
    {
        if (currentFile.isEmpty())
        {
            currentFile = SafeInput.getNonZeroLenString(pipe, "Enter the file name to save to") + ".txt";
        }

        Path path = Paths.get(currentFile);
        Files.write(path, list);
        needsToBeSaved = false;
        System.out.println("File saved successfully.");
    }

    private static void clearList()
    {
        list.clear();
        needsToBeSaved = true;
        System.out.println("List cleared.");
    }
}
