translatables
=============

translatables is a tool for localizing Scala and Java programs. It features a library to translate text by looking up translations from your translations file according to the grammar of the target language. A command line tool is also provided to scan Scala and Java sources for translatable texts, and to create and maintain translation lists.

Multilingual domain aware translations
--------------------------------------

Being domain aware when building translation lists or looking up translations means that the number of translations varies between languages. The grammar of the target language is taken into account for pluralization and the like which leads to using the minimal number of translations required to completely cover a language.

Essentially this removes the need to adapt the translated software when a new language is to be added. Programmer won't need knowledge of the user interface's language and code is less cluttered.

Usage for the impatient
-----------------------

A method call is used to identify translatable texts and to perform the translation. This is similar to other translation tools. For example a program translated with translatables might look like the following fragment.

```
import static your.application.tr;

public class Test {
	public static void main(String[] args){
		String translatedValue1 = tr("This is a sample");
		String translatedValue2 = tr("You've received {number(0)} messages.", 8);
	}
}

```

Translations can be extracted with the command line tool, for example:
```
java -jar translatables.jar extract --source /application/src/ --target /application/translations/de.json --language de --calls tr
```
The default implementation supports JSON, Java property files and gettext's PO files. Assess PO files for suitability because they are compatible with a range of translation systems and usually a good choice when working with external translators.

If you want to do some quick tests, be sure to try the `--pseudo-translations` flag. It generates accented versions of the input texts as translations.

Running the extraction leaves you with a file for manual translation:
```
{
  "This is a sample": "",
	"You've received {number(zero(0))} messages": "",
	"You've received {number(one(0))} messages": ""
}
```
Go on by fill out the blanks with the actual translations:
```
{
  "This is a sample": "Das ist ein Test",
	"You've received {number(zero(0))} messages": "Du hast {0} Nachrichten erhalten.",
	"You've received {number(one(0))} messages": "Du hast {0} Nachricht erhalten."
}
```
Finally, use the translatables library to select the suitable translations at runtime of your program. The library will take care of the grammar for most languages.
