import java.util.*;
import java.io.*;

public class Main {

    static int N;

    static int[] arr;
    static int[] op; // 1: 덧셈, 2: 뺼셈, 3: 곱셈

    static int[] opCount;

    static int min, max;
    static int INF = 1000000000;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(st.nextToken());
        arr = new int[N];
        op = new int[N];
        opCount = new int[4];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i < 4; i++) {
            opCount[i] = Integer.parseInt(st.nextToken());
        }

        max = -1000000000;
        min = INF;

        dfs(1, arr[0]);
        sb.append(min).append(" ").append(max);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void dfs(int depth, int sum) {
        if (depth == N) {
            min = Math.min(min, sum);
            max = Math.max(max, sum);
            return;
        }

        for (int i = 1; i <= 3; i++) {
            if (opCount[i] <= 0) continue;
            op[depth] = i;
            opCount[i]--;
            if (op[depth] == 1) dfs(depth + 1, sum + arr[depth]);
            else if (op[depth] == 2) dfs(depth + 1, sum - arr[depth]);
            else dfs(depth + 1, sum * arr[depth]);
            opCount[i]++;
        }
    }
}
