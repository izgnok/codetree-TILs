import org.w3c.dom.Node;

import java.util.*;
import java.io.*;

public class Main {

    static int N, M;
    static Node dice;

    static int[][] map;
    static int[][] dirs = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
    static int[] result;

    static int top = 1;
    static int bottom = 2;
    static int east = 3;
    static int west = 4;
    static int front = 5;
    static int back = 6;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        dice = new Node(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        int T = Integer.parseInt(st.nextToken());

        map = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        st = new StringTokenizer(br.readLine());
        result = new int[7];
        while (T-- > 0) {
            int dir = Integer.parseInt(st.nextToken()) - 1;

            int row = dice.x + dirs[dir][0];
            int col = dice.y + dirs[dir][1];
            if (row < 0 || row >= N || col < 0 || col >= M) continue;

            rollDice(dir);
            sb.append(result[top]).append("\n");
            if (map[row][col] != 0) {
                result[bottom] = map[row][col];
                map[row][col] = 0;
            } else {
                map[row][col] = result[bottom];
            }
            dice.x = row;
            dice.y = col;
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void rollDice(int dir) {
        switch (dir) {
            case 0: {
                int tmp = top;
                top = west;
                west = bottom;
                bottom = east;
                east = tmp;
                break;
            }
            case 1: {
                int tmp = top;
                top = east;
                east = bottom;
                bottom = west;
                west = tmp;
                break;
            }
            case 2: {
                int tmp = top;
                top = front;
                front = bottom;
                bottom = back;
                back = tmp;
                break;
            }
            case 3: {
                int tmp = top;
                top = back;
                back = bottom;
                bottom = front;
                front = tmp;
                break;
            }
        }
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
