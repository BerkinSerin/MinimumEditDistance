import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.util.Comparator.comparing;

public class MedGui extends JFrame {


    private JPanel Panel;
    private JLabel label1;
    private JLabel label2;
    private JTextField textField1;
    private JButton findButton;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel word1;
    private JLabel word2;
    private JLabel word3;
    private JLabel word4;
    private JLabel word5;
    private JLabel ed1;
    private JLabel ed2;
    private JLabel ed3;
    private JLabel ed4;
    private JLabel ed5;
    private JLabel runtimeLabel;
    private JTextField medWord1;
    private JTextField medWord2;
    private JButton findButton1;
    private JTextArea medOperationsText;
    private JLabel runtimeMEDlabel;
    private JTextArea tableText;
    private JTextPane textPane1;

    public MedGui(){
        add(Panel);
        setSize(1000,600);
        setTitle("MED Assignment 2016510059");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long startTime = System.nanoTime();
                String word;
                word = textField1.getText();
                Charset iso88599charset = Charset.forName("ISO-8859-9");
                try {
                    String s = Files.readString(Path.of("sozluk.txt"), iso88599charset);
                    String dictionary[] = s.split("\r\n");
                    HashMap<String, Integer> map = new HashMap<>();
                    for (String item: dictionary) {
                        map.put(item,editDistDP(word,item,word.length(),item.length())[word.length()][item.length()]);
                    }

                    Object[] a = map.entrySet().toArray();
                    Arrays.sort(a, new Comparator() {
                        public int compare(Object o1, Object o2) {
                            return ((Map.Entry<String, Integer>) o1).getValue()
                                    .compareTo(((Map.Entry<String, Integer>) o2).getValue());
                        }
                    });

                    word1.setText(((Map.Entry<String, Integer>) a[0]).getKey());
                    ed1.setText(String.valueOf(((Map.Entry<String, Integer>) a[0]).getValue()));
                    word2.setText(((Map.Entry<String, Integer>) a[1]).getKey());
                    ed2.setText(String.valueOf(((Map.Entry<String, Integer>) a[1]).getValue()));
                    word3.setText(((Map.Entry<String, Integer>) a[2]).getKey());
                    ed3.setText(String.valueOf(((Map.Entry<String, Integer>) a[2]).getValue()));
                    word4.setText(((Map.Entry<String, Integer>) a[3]).getKey());
                    ed4.setText(String.valueOf(((Map.Entry<String, Integer>) a[3]).getValue()));
                    word5.setText(((Map.Entry<String, Integer>) a[4]).getKey());
                    ed5.setText(String.valueOf(((Map.Entry<String, Integer>) a[4]).getValue()));
                    long endTime   = System.nanoTime();
                    long totalTime = (endTime - startTime) / 1000000;
                    runtimeLabel.setText("Runtime: " + totalTime + " ms");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
        findButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                medOperationsText.setText("");
                long startTime = System.nanoTime();
                String word1, word2, table = "";
                word1 = medWord1.getText();
                word2 = medWord2.getText();
                int[][] dp = editDistDP(word1,word2,word1.length(),word2.length());
                String [][] temp = new String[word1.length() +2][word2.length() +2];
                temp[0][0] = "";
                temp[1][0] = "#";
                temp[0][1] = "#";

                for (int i = 0; i < word1.length(); i++) {
                    temp[i+2][0] = String.valueOf(word1.charAt(i));
                }
                for (int i = 0; i < word2.length(); i++) {
                    temp[0][i+2] = String.valueOf(word2.charAt(i));
                }

                for (int i = 0; i < dp.length; i++) {
                    for (int j = 0; j < dp[i].length; j++) {
                        temp[i+1][j+1] = String.valueOf(dp[i][j]);
                    }
                }

                int k = word1.length()+1;
                int l = word2.length()+1;
                int insert = 0;
                int remove = 0;
                int replace = 0;

                while(!(k == 1 && l == 1)){
                    if(l == 1){
                        temp[k - 1][l] = temp[k - 1][l] + String.valueOf("\u2193");
                        if(temp[k][l].substring(0,temp[k][l].length() - 1) != temp[k - 1][l].substring(0,temp[k - 1][l].length() - 1)){
                            remove++;
                        }
                        k = k-1;
                        continue;
                    }
                    int val = min(Integer.parseInt(temp[k][l - 1]), // Insert
                            Integer.parseInt(temp[k - 1][l]), // Remove
                            Integer.parseInt(temp[k - 1][l - 1])); // Replace
                    if (val == Integer.parseInt(temp[k - 1 ][l - 1])){
                        String a,b;
                        temp[k - 1][l - 1] = temp[k - 1][l - 1] +String.valueOf("\u2198");
                        if (temp[k][l].length() == 1){
                            a = temp[k][l];
                        }else{
                            a = temp[k][l].substring(0,temp[k][l].length() - 1);
                        }
                        b = temp[k - 1][l - 1].substring(0,temp[k - 1][l - 1].length() - 1);

                        if(!(a.equals(b))){
                            System.out.println(a.equals(b));
                            System.out.println(a + " " + b);
                            replace++;
                        }
                        k = k - 1;
                        l = l - 1;
                    }else if(val == Integer.parseInt(temp[k - 1][l])){
                        temp[k - 1][l] = temp[k - 1][l] + String.valueOf("\u2193");
                        String a = temp[k][l].substring(0,temp[k][l].length() - 1);
                        String b = temp[k - 1][l].substring(0,temp[k - 1][l].length() - 1);
                        if( !(a.equals(b)) ){
                            remove++;
                        }
                        k = k-1;
                    }else{
                        temp[k][l - 1] = temp[k][l - 1] + String.valueOf("\u2192");
                        String a = temp[k][l].substring(0,temp[k][l].length() - 1);
                        String b = temp[k][l - 1].substring(0,temp[k][l - 1].length() - 1);
                        if( !(a.equals(b))){
                            insert++;
                        }
                        l = l-1;
                    }
                }

                for (int i = 0; i < temp.length; i++) {
                    for (int j = 0; j < temp[i].length; j++) {
                        table = table + temp[i][j] + "\t";

                    }
                    table = table + "\n";
                }

                tableText.setText(table);
                long endTime   = System.nanoTime();
                long totalTime = (endTime - startTime) / 1000000;
                runtimeMEDlabel.setText("Runtime: " + totalTime + " ms");
                medOperationsText.setText(medOperationsText.getText() + insert +
                        " Insert Operations \n" + remove + " Remove Operations \n" + replace + " Replace Operations \n" + "\n");
                medOperationsText.setText(medOperationsText.getText() + "Minimum Edit Distance: " + temp[word1.length()+1][word2.length()+1]+ "\n");

            }
        });
    }
    static int min(int x, int y, int z)
    {
        if (x <= y && x <= z)
            return x;
        if (y <= x && y <= z)
            return y;
        else
            return z;
    }

    static int[][] editDistDP(String str1, String str2, int m,
                              int n)
    {
        // Create a table to store results of subproblems
        int[][] dp = new int[m + 1][n + 1];

        // Fill d[][] in bottom up manner
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                // If first string is empty, only option is
                // to insert all characters of second string
                if (i == 0)
                    dp[i][j] = j; // Min. operations = j

                    // If second string is empty, only option is
                    // to remove all characters of second string
                else if (j == 0)
                    dp[i][j] = i; // Min. operations = i

                    // If last characters are same, ignore last
                    // char and recur for remaining string
                else if (str1.charAt(i - 1)
                        == str2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];

                    // If the last character is different,
                    // consider all possibilities and find the
                    // minimum
                else
                    dp[i][j] = 1
                            + min(dp[i][j - 1], // Insert
                            dp[i - 1][j], // Remove
                            dp[i - 1]
                                    [j - 1]); // Replace
            }
        }

        return dp;
    }

    
}
