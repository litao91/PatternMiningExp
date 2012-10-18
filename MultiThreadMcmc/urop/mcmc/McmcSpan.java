package urop.mcmc;
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
import java.util.Set;
import java.util.Random;
import java.util.HashSet;

public class McmcSpan {
    private static Charset ENCODING = StandardCharsets.UTF_8;

    private int mMinSup;
    private int mK;
    private String mFilename;
    private int mThreadNum;
    private int mItemNum;

    //All the seuqences read from the file is database
    private List<Sequence> mDatabase;
    private Set<Sequence> mFreqPattern = new HashSet<Sequence>();
    private Set<Sequence> mMaxPattern = new HashSet<Sequence>();

    public static void main(String args[]) {
        McmcSpan mcmcSpan = new McmcSpan();
        mcmcSpan.parseParameter(args);
        mcmcSpan.ReadnProcessData();
        mcmcSpan.startThreads();
        mcmcSpan.printMaxPattern();
    }

    public void printMaxPattern() {
        Iterator<Sequence> iter = mMaxPattern.iterator();
        while(iter.hasNext()) {
            printASeq(iter.next().getSeq());
        }
    }

    /**
     * Printing methods for debugging purpose
     */
    private void printParameter() {
        System.out.printf("minsup:%d, k:%d, fileame:%s\n",
                mMinSup, mK, mFilename );
    }

    private void printDb() {
        Iterator<Sequence> iter = mDatabase.iterator();
        while(iter.hasNext()) {
            printASeq(iter.next().getSeq());
        }
    }

    private void printASeq(List<Integer> seq) {
        Iterator<Integer> iter = seq.iterator();
        while(iter.hasNext()) {
            System.out.printf("%d ", iter.next());
        }
        System.out.printf("\n");
    }

    public void startThreads() {
        for(int i=0; i< mThreadNum; i++) {
            (new Thread(new AlgWorker())).start();
        }
    }

    public void parseParameter(String args[])  {
        if(args.length == 0 ) {
            System.out.printf("More arguments needed\n");
        }

        for(int argnum = 0; argnum < args.length; argnum++) {
            if(args[argnum].charAt(0) == '-') {
                if(args[argnum].equals("-min_sup")) {
                    mMinSup = Integer.parseInt(args[++argnum]);
                } else if(args[argnum].equals("-k")) {
                    mK = Integer.parseInt(args[++argnum]);
                } else if(args[argnum].equals("-f")) {
                    mFilename = args[++argnum];
                } else if(args[argnum].equals("-thread_num")) {
                    mThreadNum = Integer.parseInt(args[++argnum]);
                } else if(args[argnum].equals("-item_num")) {
                    mItemNum = Integer.parseInt(args[++argnum]);
                } else {
                    System.out.printf("Error: Wrong parameter type\n");
                }
            }
        }
    }
    public void ReadnProcessData() {
        Path path = Paths.get(mFilename);
        List<String> lines = new ArrayList<String>();
        try{
            lines = Files.readAllLines(path, ENCODING);
        }catch(IOException e) {

        }
        mDatabase = new ArrayList<Sequence>();
        Iterator<String> iter = lines.iterator();
        //Process each line of the input
        //Each line is a sequence, store it in a Sequence class;
        while(iter.hasNext()) {
            String[] slices = iter.next().split(" ");
            Sequence seq_tmp = new Sequence(new ArrayList<Integer>());
            for(int i = 0; i< slices.length; i++) {
                seq_tmp.getSeq().add(Integer.parseInt(slices[i]));
            }
            mDatabase.add(seq_tmp);
        }
    }

    class AlgWorker implements Runnable{
        @Override
        public void run() {
            //The current projected pattern
            ArrayList<Integer> curPattern = new ArrayList<Integer>();
            //Temporarily store the generated next pattern
            ArrayList<Integer> tmpPattern = new ArrayList<Integer>();
            //Frequent next state
            ArrayList<Sequence> surroundStates = new ArrayList<Sequence>();


            Random ranGen = new Random();

            while(mMaxPattern.size() <= mK) {
                surroundStates.clear();
                tmpPattern.clear();
                tmpPattern = new ArrayList<Integer>(curPattern);
                //Generate the next state
                for(int i=0; i < mItemNum; i++) {
                    tmpPattern.add(i);
                    if(!mFreqPattern.contains(tmpPattern)) {
                        //If the support of the next state exceed
                        //the min support, then it's a frequent pattern
                        int count = Utility.checkSupport(mDatabase, tmpPattern);
                        //If it's not frequent, we ignore this state;
                        if(count < mMinSup) {
                            tmpPattern.remove(tmpPattern.size()-1);
                            continue;
                        }
                    }
                    //If it's frequent, put it in the valid
                    //surround state
                    surroundStates.add(new Sequence(tmpPattern));
                    tmpPattern.remove(tmpPattern.size()-1);
                }
                if(surroundStates.size()==0) {
                    printASeq(curPattern);
                    synchronized(mMaxPattern) {
                        mMaxPattern.add(new Sequence(curPattern));
                    }
                    curPattern.clear();
                    continue;
                }
                int randNum = ranGen.nextInt(surroundStates.size());
                curPattern = new ArrayList<Integer>(surroundStates.get(randNum).getSeq());
                synchronized(mFreqPattern) {
                    mFreqPattern.add(new Sequence(curPattern));
                }
            }
        }
    }
}

class Sequence {
    public Sequence(ArrayList<Integer> seq) {
        setSeq(seq);
    }

    public void setSeq(ArrayList<Integer> seq) {
        Seq = new ArrayList<Integer>(seq);
    }
    List<Integer> getSeq() {
        return Seq;
    }

    List<Integer> Seq;
}

class Utility {
    public static boolean isSubseq(
            List<Integer> pattern,
            List<Integer> seq) {
        int seqIndex = 0;
        for(int i=0; i<pattern.size(); i++) {
            while(true) {
                if(seqIndex >= seq.size()) {
                    return false;
                }
                if(pattern.get(i) == seq.get(seqIndex++)) {
                    break;
                }
            }
        }
        return true;
    }

    public static int checkSupport(List<Sequence> db,
            List<Integer> pattern) {
        int supportCount = 0;
        Iterator<Sequence> db_it = db.iterator();
        while(db_it.hasNext()) {
            if(isSubseq(pattern, db_it.next().getSeq())) {
                supportCount++;
            }
        }
        return supportCount;
    }
}
