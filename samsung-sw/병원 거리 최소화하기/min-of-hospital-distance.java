import java.util.*;
import java.io.*;

public class Main {

    static int N, M;
    static int INF = 987654321;

    static List<Node> hospitals;
    static List<Node> humans;
    static int[][] dist;
    static int result;
    static HashSet<Integer> pickHospital;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        humans = new ArrayList<>();
        hospitals = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                int num = Integer.parseInt(st.nextToken());
                if (num == 1) {
                    humans.add(new Node(i, j));
                } else if (num == 2) {
                    hospitals.add(new Node(i, j));
                }
            }
        }

        if (humans.isEmpty()) result = 0;
        else {
            dist = new int[humans.size()][hospitals.size()];
            searchDist();

            result = INF;
            pickHospital = new HashSet<>();
            dfs(0, 0);
        }
        sb.append(result);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void searchDist() {
        for (int i = 0; i < humans.size(); i++) {
            Node human = humans.get(i);
            for (int j = 0; j < hospitals.size(); j++) {
                Node hospital = hospitals.get(j);
                dist[i][j] = Math.abs(human.x - hospital.x) + Math.abs(human.y - hospital.y);
            }
        }
    }

    static void dfs(int idx, int depth) {
        if (depth == M) {
            calc();
            return;
        }
        if (hospitals.size() - idx < M - depth) return;

        for (int i = idx; i < hospitals.size(); i++) {
            pickHospital.add(i);
            dfs(i + 1, depth + 1);
            pickHospital.remove(i);
        }
    }

    static void calc() {
        int sum = 0;
        for (int i = 0; i < humans.size(); i++) {
            int min = INF;
            for (int j : pickHospital) {
                min = Math.min(min, dist[i][j]);
            }
            sum += min;
        }
        result = Math.min(result, sum);
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
