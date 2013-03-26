package language;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

public class LanguageDetector {
	
	Detector detector;

	public LanguageDetector() throws LangDetectException {
		detector = DetectorFactory.create();
	}
	
	public String detectLanguage(String text) throws LangDetectException {
		detector.append(text);
		return detector.detect();
	}

}
