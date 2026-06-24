import java.util.*;
import java.io.*;

public class Main {

    static Deque<Integer>[] rightCharis;
    static Deque<Integer>[] leftCharis;
    static Queue<Node> rotateCharis;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        rightCharis = new Deque[4];
        leftCharis = new Deque[4];
        for (int i = 0; i < 4; i++) {
            String str = br.readLine().trim();
            rightCharis[i] = new ArrayDeque<>();
            leftCharis[i] = new ArrayDeque<>();
            for (int j = 0; j < 2; j++) rightCharis[i].offerLast(str.charAt(j) - '0');
            for (int j = 7; j >= 2; j--) rightCharis[i].offerFirst(str.charAt(j) - '0');
            for (int j = 0; j < 6; j++) leftCharis[i].offerLast(str.charAt(j) - '0');
            for (int j = 7; j >= 6; j--) leftCharis[i].offerFirst(str.charAt(j) - '0');
        }
        StringTokenizer st = new StringTokenizer(br.readLine());
        int T = Integer.parseInt(st.nextToken());

        rotateCharis = new ArrayDeque<>();
        while (T-- > 0) {
            st = new StringTokenizer(br.readLine());
            int idx = Integer.parseInt(st.nextToken()) - 1;
            int dir = Integer.parseInt(st.nextToken());
            rotateCharis.add(new Node(idx, dir));

            int preNum = leftCharis[idx].peekFirst();
            int preDir = dir;
            for (int i = idx - 1; i >= 0; i--) {
                if (preNum == rightCharis[i].peekFirst()) break;
                preNum = leftCharis[i].peekFirst();
                preDir *= -1;
                rotateCharis.add(new Node(i, preDir));
            }
            preNum = rightCharis[idx].peekFirst();
            preDir = dir;
            for (int i = idx + 1; i < 4; i++) {
                if (preNum == leftCharis[i].peekFirst()) break;
                preNum = rightCharis[i].peekFirst();
                preDir *= -1;
                rotateCharis.add(new Node(i, preDir));
            }
            rotate();
        }

        int sum = 0;
        for (int i = 0; i < 4; i++) {
            int num = 0;
            for (int j = 0; j < 3; j++) {
                num = leftCharis[i].pollFirst();
            }
            if (num == 0) continue;
            sum += (int) Math.pow(2, i);
        }
        sb.append(sum);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void rotate() {
        while (!rotateCharis.isEmpty()) {
            Node node = rotateCharis.poll();
            if (node.dir == 1) {
                rightCharis[node.idx].offerFirst(rightCharis[node.idx].pollLast());
                leftCharis[node.idx].offerFirst(leftCharis[node.idx].pollLast());
            } else {
                rightCharis[node.idx].offerLast(rightCharis[node.idx].pollFirst());
                leftCharis[node.idx].offerLast(leftCharis[node.idx].pollFirst());
            }
        }
    }

    static class Node {
        int idx, dir;

        Node(int idx, int dir) {
            this.idx = idx;
            this.dir = dir;
        }

        @Override
        public String toString() {
            return "Node{" + "idx=" + idx + ", dir=" + dir + '}';
        }
    }

}
