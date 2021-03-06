package ist.spln.stringmodifiers;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import ist.spln.Main;
import ist.spln.needleman.valueobject.MEDValueObject;
import ist.spln.needleman.valueobject.NeedlemanArrayValueObject;
import ist.spln.needleman.valueobject.NeedlemanArrayValueObjectWithMoreInfo;
import ist.spln.textanalysis.TextAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class Lemmatizer {
    TextAnalyzer textAnalyzer;

    public Lemmatizer() {
        this.textAnalyzer = new TextAnalyzer(Main.ANALYZER_PROPERTIES);
    }

    public List<String> lemmatize(String text) {
        List<String> lemmas = new ArrayList<>();
        List<CoreMap> sentences = this.textAnalyzer.analyze(text);
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                if (!lemma.matches(".*\\p{L}.*")) { //\p{L} = any letter
                    continue;
                }
                lemmas.add(lemma.toLowerCase());
            }
        }
        return lemmas;
    }

    public NeedlemanArrayValueObject[] modify(List<String> text) { //should not have needleman stuff, but its faster this way... i think...
        List<NeedlemanArrayValueObject> valueObjects = new ArrayList<>();
        for (int i = 0; i < text.size(); i++) {
            List<CoreMap> sentences = this.textAnalyzer.analyze(text.get(i));
            for (CoreMap sentence : sentences) {
                // traversing the words in the current sentence
                // a CoreLabel is a CoreMap with additional token-specific methods
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                    if (!lemma.matches(".*\\p{L}.*")) { //\p{L} = any letter
                        continue;
                    }
                    valueObjects.add(new NeedlemanArrayValueObjectWithMoreInfo(lemma.toLowerCase(), i));
                }
            }
        }
        NeedlemanArrayValueObject[] valueObjectsArray = new NeedlemanArrayValueObject[valueObjects.size()];
        return valueObjects.toArray(valueObjectsArray);
    }
}
