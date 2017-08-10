import java.util.HashSet;
import java.util.ArrayList;
import java.lang.StringBuilder;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
 /**
  * Use 26-way tries.
  * Used ArrayList instead of HashSet to store adjacent letters.
  * Average run-time of 10000 random board is 2.963 (s).
  * @sean
  * @0.1
  */
public class BoggleSolver
{
    private Trie dict;
    private boolean[][] marked;
    private char[][] letters;
    private int col, row, size;
    
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        dict = new Trie();
        for (int i = 0; i < dictionary.length; i++)
            dict.put(dictionary[i]);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        row = board.rows();
        col = board.cols();
        size = row * col;
        
        marked = new boolean[row][col];
        letters = new char[row][col];

        HashSet<String> result = new HashSet<>();
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                letters[i][j] = board.getLetter(i, j);
        
        
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
            {
                StringBuilder curWord = new StringBuilder();
                dfs(i, j, curWord, result);
            }
        return result;
    }
    
    private void dfs(int r, int c, StringBuilder curWord, HashSet<String> list)
    {
        if (r < 0 || r > row - 1 || c < 0 || c > col - 1 || marked[r][c]) return;
        
        char curChar = letters[r][c];
        curWord.append(curChar);
        if (curChar == 'Q') curWord.append('U');
        
        String current = curWord.toString();
        if (!dict.isPrefix(current))
        {
            curWord.deleteCharAt(current.length() - 1);
            if (curChar == 'Q') 
                curWord.deleteCharAt(current.length() - 2);
            return;
        }
        
        marked[r][c] = true;
        if (dict.contains(current) && current.length() > 2) 
            list.add(current);
        
        for (int[] next : findAdj(r, c)) 
            dfs(next[0], next[1], curWord, list);
        
        marked[r][c] = false;
        curWord.deleteCharAt(current.length() - 1);
        if (curChar == 'Q') 
            curWord.deleteCharAt(current.length() - 2);
    }
    
    private ArrayList<int[]> findAdj(int row, int col)
    {
        ArrayList<int[]> adj = new ArrayList<>();
        adj.add(new int[] {row - 1, col - 1});
        adj.add(new int[] {row - 1, col});
        adj.add(new int[] {row - 1, col + 1});
        adj.add(new int[] {row, col - 1});
        adj.add(new int[] {row, col + 1});
        adj.add(new int[] {row + 1, col - 1});
        adj.add(new int[] {row + 1, col});
        adj.add(new int[] {row + 1, col + 1});
        return adj;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
       if (!dict.contains(word)) return 0;
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
    }*/
    
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
            for (String word : solver.getAllValidWords(board))
                score += solver.scoreOf(word);
            StdOut.println(boardList[i].substring(18) + " Score = " + score);
        }
        
    }*/
    
   
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
}
