package language;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

public class LanguageDetector {
	
	private static final int MAX_TEXTLENGTH = 140;
	Detector detector;

	public LanguageDetector() throws LangDetectException {
		detector = DetectorFactory.create();
		detector.setMaxTextLength(MAX_TEXTLENGTH);
	}
	
	public String detectLanguage(String text) throws LangDetectException {
		detector.append(text);
		return detector.detect();
	}

}
