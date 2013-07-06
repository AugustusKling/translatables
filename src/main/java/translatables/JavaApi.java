package translatables;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Implementation of Java API. Use this from Java programs to avoid direct dependency on Scala.
 */
public class JavaApi implements TranslationApi {
	public static final TranslationApi instance;
	static {
		instance = new JavaApi();
	}

	public String getVariadic(Language lang, Adapter adapter, String key,
			Object... placeholder) {
		List<Object> list = Arrays.asList(placeholder);
		return Translation.getList(lang, adapter, key, list);
	}

	public String get(Language lang, Adapter adapter, String sourceKey,
			Map<String, Object> replacements) {
		return Translation.get(lang, adapter, sourceKey, replacements);
	}

}
