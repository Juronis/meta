package lt.vaidotas.metasite;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * 
 * @author Vaidotas
 * Main class for word counter app
 */
public class WordCounter {

    private static final String CHARSET_NAME = "UTF-8";
    
    /**
     * 
     * @param filePaths paths to files to read from
     */
    public static void main(String[] filePaths) {
        List<Callable<Multiset<String>>> tasks = constructTasks(filePaths);
        ExecutorService exec = Executors.newFixedThreadPool(2);
        List<Future<Multiset<String>>> results;
        Multiset<String> collectedMultiset = HashMultiset.create();
        try {
            results = exec.invokeAll(tasks);
            for (Future<Multiset<String>> f : results) {
                for(String word : f.get().elementSet()){
                    collectedMultiset.add(word, f.get().count(word));
                }
            }
            exec.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error while executing multithreaded operations:");
            e.printStackTrace();
        }
        List<Multiset<String>> multisets = splitMultiset(collectedMultiset);
        MultisetPrinter multisetPrinter = new MultisetPrinter();
        multisetPrinter.printMultisetsToFiles(multisets, 
                new String[]{"aToG.txt", "hToN.txt", "oToU.txt", "vToZ.txt"});
    }    
    /**
     * 
     * @param filePaths
     * @return List of executables to run, one for each file.
     */
    public static  List<Callable<Multiset<String>>> constructTasks(String[] filePaths){
        List<Callable<Multiset<String>>> tasks = new ArrayList<Callable<Multiset<String>>>();
        for(String filePath : filePaths){
            tasks.add(new CountTask(filePath, Charset.forName(CHARSET_NAME)));
        }
        return tasks;
    }

    /**
     * 
     * @param multiset A multiset of Strings
     * @return four multisets, each ordered based on first letters, 
     * a to g, h to n, o to u and v to z .
     */
    public static List<Multiset<String>>  splitMultiset(Multiset<String> multiset){
        Multiset<String> aToG = HashMultiset.create();
        Multiset<String> hToN = HashMultiset.create();
        Multiset<String> oToU = HashMultiset.create();
        Multiset<String> vToZ = HashMultiset.create();
        for(String element: multiset.elementSet()){
            if((element.charAt(0) >= 'a' && element.charAt(0) <= 'g') ||
                    (element.charAt(0) >= 'A' && element.charAt(0) <= 'G')){
                aToG.add(element, multiset.count(element));
            }
            if((element.charAt(0) >= 'h' && element.charAt(0) <= 'n') ||
                    (element.charAt(0) >= 'H' && element.charAt(0) <= 'N')){
                hToN.add(element, multiset.count(element));
            }
            if((element.charAt(0) >= 'o' && element.charAt(0) <= 'u') ||
                    (element.charAt(0) >= 'O' && element.charAt(0) <= 'U')){
                oToU.add(element, multiset.count(element));
            }
            if((element.charAt(0) >= 'v' && element.charAt(0) <= 'z') ||
                (element.charAt(0) >= 'V' && element.charAt(0) <= 'Z')){
                vToZ.add(element, multiset.count(element));
            }
        }
        List<Multiset<String>> toReturn = new ArrayList<Multiset<String>>();
        toReturn.add(aToG);
        toReturn.add(hToN);
        toReturn.add(oToU);
        toReturn.add(vToZ);
        return toReturn;
    }
    
}
