package lt.vaidotas.metasite;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;


public class CountTaskTest
{
    /**
     * Tests almost whole app, except writing to files
     */
    @Test
    public void WholeAppTest(){
        String[] filePaths = new String[]{"src/test/resources/one.txt",
            "src/test/resources/two.txt", "src/test/resources/three.txt"};
        List<Callable<Multiset<String>>> tasks = WordCounter.constructTasks(filePaths);
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
            e.printStackTrace();
        }
        List<Multiset<String>> multisets = WordCounter.splitMultiset(collectedMultiset);
        Assert.assertEquals(multisets.get(0).count("one"), 0 );
        Assert.assertEquals(multisets.get(0).count("two"), 0 );
        Assert.assertEquals(multisets.get(0).count("three"), 0 );
        Assert.assertEquals(multisets.get(0).count("four"), 0 );
        Assert.assertEquals(multisets.get(0).count("Four"), 1 );
        Assert.assertEquals(multisets.get(1).count("one"), 0 );
        Assert.assertEquals(multisets.get(1).count("two"), 0 );
        Assert.assertEquals(multisets.get(1).count("three"), 0 );
        Assert.assertEquals(multisets.get(1).count("four"), 0 );
        Assert.assertEquals(multisets.get(1).count("Four"), 0 );
        Assert.assertEquals(multisets.get(2).count("one"), 3 );
        Assert.assertEquals(multisets.get(2).count("two"), 3 );
        Assert.assertEquals(multisets.get(2).count("three"), 1 );
        Assert.assertEquals(multisets.get(2).count("four"), 0 );
        Assert.assertEquals(multisets.get(2).count("Four"), 0 );
        Assert.assertEquals(multisets.get(3).count("one"), 0 );
        Assert.assertEquals(multisets.get(3).count("two"), 0 );
        Assert.assertEquals(multisets.get(3).count("three"), 0 );
        Assert.assertEquals(multisets.get(3).count("four"), 0 );
        Assert.assertEquals(multisets.get(3).count("Four"), 0 );
    }
}
