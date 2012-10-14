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
    private ArrayList<Trajectory> trajecotries = new ArrayList<Trajectory>();

    private ArrayList<Sequence> seqs = new ArrayList<Sequence>();

    int gridNum = 10;

    float maxLAT = -9999999;
    float minLAT = 9999999;
    float maxLON = -9999999;
    float minLON = 9999999;


    public static void main(String args[]) {
        DataProcessor processor = new DataProcessor();
        List<String> lines = new ArrayList<String>();
        try {
            lines = processor.toLines(args[0]);
        }catch (IOException e) { }
        processor.processLines(lines,1);
        try {
            lines = processor.toLines(args[1]);
        }catch (IOException e) { }
        processor.processLines(lines,2);

        processor.calDataBound();
        processor.generateSeqs();
        processor.averageLength();
        //processor.printSeqs();
    }

    void averageLength() {
        int count = seqs.size();
        int sum = 0;
        Iterator<Sequence> iter = seqs.iterator();
        while(iter.hasNext()) {
            sum += iter.next().seqs.size();
        }
        System.out.printf("Avg. Length: %f\n", ((float)sum)/count);
    }

    void discreteSingleTra(Trajectory tra) {
        Sequence the_seq = new Sequence();
        the_seq.label = tra.label;
        Iterator<Position> iter = tra.poses.iterator();
        float sum_lat = 0;
        float sum_lon = 0;
        int count = 0;
        while(iter.hasNext()) {
            if(count == 5) {
                int num = getEntryNum(new Position(sum_lon/count, sum_lat/count), gridNum);
                the_seq.seqs.add(num);
                count = 0;
                sum_lat = 0;
                sum_lon = 0;
            }
            count++;
            Position pos = iter.next();
            sum_lat+=pos.LAT;
            sum_lon+=pos.LON;
        }
        seqs.add(the_seq);
    }

    void generateSeqs() {
        Iterator<Trajectory> iter = trajecotries.iterator();
        while(iter.hasNext()) {
            discreteSingleTra(iter.next());
        }
    }

    public int getEntryNum(Position en, int gridNum) {
        float gridSizeLAT  = (maxLAT - minLAT)/gridNum;
        float gridSizeLON = (maxLON - minLON)/gridNum;
        int lat_num = (int)(((en.LAT - minLAT)/gridSizeLAT) + 1);
        int lon_num = (int)(((en.LON - minLON)/gridSizeLON) + 1);
        return (lat_num-1)*gridNum + lon_num;
    }

    List<String> toLines(String fileName) throws IOException{
        Path path = Paths.get(fileName);
        return Files.readAllLines(path, ENCODING);
    }

    void processLines(List<String> lines, int label) {
        Iterator<String> iter = lines.iterator();
        Trajectory curTra = new Trajectory();
        String curTraId = "-1";
        while(iter.hasNext()) {
            String ln = iter.next();
            String[] slices = ln.split(";");
            String tra_id = slices[0] + slices[1] + slices[2];
            float LAT = Float.parseFloat(slices[6]);
            float LON = Float.parseFloat(slices[7]);
            if(curTraId.equals("-1")) {
                curTraId= tra_id;
                curTra =  new Trajectory();
                curTra.poses = new ArrayList<Position>();
                curTra.label = label;
            }
            if(!curTraId.equals(tra_id)) {
                trajecotries.add(curTra);
                curTraId = tra_id;
                curTra = new Trajectory();
                curTra.label = label;
                curTra.poses = new ArrayList<Position>();
            }
            curTra.poses.add(new Position(LON, LAT));
        }
    }

    void calDataBound() {
        Iterator<Trajectory> iter = trajecotries.iterator();
        while(iter.hasNext()) {
            Trajectory tra = iter.next();
            checkSeqBound(tra.poses);
        }
    }

    void checkSeqBound(List<Position> poses) {
        Iterator<Position> iter = poses.iterator();
        while(iter.hasNext()){
           Position en = iter.next();
           maxLAT = en.LAT > maxLAT?en.LAT:maxLAT;
           minLAT = en.LAT < minLAT?en.LAT:minLAT;
           maxLON = en.LON > maxLON? en.LON:maxLON;
           minLON = en.LON < minLON?en.LON: minLON;
        }
    }
    void printTras() {
        Iterator<Trajectory> iter = trajecotries.iterator();
        while(iter.hasNext()) {
            Trajectory tra = iter.next();
            System.out.printf("ID:%d ", tra.label);
            printTra(tra.poses);
            System.out.printf("\n");
        }
    }


    void printTra(List<Position> poses) {
        Iterator<Position> iter = poses.iterator();
        while(iter.hasNext()){
            System.out.printf(iter.next().toString());
        }
    }
    void printSeqs() {
        Iterator<Sequence> iter = seqs.iterator();
        while(iter.hasNext()) {
            Sequence seq = iter.next();
         //   System.out.printf("%d ", seq.label);
            printSeq(seq.seqs);
            System.out.printf("\n");
        }

    }

    void printSeq(List<Integer> poses) {
        Iterator<Integer> iter = poses.iterator();
        while(iter.hasNext()){
            System.out.printf("%d ", iter.next());
        }
    }


}

class Position {
    public Position(float lon, float lat) {
        LON = lon;
        LAT = lat;
    }
    public String toString() {
        return "("+LON+","+LAT+")";
    }

    public float LON;
    public float LAT;
}

class Trajectory {
    List<Position> poses;
    int label;
}

class Sequence {
    public Sequence() {
        seqs = new ArrayList<Integer>();
    }
    List<Integer> seqs;
    int label;
}


