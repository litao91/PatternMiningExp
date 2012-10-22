package urop.data;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.*;
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
import java.util.Random;
/**
 * File format: first item is frequency and the remaining are seqs
 */

public class MaxKAVGLCS {
    private static Charset ENCODING = StandardCharsets.UTF_8;
    private String mFilename;
    private int mK;
    ArrayList<Sequence> mMaxPatterns = new ArrayList<Sequence>();
    public static void main(String args[]) {
        MaxKAVGLCS sampler = new MaxKAVGLCS();
        sampler.parseParameter(args);
        sampler.findMax();
        sampler.doLCS();

    }

    private void parseParameter(String args[]){
        if(args.length == 0 ) {
            System.out.printf("More arguments needed\n");
        }

        for(int argnum = 0; argnum < args.length; argnum++) {
            if(args[argnum].charAt(0) == '-') {
                if(args[argnum].equals("-k")) {
                    mK = Integer.parseInt(args[++argnum]);
                } else if(args[argnum].equals("-f")) {
                    mFilename = args[++argnum];
                }

            }
        }
    }

    private void findMax() {
        List<String> lines = new ArrayList<String>();
        try {
            FileInputStream fstream = new FileInputStream(mFilename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String tmpLine;
            while((tmpLine =br.readLine()) !=null) {
                lines.add(tmpLine);

            }
        } catch (IOException e) {

        }

        Iterator<String> iter = lines.iterator();
        String[] prevSlice = null;
        int prevLength=0;
        while(iter.hasNext()) {
            String[] slices = iter.next().split(" ");
            if(prevSlice!=null&&slices.length < prevLength) {
                Sequence tmp_seq = new Sequence();
                for(int i=1; i < prevSlice.length; i++) {
                    tmp_seq.Seq.add(Integer.parseInt(prevSlice[i]));
                }
                mMaxPatterns.add(tmp_seq);
            }
            prevSlice = slices;
            prevLength = prevSlice.length;
        }
    }

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
    private void doLCS() {
        double LCSSum = 0;
        for(int i=0; i< mK; i++) {
            for(int j=i+1; j< mK; j++) {
                LCSSum+=LCSAlgorithm(mMaxPatterns.get(i).Seq, mMaxPatterns.get(j).Seq);
            }
        }
        System.out.println(""+LCSSum/mK/(mK-1)*2);
    }
    private void printRan() {
        Iterator<Sequence> iter = mRanPattern.iterator();
        while(iter.hasNext()) {
            printLine(iter.next().Seq);
        }

    }
    private void printLine(ArrayList<Integer> l) {
        Iterator<Integer> iter = l.iterator();
        while(iter.hasNext()) {
            System.out.printf("%d ",iter.next());
        }
        System.out.printf("\n");
    }
}

class Sequence {
    public Sequence() {
        Seq = new ArrayList<Integer>();
    }
    public ArrayList<Integer> Seq;
}
