package lt.vaidotas.metasite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Multiset;
/**
 * @author Vaidotas
 */
public class MultisetPrinter {
    
    /**
     * prints multiset elements and their counts to console
     * @param multiset
     */
    public void printMultisetToConsole(Multiset<String> multiset){
        List<String> sortedList = new ArrayList<String>(multiset.elementSet());
        Collections.sort(sortedList);
        for (String word : sortedList) {
            System.out.printf("Word: %s, count: %s \n", word, multiset.count(word));
        }
    }
    
    /**
     * prints multiset elements and their counts to file
     * @param multiset
     */
    public void printMultisetToFile(Multiset<String> multiset, String fileName){
        Path path = Paths.get(fileName);
        List<String> sortedList = new ArrayList<String>(multiset.elementSet());
        Collections.sort(sortedList);
        StringBuilder builder = new StringBuilder();
        for (String word : sortedList) {
            builder.append(word + " " + multiset.count(word) + System.lineSeparator());
        }
        byte[] strToBytes = builder.toString().getBytes();
        try {
            Files.write(path, strToBytes);
        } catch (IOException e) {
            System.out.println("Error while writing  multiset to file: ");
            e.printStackTrace();
        }
    }
    
    /**
     * prints multisets to files
     * @param multisets Multisets 
     * filePaths paths to files. 
     * 
     * Elements will be printed to files 
     * in the same order as files and multisets are ordered. Number if files
     * and multisets must match.
     * 
     */
    public void printMultisetsToFiles(List<Multiset<String>> multisets, String[] filePaths)
        throws IllegalArgumentException{
        int i = 0;
        if(multisets.size() != filePaths.length){
            throw new IllegalArgumentException("Number of files does not match the number of"
                    + " multisets");
        }
        for(Multiset<String> multiset: multisets){
            printMultisetToFile(multiset, filePaths[i]);
            i++;
        }
    }
    
}
