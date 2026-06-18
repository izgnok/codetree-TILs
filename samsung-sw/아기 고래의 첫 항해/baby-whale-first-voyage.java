import java.util.*;
import java.io.*;

public class Main {

    static int[][] dirs = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}}; // 좌, 하, 우, 상

    static int[][] map; // 0: 바다, 1: 해초
    static boolean[][] visited;
    static Node whale;
    static int N, targetCount, findCount;

    static int INF = 987654321;

    static StringBuilder sb;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());

        findCount = 1;
        targetCount = N * N;
        map = new int[N][N];
        whale = new Node(Integer.parseInt(st.nextToken()) - 1, Integer.parseInt(st.nextToken()) - 1, Integer.parseInt(st.nextToken()));
        if(whale.dir == 1) whale.dir = 3;
        else if(whale.dir == 2) whale.dir = 1;
        else if(whale.dir == 3) whale.dir = 0;
        else whale.dir = 2;

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                if (map[i][j] == 1) targetCount--;
            }
        }

        visited = new boolean[N][N];
        visited[whale.x][whale.y] = true;
        sb = new StringBuilder();
        sb.append(whale.x + 1).append(" ").append(whale.y + 1).append("\n");
        simulation();

        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static void simulation() {
        while (findCount < targetCount) {
            move(); // 인접 바다 이동
            if (findCount == targetCount) break;
            // 가장 가까운 바다 이동
            Node next = searchNext();
            nearMove(next);
        }
    }

    static void move() {
        boolean check = true;
        while (check) {
            int row = whale.x + dirs[whale.dir][0];
            int col = whale.y + dirs[whale.dir][1];

            if (row >= 0 && row < N && col >= 0 && col < N && map[row][col] == 0 && !visited[row][col]) {
                visited[row][col] = true;
                whale.x = row;
                whale.y = col;
                sb.append(whale.x + 1).append(" ").append(whale.y + 1).append("\n");
                findCount++;
                continue;
            }

            int tmp;
            tmp = leftRotate(whale.dir);
            row = whale.x + dirs[tmp][0];
            col = whale.y + dirs[tmp][1];
            if (row >= 0 && row < N && col >= 0 && col < N && map[row][col] == 0 && !visited[row][col]) {
                visited[row][col] = true;
                whale.x = row;
                whale.y = col;
                whale.dir = tmp;
                sb.append(whale.x + 1).append(" ").append(whale.y + 1).append("\n");
                findCount++;
                continue;
            }

            tmp = rightRotate(whale.dir);
            row = whale.x + dirs[tmp][0];
            col = whale.y + dirs[tmp][1];
            if (row >= 0 && row < N && col >= 0 && col < N && map[row][col] == 0 && !visited[row][col]) {
                visited[row][col] = true;
                whale.x = row;
                whale.y = col;
                whale.dir = tmp;
                sb.append(whale.x + 1).append(" ").append(whale.y + 1).append("\n");
                findCount++;
                continue;
            }

            tmp = turnRotate(whale.dir);
            row = whale.x + dirs[tmp][0];
            col = whale.y + dirs[tmp][1];
            if (row >= 0 && row < N && col >= 0 && col < N && map[row][col] == 0 && !visited[row][col]) {
                visited[row][col] = true;
                whale.x = row;
                whale.y = col;
                whale.dir = tmp;
                sb.append(whale.x + 1).append(" ").append(whale.y + 1).append("\n");
                findCount++;
            } else check = false;
        }
    }

    static void nearMove(Node next) {
        int[][] dist = calcDist(next);

        while (whale.x != next.x || whale.y != next.y) {
            int nextRow = -1;
            int nextCol = -1;
            int nextDir = -1;

            for (int d = 0; d < 4; d++) {
                int row = whale.x + dirs[d][0];
                int col = whale.y + dirs[d][1];
                if (row < 0 || row >= N || col < 0 || col >= N) continue;
                if (map[row][col] == 1) continue;
                if (dist[row][col] == dist[whale.x][whale.y] - 1) {
                    nextRow = row;
                    nextCol = col;
                    nextDir = d;
                    break;
                }
            }
            if (!visited[nextRow][nextCol]) {
                findCount++;
                visited[nextRow][nextCol] = true;
                sb.append(nextRow + 1).append(" ").append(nextCol + 1).append("\n");
            }
            whale.x = nextRow;
            whale.y = nextCol;
            whale.dir = nextDir;
        }
    }

    static Node searchNext() {

        Queue<Node> q = new ArrayDeque<>();
        q.offer(new Node(whale.x, whale.y, whale.dir, 0));
        boolean[][] visit = new boolean[N][N];
        visit[whale.x][whale.y] = true;

        int min = INF;
        int nextRow = -1;
        int nextCol = -1;

        while (!q.isEmpty()) {
            Node cur = q.poll();
            if (!visited[cur.x][cur.y]) {
                if (min > cur.count) {
                    min = cur.count;
                    nextRow = cur.x;
                    nextCol = cur.y;
                } else if (min == cur.count) {
                    if (nextRow > cur.x || (nextRow == cur.x && nextCol > cur.y)) {
                        nextRow = cur.x;
                        nextCol = cur.y;
                    }
                }
            }

            for (int[] dir : dirs) {
                int row = cur.x + dir[0];
                int col = cur.y + dir[1];
                if (row < 0 || row >= N || col < 0 || col >= N) continue;
                if (map[row][col] == 1) continue;
                if (visit[row][col]) continue;

                visit[row][col] = true;
                q.offer(new Node(row, col, cur.dir, cur.count + 1));
            }
        }
        return new Node(nextRow, nextCol);
    }

    static int[][] calcDist(Node next) {

        Queue<Node> q = new ArrayDeque<>();
        q.offer(new Node(next.x, next.y, next.dir, 0));

        int[][] dist = new int[N][N];
        for (int i = 0; i < N; i++) Arrays.fill(dist[i], INF);

        boolean[][] visit = new boolean[N][N];
        visit[next.x][next.y] = true;

        while (!q.isEmpty()) {
            Node cur = q.poll();
            dist[cur.x][cur.y] = cur.count;
            if (cur.x == whale.x && cur.y == whale.y) break;

            for (int[] dir : dirs) {
                int row = cur.x + dir[0];
                int col = cur.y + dir[1];
                if (row < 0 || row >= N || col < 0 || col >= N) continue;
                if (map[row][col] == 1) continue;
                if (visit[row][col]) continue;
                visit[row][col] = true;
                q.offer(new Node(row, col, cur.dir, cur.count + 1));
            }
        }
        return dist;
    }

    static int leftRotate(int dir) {
        return (dir + 1) % 4;
    }

    static int rightRotate(int dir) {
        return (dir + 3) % 4;
    }

    static int turnRotate(int dir) {
        return (dir + 2) % 4;
    }

    static class Node {
        int x, y, dir, count;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Node(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        Node(int x, int y, int dir, int count) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.count = count;
        }


        @Override
        public String toString() {
            return "Node{" +
                   "x=" + x +
                   ", y=" + y +
                   ", dir=" + dir +
                   ", count=" + count +
                   '}';
        }
    }
}
