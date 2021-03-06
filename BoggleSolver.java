import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
 /**
  * Use 26-way tries. 
  * based on ver 0.7, Using Node for DFS search: 
  *     for every recursive call, instead of starting all over again from root
  *     new version of DFS continues searching from current node.
  *     Search ends till a null node is hit.
  *     
  * Note: Need to check if node is NULL or MARKED first in DFS
  *       since node with Q need to go one step further to node U
  *       if Node Q itself is already NULL, node.next[] will throw a NullPointerException
  *       after Node Q is update to Node U, a second NULL check is a must.
  * Average run-time of 10000 random board is 1.05 (s).
  * @sean
  * @1.0
  */
public class BoggleSolver
{
    private Node root;
    private BoggleBoard board;
    private boolean[][] marked;
    private int col, row;
    
    private LinkedList<String> result;
    private LinkedList<Node> nodeToReset;
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        root = new Node();
        for (int i = 0; i < dictionary.length; i++)
            put(dictionary[i]);   
    }
    
    private static class Node
    {
        private int val = 0;
        private Node[] next = new Node[26];
    }
    
    private int get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return 0;
        return x.val;
    }
    
    private Node get(Node x, String key, int d) 
    {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d) - 'A';
        return get(x.next[c], key, d+1);
    }
    
    public void put(String key) 
    {root = put(root, key, 0);}

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

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        this.board = board;
        this.row = board.rows();
        this.col = board.cols();
        
        marked = new boolean[row][col];
        result = new LinkedList<>();
        nodeToReset = new LinkedList<>();
        
        Node node;
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) 
            {
                node = root.next[board.getLetter(i, j) - 'A'];
   
                    dfs(node, i, j, "");
            }
                 
        for (Node n : nodeToReset) n.val = 1;
           
        return result;
    }
    
    private void dfs(Node node, int r, int c, String curWord)
    {
        if (node == null || marked[r][c]) return; // check if next node in tries is null
        
        char curChar = board.getLetter(r, c);
        
        if (curChar == 'Q') node = node.next['U' - 'A'];
        if (node == null) return; // second check since node of 'Q' is moved onto node 'U'
            
        if (curChar == 'Q') curWord += "QU";
        else curWord += curChar;
        
        
        marked[r][c] = true;
        
        if (node.val == 1)
        {
            nodeToReset.add(node);
            node.val = 2;
            if (curWord.length() > 2) result.add(curWord);
        }
        
        if (r - 1 >= 0 && c - 1 >= 0)
            dfs(node.next[board.getLetter(r-1, c-1) - 'A'], r - 1, c - 1, curWord);
        if (r - 1 >= 0)
            dfs(node.next[board.getLetter(r-1, c) - 'A'], r - 1, c, curWord);
        if (r - 1 >= 0 && c + 1 < col)
            dfs(node.next[board.getLetter(r-1, c+1) - 'A'], r - 1, c + 1, curWord);
        if (c - 1 >= 0)
            dfs(node.next[board.getLetter(r, c-1) - 'A'], r, c - 1, curWord);
        if (c + 1 < col)
            dfs(node.next[board.getLetter(r, c+1) - 'A'], r, c + 1, curWord);
        if (r + 1 < row && c - 1 >= 0)
            dfs(node.next[board.getLetter(r+1, c-1) - 'A'], r + 1, c - 1, curWord);
        if (r + 1 < row)
            dfs(node.next[board.getLetter(r+1, c) - 'A'], r + 1, c, curWord);
        if (r + 1 < row && c + 1 < col)
            dfs(node.next[board.getLetter(r+1, c+1) - 'A'], r + 1, c + 1, curWord);
        
        marked[r][c] = false;
        return;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
       if (get(root, word, 0).val < 1) return 0;
       int len = word.length();
       if (len < 3) return 0;
       else if (len < 4) return 1; 
       else if (len < 7) return len - 3;
       else if (len < 8) return 5;
       else return 11;
    }
    
    /*
    public static void main(String[] args) 
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word + " " + solver.scoreOf(word));
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
    */
    /*
    public static void main(String[] args) 
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        
        In inBoard = new In("boardList.txt");
        String[] boardList =  inBoard.readAllStrings();
        for (int i = 0; i < boardList.length; i++)
        {
            BoggleBoard board = new BoggleBoard(boardList[i]);
            int score = 0;
            Iterable<String> words = solver.getAllValidWords(board);
            //for (String word : solver.getAllValidWords(board))
            for (String word : words)
                score += solver.scoreOf(word);
            StdOut.println(boardList[i].substring(18) + " Score = " + score);
        }
    }
   */
   /*
    public static void main(String[] args) 
    {
        int iteration = 0;
        double totalTime = 0;
        while (iteration < 10)
        {
            Stopwatch timer = new Stopwatch();
            In in = new In(args[0]);
            String[] dictionary = in.readAllStrings();
            BoggleSolver solver = new BoggleSolver(dictionary);
            int count = 0;
            while (count < 10000)
            {
                BoggleBoard board = new BoggleBoard();
                solver.getAllValidWords(board);
                count++;
            }
            double time = timer.elapsedTime();
            StdOut.println("Iteration "+ iteration + " elapsed " + time);
            totalTime += time;
            iteration++;
        }
        double avg = Math.floor(totalTime * 100) / 1000;
        StdOut.println("Average run-time for 10000 board is :" + avg);
    }
   */
   
   public static void main(String[] args) 
    {
        Stopwatch timer = new Stopwatch();
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        
        BoggleSolver solver = new BoggleSolver(dictionary);
        int count = 0;
        while (count < 30000)
        {
            BoggleBoard board = new BoggleBoard();
            solver.getAllValidWords(board);
            count++;
        }
        double time = timer.elapsedTime();
        double solPerSec = Math.floor(30000 / time * 100) / 100; 
        double ratio = Math.floor(6175.83 / solPerSec * 100) / 100;
        StdOut.println("Total Time for 30000 random board is " + timer.elapsedTime());
        StdOut.println("reference solution per second is 6175.83");
        StdOut.println("student solution per second is " + solPerSec);
        StdOut.println("reference/student ratio is " + ratio);  
    }
    
}
