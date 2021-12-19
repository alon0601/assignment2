package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) throws IOException {
        String path = args[0];
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Thread> threads = new ArrayList<>();

        Gson gson = new Gson();
        HashMap<Object, Object> json = gson.fromJson(bufferedReader, HashMap.class);

        Double dTickTime = (Double) json.get("TickTime");
        Double dDuration = (Double) json.get("Duration");
        int duration = dDuration.intValue();
        int tickTime = dTickTime.intValue();


        //init timeService
        Thread threadTime = initTimeService(duration,tickTime);
        ArrayList<Student> realStudents = new ArrayList<>();
        ArrayList<ConfrenceInformation> realCon = new ArrayList<>();


        //init students
        ArrayList<Object> students = (ArrayList<Object>)json.get("Students");
        List<Thread> studentsT = initStudents(students,realStudents);


        //init gpus
        ArrayList<Object> gpus = (ArrayList<Object>) json.get("GPUS");
        List<Thread> GPUST= initGPUS(gpus);


        //init cpus
        ArrayList<Object>  cpus = (ArrayList<Object>) json.get("CPUS");
        List<Thread> cpusT = initCPUS(cpus);


        //init conferences
        ArrayList<Object> conferences = (ArrayList<Object>) json.get("Conferences");
        List<Thread> conferencesT = initConferences(conferences,realCon);

        int i = 0;
        Thread[] threads1 = new Thread[GPUST.size() + cpusT.size() + conferencesT.size() + studentsT.size() + 1];
        for(Thread t : GPUST) {
            t.start();
            threads1[i] = t;
            i++;
        }
        for(Thread t : cpusT){
            t.start();
            threads1[i] = t;
            i++;
        }
        for (Thread t: conferencesT){
            t.start();
            threads1[i] = t;
            i++;
        }
        for (Thread t : studentsT) {
            t.start();
            threads1[i] = t;
            i++;
        }
        threadTime.start();
        threads1[i] = threadTime;


        try {
            WaitForThreadsToFinish(threads1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Cluster c = Cluster.getInstance();
        System.out.println(" the cpus worked: " + c.getCpusTime());
        System.out.println(" the Gpus worked: " + c.getGpusTime());
        System.out.println("num of batches pro: " + c.getNumOfBach());
        System.out.println("---------------------------------------");
        //new shit
        Gson gson2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.json"));
        Information information = new Information(realStudents,realCon);
        String output = gson2.toJson(information);
        try {
            Writer writer1 = new FileWriter(args[1]);
            writer1.write(output);
            writer1.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        writer.flush();
        writer.close();
    }

    public static Thread initTimeService(int duration,int tickTime){
        TimeService timeService = new TimeService(tickTime,duration);
        Thread t = new Thread(timeService);
        return t;
    }



    public static List<Thread> initConferences(ArrayList<Object> conferences,ArrayList<ConfrenceInformation> realCon) {
        LinkedList<Thread> conferencesT = new LinkedList<>();
        for(int i = 0;i<conferences.size();i++){
            LinkedTreeMap<Object,Object> conference = (LinkedTreeMap<Object, Object>) conferences.get(i);
            Double date = (Double) conference.get("date");
            ConfrenceInformation confrenceInformation = new ConfrenceInformation((String) conference.get("name"),date.intValue());
            realCon.add(confrenceInformation);
            Thread t4 = new Thread(new ConferenceService(confrenceInformation));
            conferencesT.addFirst(t4);
        }
        return conferencesT;
    }


    public static List<Thread> initCPUS(ArrayList<Object> cpus) {
        List<Thread> cpusT = new ArrayList<>();
        for(int l = 0 ; l < cpus.size();l++){
            Double d = (Double) cpus.get(l);
            CPU cpu = new CPU(d.intValue());
            Thread t1 = new Thread(new CPUService(cpu));
            cpusT.add(t1);
        }
        return cpusT;
    }

    public static List<Thread> initGPUS(ArrayList<Object> gpus){
        List<Thread> GPUST = new ArrayList<>();
        for(int l = 0 ; l < gpus.size();l++){
            GPU.Type type1;
            if(gpus.get(l).equals("GTX1080"))
                type1 = GPU.Type.GTX1080;
            else if(gpus.get(l).equals("RTX2080"))
                type1 = GPU.Type.RTX2080;
            else
                type1 = GPU.Type.RTX3090;
            GPU gpu = new GPU(type1);
            Thread t2 = new Thread(new GPUService(gpu));
            GPUST.add(t2);
        }
        return GPUST;
    }

    public static List<Thread> initStudents(ArrayList<Object> students,ArrayList<Student> realStudents){
        List<Student> st = new ArrayList<>();
        List<Thread> studentsT = new ArrayList<>();
        for (int i = 0; i < students.size();i++){
            List<Model> modelss = new ArrayList<>();
            LinkedTreeMap<Object,Object> t = (LinkedTreeMap<Object, Object>)students.get(i);
            ArrayList<Object> models = (ArrayList<Object>) t.get("models");
            for(int j = 0;j<models.size();j++) {
                LinkedTreeMap<Object,Object> b = (LinkedTreeMap<Object, Object>) models.get(j);
                Data.Type type1 = null;
                if(b.get("type").toString().toUpperCase().equals("IMAGES"))
                    type1 = Data.Type.Images;
                else if(b.get("type").toString().toUpperCase().equals("TABULAR"))
                    type1 = Data.Type.Tabular;
                else if(b.get("type").toString().toUpperCase().equals("TEXT"))
                    type1 = Data.Type.Text;
                Double size = (Double) b.get("size");
                Data d = new Data(type1,size.intValue());
                Model model = new Model((String)b.get("name"),d);
                modelss.add(model);
            }
            Student.Degree degree;
            if(t.get("status").toString() == "PhD")
                degree = Student.Degree.PhD;
            else
                degree = Student.Degree.MSc;
            Student student = new Student((String) t.get("name"),(String) t.get("department"),degree,modelss);
            StudentService s = new StudentService(student);
            realStudents.add(student);
            Thread t4 = new Thread(s);
            studentsT.add(t4);
        }
        return studentsT;
    }

    private static void WaitForThreadsToFinish(Thread[] threads) throws InterruptedException {
        for (Thread t : threads) {
            if (t != null)
                t.join();
        }
    }
}