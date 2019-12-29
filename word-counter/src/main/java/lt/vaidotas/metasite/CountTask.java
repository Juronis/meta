package lt.vaidotas.metasite;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * 
 * @author Vaidotas
 * Class which reads file into words and counts them in a separate thread
 */
class CountTask implements Callable<Multiset<String>> {
    
    private String filePath;
    private Charset charset;
    
    public CountTask(String filePath, Charset charset) {
        this.filePath = filePath;
        this.charset = charset;
    }
    
    /**
     * reads text from file, and counts how many times each word 
     * appeared.   
     */
    @Override
    public Multiset<String> call() {
        String content = null;
        try {
            content = readFile(filePath, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder word = new StringBuilder();
        Multiset<String> multiset = HashMultiset.create();
        if (content != null){
            for (int i = 0; i < content.length(); i++) {
                if (Character.isWhitespace(content.charAt(i)) || 
                        content.charAt(i) == '-' || content.charAt(i) == '—') {
                    if (word.length() > 0) {
                        multiset.add(word.toString());
                        word.setLength(0);
                    }
                } else {
                    if(isIgnorableSymbol(content.charAt(i))){
                        word.append(content.charAt(i));
                        if( i == content.length()-1){
                            multiset.add(word.toString());
                            word.setLength(0);
                        }
                    }
                }
            }
        }
        return multiset;
    }
    
    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    
    private boolean isIgnorableSymbol(char symbol){
    	return (symbol!=',' && symbol!='!' &&
    			symbol!='.' && symbol!='?'
                && symbol!='”' && symbol!='“'
                && symbol!=')' && symbol!='('
                && symbol!='’' && symbol!=':'
                && symbol!='‘' && symbol!=':'
                && symbol!=';');
    }
}