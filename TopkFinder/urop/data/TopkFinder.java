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
import java.util.Collections;
import java.util.Comparator;
/**
 * File format: first item is frequency and the remaining are seqs
 */

public class TopkFinder {
    private static Charset ENCODING = StandardCharsets.UTF_8;
    private String mFilename;
    private ArrayList<Sequence> Data = new ArrayList<Sequence>();

    private int mK;
    public static void main(String args[]) {
        TopkFinder sampler = new TopkFinder();
        sampler.parseParameter(args);
        sampler.readData();
        sampler.pickTopk();
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

    private void readData() {
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
        while(iter.hasNext()) {
            String[] slices = iter.next().split(" ");
            Sequence tmp_seq = new Sequence();
            tmp_seq.frequency = Integer.parseInt(slices[0]);
            for(int i=1; i < slices.length; i++) {
                tmp_seq.Seq.add(Integer.parseInt(slices[i]));
            }
            Data.add(tmp_seq);
        }
    }
    private void pickTopk() {
        Collections.sort(Data, new Compare());
        for(int i=1; i<= mK; i++) {
            printLine(Data.get(i).Seq);
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

class Compare implements Comparator<Sequence>{
    @Override
    public int compare(Sequence x, Sequence y) {
        return y.frequency - x.frequency;
    }
}

class Sequence{
    public Sequence() {
        Seq = new ArrayList<Integer>();
    }
    public ArrayList<Integer> Seq;
    int frequency;
}
