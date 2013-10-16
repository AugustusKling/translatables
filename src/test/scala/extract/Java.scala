package extract

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.io.File
import languages.de
import languages.Root
import scala.io.Source
import java.io.FileWriter

@RunWith(classOf[JUnitRunner])
class Java extends FunSuite {

  test("Java source") {
    val src = File.createTempFile("test", ".java")
    val fw = new FileWriter(src)
    fw.write("""import translatables.TranslationKey;
    		
    class Test {
        @TranslationKey
    	private static final String MY_NUMBER_XYZ_SAMPLE = "annotated";
    	private static final String MY_NUMBER_XYZ_SAMPLE2 = "To be ignored";
    	private final String MY_NUMBER_XYZ_SAMPLE3 = "To be ignored";
    	private String MY_NUMBER_XYZ_SAMPLE4 = "To be ignored";
    	private String MY_NUMBER_XYZ_SAMPLE5;
        
        public void main(String[] args){
	        t("{number(varName)} blub!");
    		
    		tQuoted("{{");
    		
    		List<String> infer = new ArrayList<>();
    		
    		tQuotedSimple("a {{ b");
    		
    		tQuotedPlaceholder("a {{ {x} {{{y} end");
        }
    }""")
    fw.close()

    val je = new JavaExtractor(src, new ExtractionHint(Seq("t")))
    assert(je.extract(Root) === Set("{one(number(varName))} blub!", "annotated"))
    assert(je.extract(de) === Set("{one(number(varName))} blub!", "{zero(number(varName))} blub!", "annotated"))

    val jeQuoted = new JavaExtractor(src, new ExtractionHint(Seq("tQuoted")))
    assert(jeQuoted.extract(Root) === Set("{{", "annotated"))

    val jeQuotedSimple = new JavaExtractor(src, new ExtractionHint(Seq("tQuotedSimple")))
    assert(jeQuotedSimple.extract(Root) === Set("a {{ b", "annotated"))

    val jeQuotedPlaceholder = new JavaExtractor(src, new ExtractionHint(Seq("tQuotedPlaceholder")))
    assert(jeQuotedPlaceholder.extract(Root) === Set("a {{ {plain(plain(x))} {{{plain(plain(y))} end", "annotated"))
  }
  
  test("Bogus arguments to translation call") {
	  val src = File.createTempFile("test", ".java")
			  val fw = new FileWriter(src)
	  fw.write("""import translatables.TranslationKey;
			  
			  class Test {
	      
			  @TranslationKey
			  private static final String MY_NUMBER_XYZ_SAMPLE1 = "";

			  @TranslationKey
			  private static final int MY_NUMBER_XYZ_SAMPLE2 = 13;
			  
			  @TranslationKey
			  private static final String MY_NUMBER_XYZ_SAMPLE3 = null;
			  
			  public void main(String[] args){
				  t();
				  
				  t("");
				  
				  t(15, "a {{ b");
				  
				  t(null);
			  	
			  	String variable="sdf";
			  	t(variable);
				  }
			  }""")
			  fw.close()
			  
			  val je = new JavaExtractor(src, new ExtractionHint(Seq("t")))
	  assert(je.extract(Root) === Set(""))
	  assert(je.extract(de) === Set(""))
  }
}