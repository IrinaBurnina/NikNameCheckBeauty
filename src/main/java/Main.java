import java.util.*;
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
        checkBeauty(3, texts, counter3);
        checkBeauty(4, texts, counter4);
        checkBeauty(5, texts, counter5);
        System.out.println("Красивых слов с длиной 3: " + counter3.get() + " шт." +
                "\nКрасивых слов с длиной 4: " + counter4.get() + " шт." +
                "\nКрасивых слов с длиной 5: " + counter5.get() + " шт.");
    }

    public static void checkBeauty(int length, String[] texts, AtomicInteger atomicInteger) throws InterruptedException {
        for (String text : texts) {
            if (text.length() == length) {
                Thread thread1 = new Thread(() -> {
                    if (checkIncrease(text)) {
                        atomicInteger.getAndIncrement();
                    }
                });
                Thread thread2 = new Thread(() -> {
                    if (checkSymmetry(text)) {
                        atomicInteger.getAndIncrement();
                    }
                });
                Thread thread3 = new Thread(() -> {
                    if (checkEquals(text)) {
                        atomicInteger.getAndIncrement();
                    }
                });
                thread1.start();
                thread2.start();
                thread3.start();

                thread1.join();
                thread2.join();
                thread3.join();
            }
        }
    }


    public static boolean checkSymmetry(String text) {
        StringBuilder sb = new StringBuilder(text);
        return text.equals(sb.reverse().toString());
    }

    public static boolean checkEquals(String text) {
        return text.chars().allMatch(x -> x == text.charAt(0));
    }

    public static boolean checkIncrease(String text) {
//        List <String> arrayText= text.lines()
//                .map(s -> s.split(""))
//                .flatMap(Arrays::stream)
//                .collect(Collectors.toList());
//       List<String> list=text.lines()
//               .map(s->s.split(""))
//                .flatMap(Arrays::stream).sorted()
//                .collect(Collectors.toList());
//        return arrayText.equals(list);

        int textLine = text.length();
        boolean[] isIncrease = new boolean[textLine - 1];
        boolean[] increase = new boolean[textLine - 1];
        for (int j = 0; j < textLine - 1; j++) {
            if (text.charAt(j) <= text.charAt(j + 1) && text.charAt(j + 1) <= text.charAt(textLine - 1)) {
                isIncrease[j] = true;
            }
            increase[j] = true;
        }
        return Arrays.equals(isIncrease, increase);
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
