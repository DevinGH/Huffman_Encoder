import java.util.*;

/**
 * Class implements the behavior stated in Huffman Coding
 * Source that helped with implementation: https://www.youtube.com/watch?v=21_bJLB7gyU
 */
public class CodingTree {
    /**
     * Instance Variables
     */
    private Map<Character, String> codes;
    private Map<Character, Integer> charFrequencies;
    private String message;
    private Node root;

    /**
     * Constructor
     * @param message
     */
    public CodingTree(String message){
        this.message = message;
        getFrequencyMap();
        codes = new HashMap<>();
    }

    /**
     * Adds the frequencies of each character into a map
     */
    private void getFrequencyMap(){
        charFrequencies = new HashMap<>();

        for(char character : message.toCharArray()){
            Integer charFrequency = charFrequencies.get(character);

            charFrequencies.put(character, charFrequency != null ? charFrequency + 1 : 1);
        }
    }

    /**
     * Encodes text into a binary equivalent
     * @return
     */
    public BitSet encode(){
        Queue<Node> queue = new PriorityQueue<>();

        charFrequencies.forEach((character, integer) ->
                queue.add(new CharNode(character, integer))
        );
        while(queue.size() > 1){
            queue.add(new Node(queue.poll(), queue.poll()));
        }
        getCodes(root = queue.poll(), "");
        return getText();
    }

    /**
     * Creates a map of all individual characters in a string with their respective
     * binary equivalents determined by the encode method
     * @param node
     * @param code
     */
    private void getCodes(Node node, String code){
        if(node instanceof CharNode){
            codes.put(((CharNode)node).character, code);
            return;
        }
        getCodes(node.leftNode, code.concat("0"));
        getCodes(node.rightNode, code.concat("1"));
    }

    /**
     * Returns the bitset representation of the text
     * @return
     */
    private BitSet getText(){
        StringBuilder string = new StringBuilder();
        for(char character : message.toCharArray()){
            string.append(codes.get(character));
        }

        BitSet outputBitSet = new BitSet(string.toString().length());
        int bitcounter = 0;
        for(Character c : string.toString().toCharArray()){
            if(c.equals('1')){
                outputBitSet.set(bitcounter);
            }
            bitcounter++;
        }

        return outputBitSet;
    }

    /**
     * Decodes the binary string using the map HashTree created beforehand
     * @param encodedMessage
     * @return
     */
    public String decode(String encodedMessage){
        StringBuilder string = new StringBuilder();
        Node current = root;

        for(char character : encodedMessage.toCharArray()){
            current = character == '0' ? current.leftNode : current.rightNode;
            if(current instanceof CharNode){
                string.append(((CharNode)current).character);
                current = root;
            }
        }

        return string.toString();
    }

    /**
     * Outputs all the individual characters in the text and their respective
     * binary values
     */
    public String codeString(){
        StringBuilder str = new StringBuilder("[");
        codes.forEach((character, code) ->
                //System.out.println(character + ": " + code));
                str.append(character + ":" + code + ", \n"));
        str.append("]");
        System.out.println(str.toString());

        return str.toString();
    }
}

/**
 * Implementation of the Node class that includes an integer to represent
 * the frequencies
 */
class Node implements Comparable<Node>{
    /**
     * Instance variables
     */
    public int frequency;
    public Node leftNode;
    public Node rightNode;

    /**
     * Constructor mainly used by the CharNode Class
     * @param frequency
     */
    public Node(int frequency){
        this.frequency = frequency;
    }

    /**
     * Constructor that gets its frequency value form the sum
     * of its children's values
     * @param leftNode
     * @param rightNode
     */
    public Node(Node leftNode, Node rightNode){
        this.frequency = leftNode.frequency + rightNode.frequency;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    @Override
    public int compareTo(Node other){
        return Integer.compare(this.frequency, other.frequency);
    }

}

/**
 * Class that represents the leaf nodes of the tree that contain the characters
 */
class CharNode extends Node{
    /**
     * Instance variable
     */
    public char character;

    /**
     * Similar to the node Constructor, but includes the char along with its
     * frequency
     * @param character
     * @param frequency
     */
    public CharNode(char character, int frequency){
        super(frequency);
        this.character = character;
    }
}
