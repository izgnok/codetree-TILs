

import org.w3c.dom.Node;

import java.sql.Array;
import java.util.*;
import java.io.*;

public class Main {

    static int N, L, R;
    static int[][] map;
    static boolean[][] visited;
    static boolean stop;
    static int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    static Queue<Node> eggs;
    static Queue<Node> moveEgg;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken());
        map = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        stop = false;
        eggs = new ArrayDeque<>();
        moveEgg = new ArrayDeque<>();
        int result = 0;

        while (true) {
            stop = true;
            visited = new boolean[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (visited[i][j]) continue;
                    eggMove(i, j);
                }
            }
            if (stop) break;
            result++;
        }
        sb.append(result);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void eggMove(int x, int y) {
        eggs.offer(new Node(x, y, map[x][y]));
        visited[x][y] = true;
        int sum = 0;
        int count = 0;

        while (!eggs.isEmpty()) {
            Node cur = eggs.poll();
            sum += cur.cost;
            count++;
            moveEgg.offer(cur);

            for (int[] dir : dirs) {
                int row = cur.x + dir[0];
                int col = cur.y + dir[1];
                if (row < 0 || row >= N || col < 0 || col >= N) continue;
                if (visited[row][col]) continue;

                int dist = Math.abs(cur.cost - map[row][col]);
                if (dist < L || dist > R) continue;
                visited[row][col] = true;
                eggs.offer(new Node(row, col, map[row][col]));
            }
        }

        if (count > 1) stop = false;
        while (!moveEgg.isEmpty()) {
            Node cur = moveEgg.poll();
            map[cur.x][cur.y] = sum / count;
        }
    }

    static class Node {
        int x, y, cost;

        Node(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "Node{" +
                   "x=" + x +
                   ", y=" + y +
                   ", cost=" + cost +
                   '}';
        }
    }
}
