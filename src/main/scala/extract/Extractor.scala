package extract

import java.io.File
import languages.de

object Extractor extends App {
	val Array(rootPath, languageName) = args;
	
	val extractor = new JavaExtractor(new File(rootPath));
	println(extractor.extract(de))
}