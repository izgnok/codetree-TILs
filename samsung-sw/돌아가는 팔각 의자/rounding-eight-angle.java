import java.util.*;
import java.io.*;

public class Main {

    static int[][] gear;
    static int[] head;
    static Queue<Node> rotateCharis;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        gear = new int[4][8];
        head = new int[4];
        for (int i = 0; i < 4; i++) {
            String str = br.readLine().trim();
            for (int j = 0; j < 8; j++) gear[i][j] = str.charAt(j) - '0';
        }

        StringTokenizer st = new StringTokenizer(br.readLine());
        int T = Integer.parseInt(st.nextToken());

        rotateCharis = new ArrayDeque<>();
        while (T-- > 0) {
            st = new StringTokenizer(br.readLine());
            int idx = Integer.parseInt(st.nextToken()) - 1;
            int dir = Integer.parseInt(st.nextToken());
            rotateCharis.add(new Node(idx, dir));

            // 왼쪽 전파
            int preNum = left(idx);
            int preDir = dir;
            for (int i = idx - 1; i >= 0; i--) {
                if (preNum == right(i)) break;
                preNum = left(i);
                preDir *= -1;
                rotateCharis.add(new Node(i, preDir));
            }

            // 오른쪽 전파
            preNum = right(idx);
            preDir = dir;
            for (int i = idx + 1; i < 4; i++) {
                if (preNum == left(i)) break;
                preNum = right(i);
                preDir *= -1;
                rotateCharis.add(new Node(i, preDir));
            }

            rotate();
        }

        int sum = 0;
        for (int i = 0; i < 4; i++) {
            if (gear[i][head[i]] == 0) continue;   // 12시 N극이면 점수 X
            sum += (int) Math.pow(2, i);
        }
        sb.append(sum);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    // 접촉 톱니: head 기준 고정 오프셋
    static int left(int i)  { return gear[i][(head[i] + 6) % 8]; }   // 9시
    static int right(int i) { return gear[i][(head[i] + 2) % 8]; }   // 3시

    static void rotate() {
        while (!rotateCharis.isEmpty()) {
            Node node = rotateCharis.poll();
            if (node.dir == 1) {
                head[node.idx] = (head[node.idx] + 7) % 8;   // 시계
            } else {
                head[node.idx] = (head[node.idx] + 1) % 8;   // 반시계
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
