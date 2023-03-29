import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static final AtomicInteger counter3 = new AtomicInteger(0);
    public static final AtomicInteger counter4 = new AtomicInteger(0);
    public static final AtomicInteger counter5 = new AtomicInteger(0);
    public static final int MASSIVE_NAMES_SIZE = 100_000;
    public static final String LETTERS = "abc";

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[MASSIVE_NAMES_SIZE];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText(LETTERS, 3 + random.nextInt(3));
        }
        List<Thread> threadList = new ArrayList<>();
        Thread threadCounter3 = new Thread(runnable(3, counter3, texts));
        Thread threadCounter4 = new Thread(runnable(4, counter4, texts));
        Thread threadCounter5 = new Thread(runnable(5, counter5, texts));
        threadList.add(threadCounter3);
        threadList.add(threadCounter4);
        threadList.add(threadCounter5);
        for (Thread thread : threadList) {
            thread.start();
            thread.join();
        }
        System.out.println("Красивых слов с длиной 3: " + counter3.get() + " шт." +
                "\nКрасивых слов с длиной 4: " + counter4.get() + " шт." +
                "\nКрасивых слов с длиной 5: " + counter5.get() + " шт.");
    }

    public static Runnable runnable(int length, AtomicInteger atomicInteger, String[] texts) {
        Runnable runText = () -> {
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].length() == length) {
                    if (checkEquals(texts[i]) == true || checkSymmetry(texts[i]) == true || checkIncrease(texts[i]) == true) {
                        atomicInteger.getAndIncrement();
                    }
                }
            }
        };
        return runText;
    }

    public static boolean checkSymmetry(String text) {
        int textLine = text.length();
        boolean[] isSymmetry = new boolean[textLine / 2];
        boolean[] symmetry = new boolean[textLine / 2];
        for (int j = 0; j < textLine / 2; j++) {
            if (text.charAt(j) == text.charAt(textLine - j - 1)) {
                isSymmetry[j] = true;
            }
            symmetry[j] = true;
        }
        if (Arrays.equals(isSymmetry, symmetry)) {
            return true;
        }
        return false;
    }

    public static boolean checkEquals(String text) {
        int textLine = text.length();
        boolean[] isEquals = new boolean[textLine];
        boolean[] textEquals = new boolean[textLine];
        char d = text.charAt(0);
        isEquals[0] = true;
        textEquals[0] = true;
        for (int j = 1; j < textLine; j++) {
            if (text.charAt(j) == d) {
                isEquals[j] = true;
            }
            textEquals[j] = true;
        }
        if (Arrays.equals(textEquals, isEquals)) {
            return true;
        }
        return false;
    }

    public static boolean checkIncrease(String text) {
        int textLine = text.length();
        boolean[] isIncrease = new boolean[textLine - 1];
        boolean[] increase = new boolean[textLine - 1];
        for (int j = 0; j < textLine - 1; j++) {
            if (text.charAt(j) <= text.charAt(j + 1) && text.charAt(j + 1) <= text.charAt(textLine - 1)) {
                isIncrease[j] = true;
            }
            increase[j] = true;
        }
        if (Arrays.equals(isIncrease, increase)) {
            return true;
        }
        return false;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
