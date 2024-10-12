import java.util.*;
import java.io.*;

public class Main {

	static int N, M, P, C, D;
	static int[][] map;

	static int[] score;

	static Node[] santas;
	static Node rudolp;

	static int out_count;

	static int[][] dir = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, -1 }, { -1, 1 }, { 1, 1 }, { 1, -1 } };

	public static void main(String[] args) throws IOException {
		// 여기에 코드를 작성해주세요.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		map = new int[N + 1][N + 1];
		M = Integer.parseInt(st.nextToken());
		P = Integer.parseInt(st.nextToken());
		score = new int[P + 1];
		C = Integer.parseInt(st.nextToken());
		D = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(br.readLine());
		int rudolp_x = Integer.parseInt(st.nextToken());
		int rudolp_y = Integer.parseInt(st.nextToken());
		map[rudolp_x][rudolp_y] = -1;
		rudolp = new Node(-1, rudolp_x, rudolp_y);

		santas = new Node[P + 1];
		for (int i = 0; i < P; i++) {
			st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			int santa_x = Integer.parseInt(st.nextToken());
			int santa_y = Integer.parseInt(st.nextToken());
			santas[id] = new Node(id, santa_x, santa_y);
			map[santa_x][santa_y] = id;
		}

		out_count = 0;
		while (M-- > 0) {
			rudolpMove();
			if (out_count == P)
				break;
			santaSimulation();
			if (out_count == P)
				break;
			addScoreAndMoveInit();
		}

		for (int i = 1; i <= P; i++) {
			sb.append(score[i]).append(" ");
		}
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}

	static void rudolpMove() {
		// 가장 가까운 산타 선택 ( 여러명이면 r좌표가 크고, 그것도 여러명이면 c좌표가 큰 곳으로)
		int min = Integer.MAX_VALUE;
		int max_x = 0;
		int max_y = 0;
		int target = 0;
		for(int i=1; i<=P; i++) {
			Node santa = santas[i];
			if(santa.out) continue;
			
			int x = rudolp.x - santa.x;
			int y = rudolp.y - santa.y;
			int dist = x*x + y*y;
			
			if(min > dist) {
				min = dist;
				max_x = santa.x;
				max_y = santa.y;
				target = i;
			} else if(min == dist) {
				if(max_x < santa.x) {
					target = i;
					max_x = santa.x;
					max_y = santa.y;
				} else if(max_x == santa.x) {
					if(max_y < santa.y) {
						target = i;
						max_y = santa.y;
					}
				}
					
			}
		}

		// 이동 ( 충돌체크 )
		Node santa = santas[target];
		int x = rudolp.x - santa.x;
		int y = rudolp.y - santa.y;
		int dist = x * x + y * y;
		int direct = -1;
		for(int i=0; i<8; i++) {
			int row = rudolp.x + dir[i][0];
			int col = rudolp.y + dir[i][1];
			
			if(row < 1 || row > N || col < 1 || col > N) continue;
			
			int tmp_x = row - santa.x;
			int tmp_y = col - santa.y;
			int tmp_dist = tmp_x * tmp_x + tmp_y * tmp_y;
			if(dist > tmp_dist) {
				dist = tmp_dist;
				direct = i;
			}
		}
		
		if(direct == -1) return;
		int row = rudolp.x + dir[direct][0];
		int col = rudolp.y + dir[direct][1];
		
		map[rudolp.x][rudolp.y] = 0;
		rudolp.x = row;
		rudolp.y = col;
		if(map[row][col] > 0) {
			map[row][col] = -1;
			santa.check = 2;
			conflictMove(santa, direct, false);
		}
		map[row][col] = -1;
	}

	static void santaSimulation() {

		for (int i = 1; i <= P; i++) {
			Node santa = santas[i];
			if (santa.out)
				continue;
			
			if (santa.check > 0) {
				santa.check--;
				continue;
			}
			santa.move = true;
			
			int x = rudolp.x - santa.x;
			int y = rudolp.y - santa.y;
			int dist = x*x + y*y;
			int direct = -1;
			// 가장 가까운 이동 방향 찾기 
			for(int k=0; k<4; k++) {
				int row = santa.x + dir[k][0];
				int col = santa.y + dir[k][1];
				
				if(row < 1 || row > N || col < 1 || col > N) continue;
				if(map[row][col] > 0) continue;
				
				int tmp_x = rudolp.x - row;
				int tmp_y = rudolp.y - col;
				int tmp_dist = tmp_x * tmp_x + tmp_y * tmp_y;
				if(dist > tmp_dist) {
					dist = tmp_dist;
					direct = k;
				}
			}
			// 이동하기 
			if(direct == -1) continue;	
			santaMove(santa, direct);
		}
	}

	static void santaMove(Node santa, int direct) {
		
		int row = santa.x + dir[direct][0];
		int col = santa.y + dir[direct][1];
		
		if(row < 1 || row > N || col < 1 || col > N) {
			out_count++;
			santa.out = true;
			return;
		}
		
		if(map[row][col] > 0) {
			int next = map[row][col];
			map[santa.x][santa.y] = 0;
			map[row][col] = santa.id;
			santa.x = row;
			santa.y = col;
			santaMove(santas[next], direct);
			map[row][col] = santa.id;
		} else if(map[row][col] < 0) {
			if(santa.move) santa.check = 1; 
			else santa.check = 2;
			map[santa.x][santa.y] = 0;
			santa.x = row;
			santa.y = col;
			conflictMove(santa, direct, true);
		} else {
			map[santa.x][santa.y] = 0;
			map[row][col] = santa.id;
			santa.x = row;
			santa.y = col;
		}
		
	}
	
	static void conflictMove(Node santa, int direct, boolean who) {	
		
		int row = santa.x;
		int col = santa.y;
		
		if(who) {
			direct = changeDir(direct);
			row += dir[direct][0] * D;
			col += dir[direct][1] * D;
			score[santa.id] += D;
		} else {
			row += dir[direct][0] * C;
			col += dir[direct][1] * C;
			score[santa.id] += C;
		}
		
		
		if(row < 1 || row > N || col < 1 || col > N) {
			out_count++;
			santa.out = true;
			return;
		}
		
		if(map[row][col] > 0) {
			int next = map[row][col];
			map[row][col] = santa.id;
			santa.x = row;
			santa.y = col;
			santaMove(santas[next], direct);
			map[row][col] = santa.id;
		} else if(map[row][col] == 0){
			map[row][col] = santa.id;
			santa.x = row;
			santa.y = col;
		}
	}
	
	static int changeDir(int dir) {
		if(dir == 0) return 2;
		else if(dir == 1) return 3;
		else if(dir == 2) return 0;
		else return 1;
	}

	static void addScoreAndMoveInit() {
		for (int i = 1; i <= P; i++) {
			Node santa = santas[i];
			if (santa.out)
				continue;
			score[i]++;
			santa.move = false;
		}
	}

	static class Node {
		int id, x, y;
		int check;
		boolean out;
		boolean move;

		Node(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "Node [id=" + id + ", x=" + x + ", y=" + y + ", check=" + check + ", out=" + out + "]";
		}

	}
}