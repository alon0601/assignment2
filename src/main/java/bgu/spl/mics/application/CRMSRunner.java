package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        String path = "C:\\Users\\alon5\\OneDrive\\Desktop\\example.json";
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Thread> threads = new ArrayList<>();
        List<Student> st = new ArrayList<>();
        List<Model> modelSt = new ArrayList<>();
        Gson gson = new Gson();
        HashMap<Object, Object> json = gson.fromJson(bufferedReader, HashMap.class);
        Double dTickTime = (Double) json.get("TickTime");
        Double dDuration = (Double) json.get("Duration");
        int duration = dDuration.intValue();
        int tickTime = dTickTime.intValue();
        TimeService timeService = new TimeService(tickTime,duration);
        Thread threadTime = new Thread(timeService);
        ArrayList<Object> students = (ArrayList<Object>)json.get("Students");
        for (int i = 0; i < students.size();i++){
            LinkedTreeMap<Object,Object> t = (LinkedTreeMap<Object, Object>)students.get(i);
            Object TempM = t.get("models");
            ArrayList<Object> models = (ArrayList<Object>) TempM;
            for(int j = 0;j<models.size();j++) {
                LinkedTreeMap<Object,Object> b = (LinkedTreeMap<Object, Object>) models.get(j);
                Data.Type type1;
                if(b.get("type").toString() == "Images")
                    type1 = Data.Type.Images;
                else if(b.get("type").toString() == "Tabular")
                    type1 = Data.Type.Tabular;
                else
                    type1 = Data.Type.Text;
                Double size = (Double) b.get("size");
                Data d = new Data(type1,size.intValue());
                Model model = new Model((String)b.get("name"),d);
                modelSt.add(model);
            }
            Student.Degree degree;
            if(t.get("status").toString() == "PhD")
                degree = Student.Degree.PhD;
            else
                degree = Student.Degree.MSc;
            Student student = new Student((String) t.get("name"),(String) t.get("department"),degree,modelSt);
            Thread t4 = new Thread(new StudentService(student));
            threads.add(t4);
            st.add(student);
            System.out.println(student);
        }
        threads.add(threadTime);

        List<GPU> gpus1 = new ArrayList<>();
        ArrayList<Object> gpus = (ArrayList<Object>) json.get("GPUS");
        for(int l = 0 ; l < gpus.size();l++){
            GPU.Type type1;
            if(gpus.get(l).equals("GTX1080"))
                type1 = GPU.Type.GTX1080;
            else if(gpus.get(l).equals("RTX2080"))
                type1 = GPU.Type.RTX2080;
            else
                type1 = GPU.Type.RTX3090;
            GPU gpu = new GPU(type1);
            gpus1.add(gpu);
            Thread t2 = new Thread(new GPUService(gpu));
            threads.add(t2);
        }
        List<CPU> cpus1 = new ArrayList<>();
        ArrayList<Object>  cpus = (ArrayList<Object>) json.get("CPUS");
        for(int l = 0 ; l < gpus.size();l++){
            Double d = (Double) cpus.get(l);
            CPU cpu = new CPU(d.intValue());
            Thread t1 = new Thread(new CPUService(cpu));
            threads.add(t1);
            cpus1.add(cpu);
        }

        ArrayList<Object> conferences = (ArrayList<Object>) json.get("Conferences");
        for(int i = 0;i<conferences.size();i++){
            LinkedTreeMap<Object,Object> conference = (LinkedTreeMap<Object, Object>) conferences.get(i);
            Double date = (Double) conference.get("date");
            ConfrenceInformation confrenceInformation = new ConfrenceInformation((String) conference.get("name"),date.intValue());
            Thread t4 = new Thread(new ConferenceService(confrenceInformation));
            threads.add(t4);
        }
        for(Thread t : threads)
            t.start();
    }
}
