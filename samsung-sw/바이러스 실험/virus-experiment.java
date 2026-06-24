import org.w3c.dom.Node;

import java.util.*;
import java.io.*;

public class Main {

    static int N, M, K;

    static int[][] map;
    static int[][] energies;
    static int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    static PriorityQueue<Node> viruses;
    static Queue<Node> addViruses;
    static Queue<Node> dieViruses;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        map = new int[N][N];
        energies = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                energies[i][j] = Integer.parseInt(st.nextToken());
                map[i][j] = 5;
            }
        }

        viruses = new PriorityQueue<>((a, b) -> a.age - b.age);
        addViruses = new ArrayDeque<>();
        dieViruses = new ArrayDeque<>();

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int age = Integer.parseInt(st.nextToken());
            viruses.offer(new Node(x, y, age));
        }

        while (!viruses.isEmpty() && K-- > 0) {
            // 섭취
            eat();

            // 양분 변환
            virusToEnergy();

            // 번식
            virusAdd();

            // 양분 증가
            energyAdd();
        }

        sb.append(viruses.size());
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void eat() {

        int size = viruses.size();
        Queue<Node> q = new ArrayDeque<>();

        for (int i = 0; i < size; i++) {
            Node virus = viruses.poll();

            if (map[virus.x][virus.y] >= virus.age) {
                map[virus.x][virus.y] -= virus.age;
                q.add(new Node(virus.x, virus.y, virus.age + 1));
                if ((virus.age + 1) % 5 == 0) addViruses.offer(virus);
            } else {
                dieViruses.offer(virus);
            }
        }

        while (!q.isEmpty()) viruses.offer(q.poll());
    }

    static void virusToEnergy() {
        while (!dieViruses.isEmpty()) {
            Node virus = dieViruses.poll();
            map[virus.x][virus.y] += virus.age / 2;
        }
    }

    static void virusAdd() {
        while (!addViruses.isEmpty()) {
            Node virus = addViruses.poll();
            for (int[] dir : dirs) {
                int row = virus.x + dir[0];
                int col = virus.y + dir[1];
                if (row < 0 || row >= N || col < 0 || col >= N) continue;
                viruses.offer(new Node(row, col, 1));
            }
        }
    }

    static void energyAdd() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                map[i][j] += energies[i][j];
            }
        }
    }

    static class Node {
        int x, y, age;

        Node(int x, int y, int age) {
            this.x = x;
            this.y = y;
            this.age = age;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ", " + age + ")";
        }
    }

}
