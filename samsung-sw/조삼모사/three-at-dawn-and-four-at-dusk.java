import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static int[][] map;

    static int min;
    static boolean[] checked;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(st.nextToken());
        map = new int[N][N];
        min = 0;

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                min += map[i][j];
            }
        }

        checked = new boolean[N];
        dfs(0, 0);
        sb.append(min);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void dfs(int cur, int count) {
        if (count == N / 2) {
            int tmp = calc();
            if (min > tmp) min = tmp;
            return;
        }
        if (cur == N) return;
        if (count + (N - cur) < N / 2) return;
        
        checked[cur] = true;
        dfs(cur + 1, count + 1);
        checked[cur] = false;
        dfs(cur + 1, count);
    }

    static int calc() {

        int morning = 0;
        int evening = 0;

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (checked[i] && checked[j]) {
                    morning += map[i][j];
                    morning += map[j][i];
                } else if (!checked[i] && !checked[j]) {
                    evening += map[i][j];
                    evening += map[j][i];
                }
            }
        }
        return Math.abs(morning - evening);
    }
}
