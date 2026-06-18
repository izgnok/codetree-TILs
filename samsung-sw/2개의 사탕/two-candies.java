import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int N, M;
    static int result;
    static char[][] map;
    static Node end;

    static int direct[][] = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    static boolean flag;
    static boolean gameSet;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new char[N][M];
        Node red = null, blue = null;
        for (int i = 0; i < N; i++) {
            String str = br.readLine();
            for (int j = 0; j < M; j++) {
                map[i][j] = str.charAt(j);
                if (map[i][j] == 'R') {
                    red = new Node(i, j);
                    map[i][j] = '.';
                }
                if (map[i][j] == 'B') {
                    blue = new Node(i, j);
                    map[i][j] = '.';
                }
                if (map[i][j] == '0') {
                    end = new Node(i, j);
                }
            }
        }
        result = Integer.MAX_VALUE;
        dfs(0, -1, red, blue);
        if (result == Integer.MAX_VALUE)
            result = -1;
        sb.append(result);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void dfs(int depth, int k, Node Red, Node Blue) {
        Node red = new Node(Red.x, Red.y);
        Node blue = new Node(Blue.x, Blue.y);

        if (depth > 0) {
            flag = false;
            gameSet = false;
            move(k, red, blue);
        }
        if (gameSet) {
            return;
        }
        if (flag) {
            if (result > depth)
                result = depth;
            return;
        }
        if (depth == 10) {
            return;
        }
        if (depth >= result) {
            return;
        }

        for (int i = 0; i < 4; i++) { // 상, 하 , 좌, 우 선
            dfs(depth + 1, i, red, blue);
        }
    }

    static void move(int k, Node Red, Node Blue) {
        int red_row = Red.x;
        int red_col = Red.y;
        int blue_row = Blue.x;
        int blue_col = Blue.y;

        boolean red_end = false;
        boolean blue_end = false;

        while (true) {
            if (blue_end && red_row == blue_row && red_col == blue_col && map[red_row][red_col] != 'O') {
                red_end = true;
                red_row -= direct[k][0];
                red_col -= direct[k][1];
            }
            if (red_end && red_row == blue_row && red_col == blue_col && map[red_row][red_col] != 'O') {
                blue_end = true;
                blue_row -= direct[k][0];
                blue_col -= direct[k][1];
            }

            if (map[red_row][red_col] == '#') {
                red_end = true;
                red_row -= direct[k][0];
                red_col -= direct[k][1];
                if (blue_end && red_row == blue_row && red_col == blue_col) {
                    red_end = true;
                    red_row -= direct[k][0];
                    red_col -= direct[k][1];
                }
            }

            if (map[blue_row][blue_col] == '#') {
                blue_end = true;
                blue_row -= direct[k][0];
                blue_col -= direct[k][1];
                if (red_end && red_row == blue_row && red_col == blue_col) {
                    blue_end = true;
                    blue_row -= direct[k][0];
                    blue_col -= direct[k][1];
                }

            }

            if (map[red_row][red_col] == 'O') {
                flag = true;
                red_end = true;
            }

            if (map[blue_row][blue_col] == 'O') {
                gameSet = true;
                blue_end = true;
            }

            if (!red_end) {
                red_row += direct[k][0];
                red_col += direct[k][1];
            }
            if (!blue_end) {
                blue_row += direct[k][0];
                blue_col += direct[k][1];
            }
            if (red_row < 0 || red_row >= N || red_col < 0 || red_col >= M) {
                red_end = true;
                red_row -= direct[k][0];
                red_col -= direct[k][1];
            }
            if (blue_row < 0 || blue_row >= N || blue_col < 0 || blue_col >= M) {
                blue_end = true;
                blue_row -= direct[k][0];
                blue_col -= direct[k][1];
            }

            if (red_end && blue_end) {
                break;
            }
        }

        Red.x = red_row;
        Red.y = red_col;
        Blue.x = blue_row;
        Blue.y = blue_col;
    }

    static class Node {
        int x, y;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return "x: " + x + "y: " + y;
        }
    }

}