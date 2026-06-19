import org.w3c.dom.Node;

import java.util.*;
import java.io.*;

public class Main {

    static int N, M;
    static int[][] map;
    static int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // 북, 동, 남, 서 (시계방향)
    static boolean[][] visited;

    static Node car;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new int[N][M];
        visited = new boolean[N][M];

        st = new StringTokenizer(br.readLine());
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int dir = Integer.parseInt(st.nextToken());
        car = new Node(x, y, dir);
        visited[x][y] = true;

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        simulation();
        int sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (visited[i][j]) sum++;
            }
        }
        sb.append(sum).append("\n");
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void simulation() {
        int count = 0;
        // 좌회전 후 전진
        while (count < 4) {
            car.dir = (car.dir + 3) % 4; // 좌회전 (시계방향 배열이므로 -1 == +3)
            int row = car.x + dirs[car.dir][0];
            int col = car.y + dirs[car.dir][1];
            if (row >= 0 && row < N && col >= 0 && col < M && map[row][col] != 1 && !visited[row][col]) {
                car.x = row;
                car.y = col;
                visited[row][col] = true;
                count = 0;
            } else {
                count++;
            }
            /// 후진
            if (count == 4) {
                int backDir = (car.dir + 2) % 4;
                row = car.x + dirs[backDir][0];
                col = car.y + dirs[backDir][1];
                if (row < 0 || row >= N || col < 0 || col >= M || map[row][col] == 1) return;
                car.x = row;
                car.y = col;
                visited[row][col] = true;
                count = 0;
            }
        }
    }

    static class Node {
        int x, y, dir;

        Node(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }
    }

}
