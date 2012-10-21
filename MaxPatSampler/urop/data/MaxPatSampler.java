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

public class MaxPatSampler {
    private static Charset ENCODING = StandardCharsets.UTF_8;
    private String mFilename;
    private int mK;
    ArrayList<Sequence> mMaxPatterns = new ArrayList<Sequence>();
    ArrayList<Sequence> mRanPattern = new ArrayList<Sequence>();
    public static void main(String args[]) {
        MaxPatSampler sampler = new MaxPatSampler();
        sampler.parseParameter(args);
        sampler.findMax();
        sampler.doSample();
        sampler.printRan();

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
    private void doSample() {
        Random ranGen = new Random();
        for(int i=0; i< mK; i++) {
            int ranBase = mMaxPatterns.size();
            int ranIdx = ranGen.nextInt(ranBase-1);
            mRanPattern.add(mMaxPatterns.get(ranIdx));
            mMaxPatterns.remove(ranIdx);
        }
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
