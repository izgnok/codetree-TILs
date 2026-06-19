import org.w3c.dom.Node;

import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();


        int N = Integer.parseInt(st.nextToken());
        Node[] arr = new Node[N + 1];
        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            int day = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());
            arr[i] = new Node(day, cost);
        }

        int[] dp = new int[N + 1];
        dp[0] = 0;
        for (int i = 1; i <= N; i++) {
            Node cur = arr[i];
            for(int j = i + cur.day - 1; j <= N; j++) {
                dp[j] = Math.max(dp[j], dp[i - 1] + cur.cost);
            }
        }
        sb.append(dp[N]).append("\n");
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static class Node {
        int day, cost;

        Node(int day, int cost) {
            this.day = day;
            this.cost = cost;
        }
    }
}
