package scripts.justj.justarceuuslibrary.library;

import scripts.justj.justarceuuslibrary.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Credit here goes to Runelite - this is taken from their plugin
 */
public enum Book {

  DARK_MANUSCRIPT_13514(Constants.Items.DARK_MANUSCRIPT),
  DARK_MANUSCRIPT_13515(Constants.Items.DARK_MANUSCRIPT_13515),
  DARK_MANUSCRIPT_13516(Constants.Items.DARK_MANUSCRIPT_13516),
  DARK_MANUSCRIPT_13517(Constants.Items.DARK_MANUSCRIPT_13517),
  DARK_MANUSCRIPT_13518(Constants.Items.DARK_MANUSCRIPT_13518),
  DARK_MANUSCRIPT_13519(Constants.Items.DARK_MANUSCRIPT_13519),
  DARK_MANUSCRIPT_13520(Constants.Items.DARK_MANUSCRIPT_13520),
  DARK_MANUSCRIPT_13521(Constants.Items.DARK_MANUSCRIPT_13521),
  DARK_MANUSCRIPT_13522(Constants.Items.DARK_MANUSCRIPT_13522),
  DARK_MANUSCRIPT_13523(Constants.Items.DARK_MANUSCRIPT_13523),

  RADAS_CENSUS(Constants.Items.RADAS_CENSUS, "Rada's Census", "Census of King Rada III, by Matthias Vorseth."),
  RICKTORS_DIARY_7(Constants.Items.RICKTORS_DIARY_7, "Ricktor's Diary 7", "Diary of Steklan Ricktor, volume 7."),
  EATHRAM_RADA_EXTRACT(Constants.Items.EATHRAM__RADA_EXTRACT, "Eathram & rada extract", "An extract from Eathram & Rada, by Anonymous."),
  KILLING_OF_A_KING(Constants.Items.KILLING_OF_A_KING, "Killing of a king", "Killing of a King, by Griselle."),
  HOSIDIUS_LETTER(Constants.Items.HOSIDIUS_LETTER, "Hosidius Letter", "A letter from Lord Hosidius to the Council of Elders."),
  WINTERTODT_PARABLE(Constants.Items.WINTERTODT_PARABLE, "Wintertodt Parable", "The Parable of the Wintertodt, by Anonymous."),
  TWILL_ACCORD(Constants.Items.TWILL_ACCORD, "Twill Accord", "The Royal Accord of Twill."),
  BYRNES_CORONATION_SPEECH(Constants.Items.BYRNES_CORONATION_SPEECH, "Byrnes Coronation Speech", "Speech of King Byrne I, on the occasion of his coronation."),
  IDEOLOGY_OF_DARKNESS(Constants.Items.IDEOLOGY_OF_DARKNESS, "The Ideology of Darkness", "The Ideology of Darkness, by Philophaire."),
  RADAS_JOURNEY(Constants.Items.RADAS_JOURNEY, "Rada's journey", "The Journey of Rada, by Griselle."),
  TRANSVERGENCE_THEORY(Constants.Items.TRANSVERGENCE_THEORY, "Transvergence Theory", "The Theory of Transvergence, by Amon Ducot."),
  TRISTESSAS_TRAGEDY(Constants.Items.TRISTESSAS_TRAGEDY, "Tristessa's tragedy", "The Tragedy of Tristessa."),
  TREACHERY_OF_ROYALTY(Constants.Items.TREACHERY_OF_ROYALTY, "The Treachery of Royalty", "The Treachery of Royalty, by Professor Answith."),
  TRANSPORTATION_INCANTATIONS(Constants.Items.TRANSPORTATION_INCANTATIONS, "Transportation Incantations", "Transportation Incantations, by Amon Ducot."),
  SOUL_JOURNEY(Constants.Items.SOUL_JOURNEY, "Soul Journey", "The Journey of Souls, by Aretha."),
  VARLAMORE_ENVOY(Constants.Items.VARLAMORE_ENVOY, "Varlamore Envoy", "The Envoy to Varlamore, by Deryk Paulson.");

  private static final Map<Integer, Book> BY_ID = buildById();
  private static final Map<String, Book> BY_NAME = buildByName();

  private static Map<Integer, Book> buildById() {
    HashMap<Integer, Book> byId = new HashMap<>();
    for (Book b : Book.values()) {
      byId.put(b.item, b);
    }
    return Collections.unmodifiableMap(byId);
  }

  private static Map<String, Book> buildByName() {
    HashMap<String, Book> byName = new HashMap<>();
    for (Book b : Book.values()) {
      if (!b.isDarkManuscript) {
        byName.put(b.name, b);
      }
    }
    return Collections.unmodifiableMap(byName);
  }

  public static Book byId(int id) {
    return BY_ID.get(id);
  }

  public static Book byName(String name) {
    return BY_NAME.get(name);
  }

  private final int item;

  private final String name;

  private final String shortName;

  private final boolean isDarkManuscript;

  Book(int id, String shortName, String name) {
    this.item = id;
    this.isDarkManuscript = false;
    this.shortName = shortName;
    this.name = name;
  }

  Book(int id) {
    this.item = id;
    this.isDarkManuscript = true;
    this.name = "Dark Manuscript";
    this.shortName = "Dark Manuscript";
  }

  public int getItem() {
    return item;
  }

  public String getName() {
    return name;
  }

  public String getShortName() {
    return shortName;
  }

  public boolean isDarkManuscript() {
    return isDarkManuscript;
  }
}
