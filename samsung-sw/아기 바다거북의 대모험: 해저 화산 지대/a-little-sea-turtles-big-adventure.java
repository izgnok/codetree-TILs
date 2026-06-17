import org.w3c.dom.Node;

import java.util.*;
import java.io.*;

public class Main {

    static int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // 우,하,좌,상
    static int INF = 987654321;

    static int N, M, K;
    static int[][] map; // -1: 산호초, -2: 화석, 1,2,3...: 거북이 번호
    static int[][] fireMap; // 1,2,3 ...: 화산 번호

    static Queue<Turtle> turtles;
    static List<Fire> fires;
    static int[][] firePowers; // 열기정보
    static Queue<Integer> fireQueue;

    static int Time;
    static boolean[] died;
    static boolean[] check;
    static int[] result;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        map = new int[N][N];
        fireMap = new int[N][N];
        firePowers = new int[N][N];
        turtles = new ArrayDeque<>();
        fires = new ArrayList<>();
        fireQueue = new ArrayDeque<>();

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken()) * -1;
            }
        }

        for (int i = 1; i <= M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            map[x][y] = i;
            turtles.add(new Turtle(i, x, y));
        }

        fires.add(null);
        for (int i = 1; i <= K; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int max = Integer.parseInt(st.nextToken());

            fireMap[x][y] = i;
            fires.add(new Fire(x, y, 0, max));
        }

        result = new int[M + 1];
        died = new boolean[M + 1];
        check = new boolean[K + 1];
        Time = 0;
        while (++Time <= 100 && !turtles.isEmpty()) {
            turtleMove();
            fireAdd();
            fireMove();
            dieTurtle();
            envReset();
        }

        for (int i = 1; i <= M; i++) {
            if (result[i] == 0) sb.append("-1\n");
            else sb.append(result[i]).append("\n");
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void turtleMove() {
        int size = turtles.size();
        for (int i = 1; i <= size; i++) {
            Turtle turtle = turtles.poll();
            if (died[turtle.num]) continue;

            int min = INF;
            int dir = -1;
            for (int d = 0; d < 4; d++) {
                int row = turtle.x + dirs[d][0];
                int col = turtle.y + dirs[d][1];
                if (row < 0 || row >= N || col < 0 || col >= N) continue;
                if (map[row][col] != 0) continue;

                int count = bfs(new Turtle(turtle.num, row, col));
                if (min > count) {
                    min = count;
                    dir = d;
                }
            }

            if (min == INF || dir == -1) {
                turtles.add(turtle);
                continue;
            }
            map[turtle.x][turtle.y] = 0;
            int nextX = turtle.x + dirs[dir][0];
            int nextY = turtle.y + dirs[dir][1];

            if (nextX == N - 1 && nextY == N - 1) {
                result[turtle.num] = Time;
                continue;
            }
            map[nextX][nextY] = turtle.num;
            turtles.add(new Turtle(turtle.num, nextX, nextY));
        }
    }

    static int bfs(Turtle turtle) {
        boolean[][] visit = new boolean[N][N];
        visit[turtle.x][turtle.y] = true;

        Queue<Node> q = new ArrayDeque<>();
        q.add(new Node(turtle.x, turtle.y, 0));

        while (!q.isEmpty()) {
            Node node = q.poll();
            if (node.x == N - 1 && node.y == N - 1) return node.count;

            for (int[] dir : dirs) {
                int row = node.x + dir[0];
                int col = node.y + dir[1];

                if (row < 0 || row >= N || col < 0 || col >= N) continue;
                if (visit[row][col]) continue;
                if (map[row][col] != 0) continue;

                visit[row][col] = true;
                q.add(new Node(row, col, node.count + 1));
            }
        }
        return INF;
    }

    static void fireAdd() {
        for (int i = 1; i < fires.size(); i++) {
            Fire fire = fires.get(i);
            fire.power += 10;

            if (fire.power >= fire.max) {
                fireQueue.add(i);
                check[i] = true;
            }
        }
    }

    static void fireMove() {
        while (!fireQueue.isEmpty()) {
            Fire fire = fires.get(fireQueue.poll());
            firePowers[fire.x][fire.y] += fire.max;
            for (int[] dir : dirs) {
                for (int i = 1; i <= N; i++) {
                    int row = fire.x + (dir[0] * i);
                    int col = fire.y + (dir[1] * i);
                    if (row < 0 || row >= N || col < 0 || col >= N) break;
                    if (map[row][col] == -1) break;
                    firePowers[row][col] += (int) (fire.max / (Math.pow(2, i)));

                    int num = fireMap[row][col];
                    if (num == 0 || check[num]) continue;
                    Fire otherFire = fires.get(num);
                    if (otherFire.max <= otherFire.power + firePowers[row][col]) {
                        fireQueue.add(num);
                        check[num] = true;
                    }
                }
            }
            fire.power = 0;
        }
    }

    static void dieTurtle() {
        int size = turtles.size();
        for (int i = 1; i <= size; i++) {
            Turtle turtle = turtles.poll();
            if (firePowers[turtle.x][turtle.y] < 20) {
                turtles.add(turtle);
                continue;
            }
            map[turtle.x][turtle.y] = -2;
            died[turtle.num] = true;
        }
    }

    static void envReset() {
        for (int i = 0; i < N; i++) Arrays.fill(firePowers[i], 0);
        for (int i = 1; i <= K; i++) check[i] = false;
    }

    static class Node {
        int x, y, count;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Node(int x, int y, int count) {
            this.x = x;
            this.y = y;
            this.count = count;
        }

        @Override
        public String toString() {
            return "Node{" +
                   "x=" + x +
                   ", y=" + y +
                   ", count=" + count +
                   '}';
        }
    }

    static class Turtle extends Node {
        int num;

        Turtle(int num, int x, int y) {
            super(x, y);
            this.num = num;
        }

        @Override
        public String toString() {
            return "Turtle{" +
                   "num=" + num +
                   ", x=" + x +
                   ", y=" + y +
                   ", count=" + count +
                   '}';
        }
    }

    static class Fire extends Node {
        int power;
        int max;

        Fire(int x, int y, int power, int max) {
            super(x, y);
            this.max = max;
            this.power = power;
        }

        @Override
        public String toString() {
            return "Fire{" +
                   "power=" + power +
                   ", max=" + max +
                   ", x=" + x +
                   ", y=" + y +
                   '}';
        }
    }
}
