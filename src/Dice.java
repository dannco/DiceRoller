import java.util.Random;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.stream.IntStream;

class Dice {
    private static final int DROP_LOW = 0, DROP_HIGH = 1, ADD = 1, SUB = -1;

    private static int sum;
    private static StringJoiner joiner;
    private static Random rng = new Random();
    private static Scanner sc;
    
    public static void main(String[] args) {
        sc = new Scanner(System.in);
        String input;
        while (true) {
            input = sc.nextLine().trim();
            if (input.equals("")) return;
            else {
                joiner = new StringJoiner(" ").add(input).add(":");
                parse(input);
                joiner.add("=").add(String.valueOf(sum)).add("\nRolled").add(String.valueOf(sum));
                System.out.println(joiner.toString());
            }
        }
    }
    
    public static int parse(String input) {
        sum = 0;
        String[] seq = input.replaceAll("\\s*([+\\-*])\\s*","¤$1¤").split("¤");
        try { 
            process(seq);
            return sum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void process(String[] seq) throws Exception {
        int value = ADD;
        for (int i = 0; i < seq.length; i++) {
            if (seq[i].length()==0) continue;
            if (seq[i].matches("[-+*]")) {
                joiner.add(seq[i]);
                if (seq[i].matches("[+-]")) {
                    sum += value;
                    value = seq[i].equals("-") ? SUB : ADD;
                }
                continue;
            }
            value *= compute(seq[i]);
        }
        sum += value;
    }

    private static int compute(String expression) {
        if (expression.matches("\\d+")) {
            joiner.add(expression);
            return Integer.parseInt(expression);
        }
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

        int[] rolls = IntStream.range(0,d).map(i -> Math.abs(rng.nextInt())%v+1).toArray();
        int sum = Arrays.stream(rolls).sum();

        boolean dropValue;
        Object[] result = Arrays.stream(rolls).mapToObj(roll -> String.valueOf(roll)).toArray();

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
        joiner.add(Arrays.toString(result));
        return sum;
    }
}