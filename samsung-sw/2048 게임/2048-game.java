import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

public class Main {
    static int result, N;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        
         int[][] arr = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
                if (result < arr[i][j])
                    result = arr[i][j];
            }
        }

        for (int i = 0; i < 4; i++) { // 0: 상, 1:하 , 2:좌 , 3:우
            back_track(i, arr, 1, 2);
        }

        sb.append(String.valueOf(result));
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void back_track(int direc, int[][] cur, int depth, int max) {
        boolean possible = false;
        int[][] tmp = new int[N][N];
        boolean[][] check = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tmp[i][j] = cur[i][j];
            }
        }
        if (direc == 0) {
            for (int i = 1; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (tmp[i][j] == 0)
                        continue;
                    if (tmp[i][j] == tmp[i - 1][j]) { // 숫자가같은경우
                        if (!check[i - 1][j]) {
                            tmp[i - 1][j] = tmp[i][j] * 2;
                            check[i - 1][j] = true;
                            if (max < tmp[i][j] * 2)
                                max = tmp[i][j] * 2;
                            tmp[i][j] = 0;
                        }        
                        int idx = i-1;
                        while (idx - 1 >= 0 && tmp[idx - 1][j] == 0) {
                            tmp[idx - 1][j] = tmp[idx][j];
                            check[idx][j] = false;
                            tmp[idx][j] = 0;
                            idx--;
                        }
                        if (i-1 != idx)
                            check[idx][j] = true;
                        possible = true;
                    } else if (tmp[i - 1][j] == 0) { // 가는방향 숫자가 0인경우
                        int idx = i;
                        while (idx - 1 >= 0 && tmp[idx - 1][j] == 0) {
                            tmp[idx - 1][j] = tmp[idx][j];
                            tmp[idx][j] = 0;
                            idx--;
                        }
                        if (idx - 1 >= 0 && tmp[idx - 1][j] == tmp[idx][j]) {
                            if (!check[idx - 1][j]) {
                                tmp[idx - 1][j] = tmp[idx][j] * 2;
                                check[idx - 1][j] = true;
                                if (max < tmp[idx][j] * 2)
                                    max = tmp[idx][j] * 2;
                                tmp[idx][j] = 0;
                            }
                        }
                        possible = true;
                    } else {
                    }
                }
            }
        } else if (direc == 1) {
            for (int i = N - 2; i >= 0; i--) {
                for (int j = 0; j < N; j++) {
                    if (tmp[i][j] == 0)
                        continue;
                    if (tmp[i][j] == tmp[i + 1][j]) {
                        if (!check[i + 1][j]) {
                            tmp[i + 1][j] = tmp[i][j] * 2;
                            check[i + 1][j] = true;
                            if (max < tmp[i][j] * 2)
                                max = tmp[i][j] * 2;
                            tmp[i][j] = 0;
                        }
                        int idx = i + 1;
                        while (idx + 1 < N && tmp[idx + 1][j] == 0) {
                            tmp[idx + 1][j] = tmp[idx][j];
                            check[idx][j] = false;
                            tmp[idx][j] = 0;
                            idx++;
                        }
                        if (i + 1 != idx)
                            check[idx][j] = true;
                        possible = true;
                    } else if (tmp[i + 1][j] == 0) {
                        int idx = i;
                        while (idx + 1 < N && tmp[idx + 1][j] == 0) {
                            tmp[idx + 1][j] = tmp[idx][j];
                            tmp[idx][j] = 0;
                            idx++;
                        }
                        if (idx + 1 < N && tmp[idx + 1][j] == tmp[idx][j]) {
                            if (!check[idx + 1][j]) {
                                tmp[idx + 1][j] = tmp[idx][j] * 2;
                                check[idx + 1][j] = true;
                                if (max < tmp[idx][j] * 2)
                                    max = tmp[idx][j] * 2;
                                tmp[idx][j] = 0;
                            }
                        }
                        possible = true;
                    } else {
                    }
                }
            }
        } else if (direc == 2) {
            for (int j = 1; j < N; j++) {
                for (int i = 0; i < N; i++) {
                    if (tmp[i][j] == 0)
                        continue;
                    if (tmp[i][j] == tmp[i][j - 1]) {
                        if (!check[i][j - 1]) {
                            tmp[i][j - 1] = tmp[i][j - 1] * 2;
                            check[i][j - 1] = true;
                            if (max < tmp[i][j] * 2)
                                max = tmp[i][j] * 2;
                            tmp[i][j] = 0;
                        }
                        int idx = j-1;
                        while (idx - 1 >= 0 && tmp[i][idx - 1] == 0) {
                            tmp[i][idx - 1] = tmp[i][idx];
                            check[i][idx] = false;
                            tmp[i][idx] = 0;
                            idx--;
                        }
                        if (j-1 != idx)
                            check[i][idx] = true;
                        possible = true;
                    } else if (tmp[i][j - 1] == 0) {
                        int idx = j;
                        while (idx - 1 >= 0 && tmp[i][idx - 1] == 0) {
                            tmp[i][idx - 1] = tmp[i][idx];
                            tmp[i][idx] = 0;
                            idx--;
                        }
                        if (idx - 1 >= 0 && tmp[i][idx - 1] == tmp[i][idx]) {
                            if (!check[i][idx - 1]) {
                                tmp[i][idx - 1] = tmp[i][idx] * 2;
                                check[i][idx - 1] = true;
                                if (max < tmp[i][idx] * 2)
                                    max = tmp[i][idx] * 2;
                                tmp[i][idx] = 0;
                            }
                        }
                        possible = true;
                    } else {
                    }
                }
            }
        } else if (direc == 3) {
            for (int j = N - 2; j >= 0; j--) {
                for (int i = 0; i < N; i++) {
                    if (tmp[i][j] == 0)
                        continue;
                    if (tmp[i][j] == tmp[i][j + 1]) {
                        if (!check[i][j + 1]) {
                            tmp[i][j + 1] = tmp[i][j + 1] * 2;
                            check[i][j + 1] = true;
                            if (max < tmp[i][j] * 2)
                                max = tmp[i][j] * 2;
                            tmp[i][j] = 0;
                        }
                        int idx = j+1;
                        while (idx + 1 < N && tmp[i][idx + 1] == 0) {
                            tmp[i][idx + 1] = tmp[i][idx];
                            check[i][idx] = false;
                            tmp[i][idx] = 0;
                            idx++;
                        }
                        if (j+1 != idx)
                            check[i][idx] = true;
                        possible = true;
                    } else if (tmp[i][j + 1] == 0) {
                        int idx = j;
                        while (idx + 1 < N && tmp[i][idx + 1] == 0) {
                            tmp[i][idx + 1] = tmp[i][idx];
                            tmp[i][idx] = 0;
                            idx++;
                        }
                        if (idx + 1 < N && tmp[i][idx + 1] == tmp[i][idx]) {
                            if (!check[i][idx + 1]) {
                                tmp[i][idx + 1] = tmp[i][idx] * 2;
                                check[i][idx + 1] = true;
                                if (max < tmp[i][idx] * 2)
                                    max = tmp[i][idx] * 2;
                                tmp[i][idx] = 0;
                            }
                        }
                        possible = true;
                    } else {
                    }
                }
            }
        }
        if (result < max)
            result = max;
        if (depth == 5) { // 5번움직였다면 리턴
            return;
        }
        if (!possible)
            return; // 해당 방향으로 움직이지 못했다면 리턴
//        if (max * (2 * Math.pow(2, 5 - depth)) < result)
//            return; // 유망하지않으면 리턴
        for (int i = 0; i < 4; i++) {
            back_track(i, tmp, depth + 1, max);
        }
    }
}