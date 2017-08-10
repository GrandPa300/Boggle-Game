import java.util.LinkedList;

public class Trie
{
    private Node root; // root of trie
    private int n;     // number of keys in trie
    private LinkedList<Node> nodeToReset;

    // 26-way trie node
    private static class Node 
    {
        private int val = 0;
        private Node[] next = new Node[26];
    }

    public Trie() 
    {
        nodeToReset = new LinkedList<>();
    }

    public int get(String key) 
    {
        Node x = get(root, key, 0);
        if (x == null) return -1;
        // for BoggleSolver ver 0.6
        if (x.val == 1)
        {
            x.val = 2;
            nodeToReset.add(x);
            return 1;
        }
        // end here
        else return x.val;
    }
    
    private Node get(Node x, String key, int d) 
    {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d) - 'A';
        return get(x.next[c], key, d+1);
    }
    
    public boolean contains(String key) {return get(key) == 1;}
    
    public boolean isPrefix(String key) {return get(key) >= 0;}
    
    public void resetNodes()
    {
        while (nodeToReset.size() > 0)
            nodeToReset.remove().val = 1;
    }

    public void put(String key) {root = put(root, key, 0);}

    private Node put(Node x, String key, int d) 
    {
        if (x == null) x = new Node();
        if (d == key.length()) 
        {
            x.val = 1;
            return x;
        }
        int c = key.charAt(d) - 'A';
        x.next[c] = put(x.next[c], key, d+1);
        return x;
    }
}