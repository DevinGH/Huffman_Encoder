import java.nio.file.*;
import java.util.*;
import java.io.*;
import java.util.stream.IntStream;


/**
 * Main Driver Program for Coding Tree
 */
public class Main {
    /**
     * Classes main method for running the program using the novel War and Peace
     * @param args
     */
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        double timestart = System.currentTimeMillis();
        Scanner inputFile = new Scanner(new File(args[0]));
        FileOutputStream encodedFile = new FileOutputStream("compressed.txt");
        FileWriter outputFile = new FileWriter("decoded.txt");

        String inputString = inputFile.useDelimiter("\\A").next();
        CodingTree tree = new CodingTree(inputString);

        BitSet textEncoded = tree.encode();
        encodedFile.write(textEncoded.toByteArray());
        encodedFile.close();
        long encodedSize = Files.size(Paths.get("compressed.txt")) / 1024;

        PrintWriter writer = new PrintWriter("codes.txt");
        writer.println(tree.codeString());
        writer.close();

        String binaryAsString = main.getBinaryString(BitSet.valueOf(textEncoded.toByteArray()));
        String textDecoded = tree.decode(binaryAsString);
        outputFile.write(textDecoded);
        outputFile.close();
        long decodedSize = Files.size(Paths.get("decoded.txt")) / 1024;

        double endtime = System.currentTimeMillis();
        System.out.println("Size of Compressed File: " + encodedSize + " Kibibytes");
        System.out.println("Size of Decoded File: " + decodedSize + " Kibibytes");
        System.out.println("Compression Ratio: " + ((double)encodedSize/(double)decodedSize) * 100 + "%");
        System.out.println("Runtime: " + (endtime - timestart) + " milliseconds");

    }

    /**
     * Returns the compressed bitset as a String representation of itself
     * Source that helped with implementation: https://stackoverflow.com/questions/34748006/how-to-convert-bitset-to-binary-string-effectively
     * @param compressed
     * @return
     */
    public String getBinaryString(BitSet compressed){
        StringBuilder output = new StringBuilder(compressed.length());

        IntStream.range(0, compressed.length()).mapToObj(i -> compressed.get(i) ? '1' : '0').forEach(output::append);

        return output.toString();
    }

    /**
     * Method to test the methods in CodingTree class
     */
    public void testCodingTree(){
        Main main = new Main();
        CodingTree testtree = new CodingTree("aaaaaaaabbbbbbbcccccccccdd");

        BitSet encoded = testtree.encode();
        System.out.println(encoded);

        testtree.codeString();

        String binaryAsString = main.getBinaryString(BitSet.valueOf(encoded.toByteArray()));
        String text = testtree.decode(binaryAsString);
        System.out.println(text);
    }
}
