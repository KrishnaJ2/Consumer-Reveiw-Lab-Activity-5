import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();
  
  static{
    try {
      Scanner input = new Scanner(new File("cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        // System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("positiveAdjectives.txt"));
      while(input.hasNextLine()){
        posAdjectives.add(input.nextLine().trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("negativeAdjectives.txt"));
      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }   
  }
  
  /** 
   * returns a string containing all of the text in fileName (including punctuation), 
   * with words separated by a single space 
   */
  public static String textToString( String fileName )
  {  
    String temp = "";
    try {
      Scanner input = new Scanner(new File(fileName));
      
      //add 'words' in the file to the string, separated by a single space
      while(input.hasNext()){
        temp = temp + input.next() + " ";
      }
      input.close();
      
    }
    catch(Exception e){
      System.out.println("Unable to locate " + fileName);
    }
    // remove any additional space that may have been added at the end of the string
    return temp.trim();
  }
  
  /**
   * @returns the sentiment value of word as a number between -1 (very negative) to 1 (very positive sentiment) 
   */
  public static double sentimentVal( String word )
  {
    try
    {
      return sentiment.get(word.toLowerCase());
    }
    catch(Exception e)
    {
      return 0;
    }
  }
  
  /**
   * Returns the ending punctuation of a string, or the empty string if there is none 
   */
  public static String getPunctuation( String word )
  { 
    String punc = "";
    for(int i=word.length()-1; i >= 0; i--){
      if(!Character.isLetterOrDigit(word.charAt(i))){
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }

  /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation( String word )
  {
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
    {
      word = word.substring(1);
    }
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
    {
      word = word.substring(0, word.length()-1);
    }
    
    return word;
  }
 
  /** 
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and returns it.
   */
  public static String randomPositiveAdj()
  {
    int index = (int)(Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }
  
  /** 
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and returns it.
   */
  public static String randomNegativeAdj()
  {
    int index = (int)(Math.random() * negAdjectives.size());
    return negAdjectives.get(index);
    
  }
  
  /** 
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective()
  {
    boolean positive = Math.random() < .5;
    if(positive){
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }

  // ===========================
  // NEW METHODS ADDED BELOW
  // ===========================

  /**
   * Count how many times a given flavor (like "vanilla" or "chocolate")
   * appears in the provided text.
   *
   * Requirements hit:
   * - takes a parameter
   * - uses loops / iteration
   * - uses String methods (toLowerCase, split, equals)
   * - uses conditionals
   */
  public static int countFlavor(String text, String flavor) {
    int count = 0;

    // normalize case so "Vanilla" and "vanilla" both match
    String lowerText = text.toLowerCase();
    String[] words = lowerText.split(" ");

    for (int i = 0; i < words.length; i++) {
      // strip punctuation using an existing helper
      String cleanWord = removePunctuation(words[i]);

      if (cleanWord.equals(flavor.toLowerCase())) {
        count++;
      }
    }

    return count;
  }

  /**
   * Compute average sentiment of a big block of text.
   * We'll:
   * - split into words
   * - remove punctuation
   * - look up each word's sentiment with sentimentVal()
   * - only count words that actually have nonzero sentiment
   *
   * Requirements hit:
   * - extra custom method with a parameter
   * - iteration
   * - conditionals / compound boolean expression
   * - String methods
   */
  public static double averageSentiment(String text) {
    String[] words = text.split(" ");

    double total = 0.0;
    int countedWords = 0;

    for (int i = 0; i < words.length; i++) {
      String cleaned = removePunctuation(words[i]);

      if (cleaned.length() > 0) {
        double val = sentimentVal(cleaned);

        // only include words that actually carry sentiment
        if (val > 0.0001 || val < -0.0001) {
          total += val;
          countedWords++;
        }
      }
    }

    if (countedWords == 0) {
      return 0.0;
    }

    return total / countedWords;
  }

  /**
   * Helper that returns a short summary about which flavor
   * was mentioned more.
   *
   * More String usage, more conditionals.
   */
  public static String flavorSummary(int vanillaCount, int chocolateCount) {
    if (vanillaCount > chocolateCount) {
      return "People talked more about vanilla than chocolate.";
    } else if (chocolateCount > vanillaCount) {
      return "People talked more about chocolate than vanilla.";
    } else {
      return "People talked about vanilla and chocolate equally.";
    }
  }

  /**
   * MAIN METHOD
   *
   * This is required by the assignment:
   * - has main
   * - calls at least one new method with a parameter
   * - uses conditional logic
   * - uses iteration
   * - analyzes data.txt (your 100 reviews)
   *
   * When you run `java Review`, this will execute.
   */
  public static void main(String[] args) {

    // 1. pull all review text from data.txt
    String reviewText = textToString("data.txt");

    // 2. count how many times each flavor appears
    int vanillaMentions = countFlavor(reviewText, "vanilla");
    int chocolateMentions = countFlavor(reviewText, "chocolate");

    // 3. decide who is "winning"
    String winner;
    if (vanillaMentions > chocolateMentions) {
      winner = "vanilla";
    } else if (chocolateMentions > vanillaMentions) {
      winner = "chocolate";
    } else {
      winner = "tie";
    }

    // 4. compute overall sentiment across all reviews
    double avgSent = averageSentiment(reviewText);

    // 5. print the analysis for presentation
    System.out.println("=== Ice Cream Review Analysis ===");
    System.out.println("Total vanilla mentions: " + vanillaMentions);
    System.out.println("Total chocolate mentions: " + chocolateMentions);
    System.out.println("Most talked-about flavor: " + winner);
    System.out.println("Average sentiment score (-1 to 1): " + avgSent);

    // 6. interpret tone using conditionals
    if (avgSent > 0.2) {
      System.out.println("Overall tone: mostly positive toward ice cream.");
    } else if (avgSent < -0.2) {
      System.out.println("Overall tone: kinda negative toward ice cream.");
    } else {
      System.out.println("Overall tone: pretty neutral / mixed feelings.");
    }

    // 7. bonus summary using another custom method that takes parameters
    System.out.println(flavorSummary(vanillaMentions, chocolateMentions));
  }

}