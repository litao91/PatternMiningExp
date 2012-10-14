package urop.data;
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

public class DataProcessor {
    private static Charset ENCODING = StandardCharsets.UTF_8;
    private static int gridSize = 15;
    private ArrayList<Entry> raw_db = new ArrayList<Entry>();
    private ArrayList<Trajectory> Database = new ArrayList<Trajectory>();
    private String pathPrefix;
    private float maxLAT;
    private float minLAT;
    private float maxLON;
    private float minLON;

    public static void main(String args[]) {
        DataProcessor processor = new DataProcessor();
        processor.pathPrefix = args[0];
        processor.processFiles(new File(args[0]));
        processor.findDataBound();
        processor.generateTrajectory();
        //processor.printSeqOnly();
        processor.avgLength();
    }

    void printSeqOnly() {
        Iterator<Trajectory> it = Database.iterator();
        while(it.hasNext()) {
            Trajectory tra = it.next();
        //    System.out.printf("%d ", tra.label);
            printIter(tra.seq.iterator(), " ");
            System.out.printf("\n");
        }
    }

    void avgLength() {
        int count = Database.size();
        Iterator<Trajectory> iter = Database.iterator();
        int sum = 0;
        while(iter.hasNext()) {
            sum+= iter.next().seq.size();
        }
        System.out.printf("Avg. Length: %f\n", ((float)sum)/count);
    }


    void printSeqWithLabel() {
        Iterator<Trajectory> it = Database.iterator();
        while(it.hasNext()) {
            Trajectory tra = it.next();
            System.out.printf("%d ", tra.label);
            printIter(tra.seq.iterator(), " ");
            System.out.printf("\n");
        }
    }

    public int getEntryNum(Entry en, int gridNum) {
        float gridSizeLAT  = (maxLAT - minLAT)/gridNum;
        float gridSizeLON = (maxLON - minLON)/gridNum;
        int lat_num = (int)(((en.LAT - minLAT)/gridSizeLAT) + 1);
        int lon_num = (int)(((en.LON - minLON)/gridSizeLON) + 1);
        return (lat_num-1)*gridNum + lon_num;
    }

    public static int getLabel(String labelName) {
        if(labelName.matches(" *?TROPICAL STORM *?")) {
            return 1;
        }else if(labelName.matches(".*?HURRICANE.*?")) {
            return 2;
        }else if(labelName.matches(".*?TROPICAL DEPRESSION.*?")) {
            return 3;
        }else if(labelName.matches(".*?EXTRATROPICAL STORM.*?")) {
            return 4;
        }else if(labelName.matches(".*?EXTRATROPICAL DEPRESSION.*?")) {
            return 5;
        }else if(labelName.matches(".*?SUBTROPICAL DEPRESSION.*?")) {
            return 6;
        }else if(labelName.matches(".*?TROPICAL WAVE.*?")) {
            return 7;
        }else if(labelName.matches(".*?SUBTROPICAL STORM.*?")) {
            return 8;
        } else {
            return 0;
        }

    }

    /**
     * Scan the files, convert lines into Entry object
     */
    public void processFiles(File dir) {
        if(dir.isDirectory()) {
            String[] children = dir.list();
            List<String> lines;
            for(int i=0; i< children.length; i++ ) {
                try {
                    lines = toLines(children[i]);
                    Iterator<String> j = lines.iterator();
                    String ln = j.next();
                    for(;j.hasNext();ln = j.next() ) {
                        raw_db.add(processALine(ln));
                    }
                } catch (IOException e) {
                }
            }
        }
    }

    void generateTrajectory() {
        Trajectory currentTrajectory  = new Trajectory();
        int currentLabel = -1;
        Iterator<Entry> en_iter = raw_db.iterator();
        while(en_iter.hasNext()) {
            Entry en = en_iter.next();
            if(en.label!=currentLabel) {
                if(currentLabel!=-1) {
                    Database.add(currentTrajectory);
                }
                currentLabel = en.label;
                currentTrajectory = new Trajectory();
                currentTrajectory.label = currentLabel;
            }
            currentTrajectory.seq.add(getEntryNum(en,gridSize));
        }
    }

    List<String> toLines(String fileName) throws IOException{
        Path path = Paths.get(pathPrefix + fileName);
        List<String> lines = Files.readAllLines(path, ENCODING);
        //Delete three lines of data description
        lines.remove(0);
        lines.remove(0);
        lines.remove(0);
        return lines;
    }

    Entry processALine(String line){
        String[] slices = line.trim().split("\\s+");
        Entry result = new Entry();
        result.LAT = Float.parseFloat(slices[1]);
        result.LON = Float.parseFloat(slices[2]);
        if(slices.length == 8) {
            result.label_str = slices[6] + " " + slices[7];
        } else {
            result.label_str = slices[6];
        }
        result.label = getLabel(result.label_str);
        return result;
    }

    void printIter(Iterator<Integer> iter, String eliminator) {
        while(iter.hasNext()) {
            System.out.printf("%d"+eliminator, iter.next());
        }
    }

    void printEntry(Entry en) {
        System.out.printf("%d: %f, %f\n", en.label, en.LAT, en.LON);
    }
    void findDataBound() {
        Iterator<Entry> iter = raw_db.iterator();
        maxLAT = -1000000;
        minLAT = 1000000;
        maxLON = -1000000;
        minLON = 1000000;
        Entry en = iter.next();
        for(;iter.hasNext();en = iter.next()){
           maxLAT = en.LAT > maxLAT?en.LAT:maxLAT;
           minLAT = en.LAT < minLAT?en.LAT:minLAT;
           maxLON = en.LON > maxLON? en.LON:maxLON;
           minLON = en.LON < minLON?en.LON: minLON;
        }
        System.out.printf("maxLAT:%f, minLAT:%f, maxLON:%f, minLON:%f\n", maxLAT, minLAT, maxLON, minLON);
    }
}

class Entry {
    public String label_str;
    public int label;
    public float LAT;
    public float LON;
}

/**
 * Trajectory contains the label and the sequences;
 */
class Trajectory {
    public Trajectory() {
        seq = new ArrayList<Integer>();
    }
    public ArrayList<Integer> seq;
    public int label;
}
