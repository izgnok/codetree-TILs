import java.util.*;
import java.io.*;

public class Main {

    static int N, M, T;
    static int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    static int[][] map;
    static List<Node> winds;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());
        map = new int[N][M];
        winds = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                if (map[i][j] == -1) winds.add(new Node(i, j));
            }
        }

        while (T-- > 0) {
            //먼지 확산
            spread();

            //돌풍 청소
            clean();
        }

        int sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (map[i][j] == -1) continue;
                sum += map[i][j];
            }
        }
        sb.append(sum);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void spread() {
        int[][] delta = new int[N][M];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (map[i][j] <= 0) continue;
                int share = map[i][j] / 5;
                for (int[] dir : dirs) {
                    int row = i + dir[0];
                    int col = j + dir[1];
                    if (row < 0 || row >= N || col < 0 || col >= M) continue;
                    if (map[row][col] == -1) continue;
                    delta[row][col] += share;
                    delta[i][j] -= share;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (map[i][j] == -1) continue;
                map[i][j] += delta[i][j];
            }
        }
    }

    static void clean() {

        // 윗칸 (시계방향)
        Node upWind = winds.get(0);
        for (int i = upWind.x - 1; i > 0; i--) {
            map[i][0] = map[i - 1][0];
        }
        for (int j = 0; j < M - 1; j++) {
            map[0][j] = map[0][j + 1];
        }
        for (int i = 0; i < upWind.x; i++) {
            map[i][M - 1] = map[i + 1][M - 1];
        }
        for (int j = M - 1; j > 0; j--) {
            map[upWind.x][j] = map[upWind.x][j - 1];
        }
        map[upWind.x][1] = 0;

        // 아래칸 (반시계방향)
        Node downWind = winds.get(1);
        for (int i = downWind.x + 1; i < N - 1; i++) {
            map[i][0] = map[i + 1][0];
        }
        for(int j=0; j<M-1; j++) {
            map[N-1][j] = map[N-1][j+1];
        }
        for(int i=N-1; i>downWind.x; i--) {
            map[i][M-1] = map[i-1][M-1];
        }
        for(int j=M-1; j>0; j--) {
            map[downWind.x][j] = map[downWind.x][j-1];
        }
        map[downWind.x][1] = 0;
    }


    static class Node {
        int x, y;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

}
