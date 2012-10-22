import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
public class LCS {
    private static Charset ENCODING = StandardCharsets.UTF_8;
    public static double LCSAlgorithm(ArrayList<Integer> seq1,
            ArrayList<Integer> seq2) {
        int m = seq1.size();
        int n = seq2.size();
        // opt[i][j] = length of LCS of x[i...m] and y[j..n]
        int[][] opt = new int[m+1][n+1];

        //compute the length of LCS and all subproblems via dynamic programming
        for(int i=m-1 ; i >= 0; i--) {
            for(int j=n-1; j >=0; j--) {
                if(seq1.get(i) == seq2.get(j) ) {
                    opt[i][j] = opt[i+1][j+1] + 1;
                } else {
                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
                }
            }
        }
        //only need then length
        return ((double)opt[0][0])/(double)(seq1.size()+seq2.size());
    }

    public static void main(String args[]) {
        //IO
        List<String> lines = null;
        ArrayList<Sequence> Database = new ArrayList<Sequence>();
        try {
            Path path = Paths.get(args[0]);
            lines = Files.readAllLines(path,ENCODING);
        } catch (IOException e) {
        }

        //Data processing
        Iterator<String> iter = lines.iterator();
        while(iter.hasNext()){
            String[] slices = iter.next().split(" ");
            Sequence seq = new Sequence();
            for(int i=0; i< slices.length; i++){
                seq.Seq.add(Integer.parseInt(slices[i]));
            }
            Database.add(seq);
        }

        //Run Algorithm
        double LCSSum = 0;
        for(int i = 0; i<Database.size(); i++) {
            for(int j=i+1; j<Database.size(); j++) {
                LCSSum+=LCSAlgorithm(Database.get(i).Seq, Database.get(j).Seq);
            }
        }
        System.out.println(""+LCSSum/Database.size()/(Database.size()-1)*2);
    }

}

class Sequence {
    public ArrayList<Integer> Seq = new ArrayList<Integer>();
}
