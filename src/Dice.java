import java.util.Random;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

class Dice {
    public static final int DROP_LOW = 0;
    public static final int DROP_HIGH = 1;
    static final int ADD = 0;
    static final int SUB = 1;
    public static String output = "";
    protected static int sum;
    private static Random rng = new Random();
    private static Scanner sc;
    
    public static void main(String[] args) {
        sc = new Scanner(System.in);
        String input;
        while (true) {
            input = sc.nextLine().trim();
            if (input.equals("")) return;
            else {
                parse(input);
                System.out.println(input+" : "+Dice.output+" = "+sum+"\nRolled "+Dice.sum);
                output="";
                sum=0;
            } 
        }
    }
    
    public static int parse(String input) {
        sum = 0;
        String seq;
        String pattern = "((?:\\+|\\-|\\*)|(?:\\d+d\\d+(?:dL\\d+|dH\\d+)?)|(?:\\d+))";
        Matcher m = Pattern.compile(pattern).matcher(input);
        m.find();
        seq = m.group(0);
        while (m.find()) {
            seq+=","+m.group(0);
        }
        try { 
            process(seq.split(","));
            return sum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void process(String[] seq) throws Exception {
        int value = 1;
        int sign = ADD;
        for (int i = 0; i < seq.length; i++) {
            if (i>0) {
                if (seq[i-1].equals("-")) {
                    sign = SUB;
                }
                output += " "+seq[i-1]+" ";
            }
            if (seq[i].matches("\\d+")) {
                value *= Integer.parseInt(seq[i]);
                output += seq[i];
            } else if (seq[i].matches("\\d+d\\d+(?:dL\\d+|dH\\d+)?")) {
                value *= compute(seq[i]);
            } else throw new Exception("unrecognized pattern");
            if ((++i<seq.length && !seq[i].equals("*")) ||
            i==seq.length) {
                if (sign==ADD) {
                    sum += value;
                } else {
                    sum -= value;
                    sign = ADD;
                }
                value = 1;
            }
        }
    }

    private static int compute(String expression) {
        String pattern = "(\\d+)d(\\d+)((dL|dH)((\\d+)))?";
        Matcher m = Pattern.compile(pattern).matcher(expression);
        m.find();
        if (m.group(3)==null) {
            return roll(Integer.parseInt(m.group(1)),
                        Integer.parseInt(m.group(2)));
        } else {
            int drop = m.group(4).matches("dL")?DROP_LOW:DROP_HIGH;
            return roll(Integer.parseInt(m.group(1)),
                        Integer.parseInt(m.group(2)),
                        drop,
                        Integer.parseInt(m.group(5)));
        }
    }

    public static int roll(int d, int v) {
        return roll(d,v,0,0);
    }
    public static int roll(int d, int v, int drop, int dropCount) {
        if (dropCount >= d) throw new IllegalArgumentException(
            "at least one die must be returned.");
        int[] rolls = new int[d];
        int sum = 0;
        for (int i = 0; i<d ; i++) {
            rolls[i] = Math.abs(rng.nextInt())%v+1;
            sum+=rolls[i];
        }
        boolean dropValue;
        String[] result = new String[d];
        for (int i=0; i<d; i++) {
            result[i] = ""+rolls[i];
        }
        for (int i = 0; i<d && dropCount>0; i++) {
            dropValue = true;
            int count = dropCount;
            for (int j=i+1; j<d; j++) {
                if (
                (drop==DROP_LOW && rolls[i]>rolls[j] || 
                drop==DROP_HIGH && rolls[j]>rolls[i])
                && --count==0) {
                    dropValue = false;
                    break;
                }
            }
            if (dropValue) {
                sum-=rolls[i];
                rolls[i] = v+1;
                dropCount--;
                result[i] += "*";
            }
        }
        output += Arrays.toString(result);
        return sum;
    }
}