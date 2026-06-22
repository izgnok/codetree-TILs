import org.w3c.dom.Node;

import java.util.*;
import java.io.*;

public class Main {

    static int INF = 987654321;
    static int N, M;
    static int[][] map;
    static int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    static List<Node> fires;
    static boolean[][] checked;
    static boolean[][] visited;

    static int wallCount;
    static int min;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        wallCount = 3;
        map = new int[N][M];
        fires = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                if (map[i][j] == 2) fires.add(new Node(i, j));
                else if (map[i][j] == 1) wallCount++;
            }
        }

        min = INF;
        checked = new boolean[N][M];
        dfs(0, 0);
        sb.append(N * M - wallCount - min);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void dfs(int start, int checkCount) {
        if (checkCount == 3) {
            int count = bfs();
            if (min > count) min = count;
            return;
        }
        for (int idx = start; idx < N * M; idx++) {
            int i = idx / M;
            int j = idx % M;
            if (map[i][j] != 0 || checked[i][j]) continue;
            
            checked[i][j] = true;
            dfs(idx + 1, checkCount + 1);
            checked[i][j] = false;
        }
    }

    static int bfs() {

        Queue<Node> q = new ArrayDeque<>();
        visited = new boolean[N][M];
        int count = fires.size();

        for (Node fire : fires) {
            q.add(fire);
            visited[fire.x][fire.y] = true;
        }

        while (!q.isEmpty()) {
            Node fire = q.poll();

            for (int[] dir : dirs) {
                int row = fire.x + dir[0];
                int col = fire.y + dir[1];
                if (row < 0 || row >= N || col < 0 || col >= M) continue;
                if (map[row][col] != 0 || checked[row][col] || visited[row][col]) continue;
                visited[row][col] = true;
                q.add(new Node(row, col));
                if (++count >= min) return INF;
            }
        }
        return count;
    }

    static class Node {
        int x, y;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
