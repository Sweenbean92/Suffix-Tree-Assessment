import SuffixTreePackage.*;

/**
 * Main class - for accessing suffix tree applications
 * 
 * @author
 */

public class Main {

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String args[]) {
        String errorMessage = "Required syntax:\n";
        errorMessage += "  java Main SearchOne <filename> <query string> for Task 1\n";
        errorMessage += "  java Main SearchAll <filename> <query string> for Task 2\n";
        errorMessage += "  java Main LRS <filename> for Task 3\n";
        errorMessage += "  java Main LCS <filename1> <filename2> for Task 4";

        if (args.length < 2) {
            System.out.println(errorMessage);
        } else {
            String command = args[0];

            switch (command) {
                case "SearchOne": {
                    if (args.length < 3) {
                        System.out.println(errorMessage);
                        break;
                    }

                    String filename = args[1];
                    String queryString = args[2];

                    try {
                        FileInput fileInput = new FileInput(filename);
                        byte[] fileContent = fileInput.readFile();

                        SuffixTree tree = new SuffixTree(fileContent);
                        SuffixTreeAppl treeAppl = new SuffixTreeAppl(tree);

                        Task1Info result = treeAppl.searchSuffixTree(queryString.getBytes());

                        if (result.getPos() == -1) {
                            System.out.println("The string" + queryString + " not found in " + filename);
                        } else {
                            System.out.println("The string " + queryString + " was found at position " + result.getPos()
                                    + " in file " + filename);
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                }
                case "SearchAll": {
                    if (args.length < 3) {
                        System.out.println(errorMessage);
                        break;
                    }

                    String filename = args[1];
                    String queryString = args[2];

                    try {
                        FileInput fileInput = new FileInput(filename);
                        byte[] fileContent = fileInput.readFile();

                        SuffixTree tree = new SuffixTree(fileContent);
                        SuffixTreeAppl treeAppl = new SuffixTreeAppl(tree);

                        Task2Info result = treeAppl.allOccurrences(queryString.getBytes());

                        if (result.getPositions().isEmpty()) {
                            System.out.println("The string" + queryString + " not found in " + filename);
                            System.out.println("Query string not found in the file.");
                        } else {
                            System.out.println("The string " + queryString + " was found at positions: ");

                            for (Integer pos : result.getPositions()) {
                                System.out.println(pos);
                            }

                            System.out.println("The total number of occurences is: " + result.getPositions().size());
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                }
                case "LRS": {
                    if (args.length < 2) {
                        System.out.println(errorMessage);
                        break;
                    }

                    String filename = args[1];

                    try {
                        FileInput fileInput = new FileInput(filename);
                        byte[] fileContent = fileInput.readFile();

                        SuffixTree tree = new SuffixTree(fileContent);
                        SuffixTreeAppl treeAppl = new SuffixTreeAppl(tree);

                        Task3Info result = treeAppl.traverseForLrs();

                        if (result.getLen() == 0) {
                            System.out.println("No repeated substring found.");
                        } else {
                            System.out.println("An LRS in " + filename + "is of length: " + result.getLen());
                            System.out.println("Starting position of one occurence is " + result.getPos1());
                            System.out.println("Starting position of another occurence is " + result.getPos2());
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                }
                case "LCS": {
                    if (args.length < 3) {
                        System.out.println(errorMessage);
                        break;
                    }

                    String filename1 = args[1];
                    String filename2 = args[2];

                    try {
                        FileInput fileInput1 = new FileInput(filename1);
                        FileInput fileInput2 = new FileInput(filename2);
                        byte[] fileContent1 = fileInput1.readFile();
                        byte[] fileContent2 = fileInput2.readFile();

                        SuffixTree tree = new SuffixTree(fileContent1, fileContent2);
                        SuffixTreeAppl treeAppl = new SuffixTreeAppl(tree);

                        Task4Info result = treeAppl.traverseForLcs(fileContent1.length);

                        if (result.getLen() == 0) {
                            System.out.println("No common substring found.");
                        } else {
                            System.out.println("An LCS of " + filename1 + " and " + filename2 + " is of length: "
                                    + result.getLen());
                            System.out.println("Starting position in " + filename1 + " is " + result.getPos1());
                            System.out.println("Starting position in " + filename2 + " is " + result.getPos2());
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                }
                default:
                    System.out.println(errorMessage);
            }
        }
    }
}
