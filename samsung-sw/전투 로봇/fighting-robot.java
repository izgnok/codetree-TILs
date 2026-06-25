import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static int level, kill, time;
    static int INF = 987654321;
    static int[][] map;
    static int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    static Node robot;
    static Queue<Node>[] monsters;
    static int monsterCount;
    static HashSet<Integer> died;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        map = new int[N][N];

        monsters = new Queue[7];
        for (int i = 1; i < 7; i++) {
            monsters[i] = new ArrayDeque<>();
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());

                if (map[i][j] == 9) {
                    map[i][j] = -1;
                    robot = new Node(i, j);
                } else if (map[i][j] > 0) {
                    monsters[map[i][j]].add(new Node(i, j, ++monsterCount));
                }
            }
        }

        level = 2;
        kill = 0;
        time = 0;
        died = new HashSet<>();
        while (monsterCount > 0) {
            int dist = INF;
            int nextRow = robot.x;
            int nextCol = robot.y;
            int monsterNum = -1;
            for (int i = Math.min(level - 1, 6); i > 0; i--) {
                Queue<Node> monsterQ = monsters[i];
                int size = monsterQ.size();
                for (int j = 0; j < size; j++) {
                    Node monster = monsterQ.poll();
                    if (died.contains(monster.num)) continue;
                    int[] info = bfs(monster, dist, nextRow, nextCol, monsterNum);
                    dist = info[0];
                    nextRow = info[1];
                    nextCol = info[2];
                    monsterNum = info[3];
                    monsterQ.offer(monster);
                }
            }

            if (dist == INF || monsterNum == -1 || (robot.x == nextRow && robot.y == nextCol)) break;
            robot.x = nextRow;
            robot.y = nextCol;
            time += dist;
            died.add(monsterNum);
            if (++kill == level) {
                level++;
                kill = 0;
            }
            monsterCount--;
        }
        sb.append(time);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    static int[] bfs(Node monster, int dist, int nextRow, int nextCol, int monsterNum) {
        Queue<Node> q = new ArrayDeque<>();
        q.offer(monster);

        boolean[][] visited = new boolean[N][N];
        visited[monster.x][monster.y] = true;

        while (!q.isEmpty()) {
            Node cur = q.poll();
            if (cur.x == robot.x && cur.y == robot.y) {
                if (cur.count < dist) {
                    dist = cur.count;
                    nextRow = monster.x;
                    nextCol = monster.y;
                    monsterNum = monster.num;
                } else if (cur.count == dist) {
                    if (nextRow > monster.x || (nextRow == monster.x && nextCol > monster.y)) {
                        nextRow = monster.x;
                        nextCol = monster.y;
                        monsterNum = monster.num;
                    }
                }
                break;
            }

            for (int[] dir : dirs) {
                int row = cur.x + dir[0];
                int col = cur.y + dir[1];
                if (row < 0 || row >= N || col < 0 || col >= N || visited[row][col]) continue;
                if (level < map[row][col]) continue;
                visited[row][col] = true;
                q.offer(new Node(row, col, monster.num, cur.count + 1));
            }
        }
        return new int[]{dist, nextRow, nextCol, monsterNum};
    }


    static class Node {
        int x, y, num, count;

        Node(int x, int y) {
            this(x, y, -1, 0);
        }

        Node(int x, int y, int num) {
            this(x, y, num, 0);
        }

        Node(int x, int y, int num, int count) {
            this.x = x;
            this.y = y;
            this.num = num;
            this.count = count;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ", " + count + ")";
        }
    }

}
