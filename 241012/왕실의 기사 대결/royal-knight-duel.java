import java.io.*;
import java.util.*;

public class Main {
	static int L, N;

	static int[][] map;
	static int[][] gisaMap;
	
	static Node[] gisaList;
	
	static int[][] dir = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
	
	static List<Integer> moveList;
	static HashSet<Integer> moveSet;

	public static void main(String[] args) throws IOException {
		// 여기에 코드를 작성해주세요.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine());

		L = Integer.parseInt(st.nextToken());
		int Q = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());

		map = new int[L + 1][L + 1];
		for (int i = 1; i <= L; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j <= L; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		gisaList = new Node[N+1];
		for (int i = 1; i <= N; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			gisaList[i] = new Node(x, y, h, w, k);
		}
		initGisaMap();

		while (Q-- > 0) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			int direct = Integer.parseInt(st.nextToken());
			// 게임 진행 
			if(gisaList[num].k <= 0) continue;
			moveList = new ArrayList<>();
			moveSet = new HashSet<>();
			boolean check = moveCheck(num, direct);
			if(check) {
				move(direct);
				initGisaMap();
				setDamage(num);
				initGisaMap();
			}
		}
		
		int sum = 0;
		for(int i=1; i<=N; i++) {
			if(gisaList[i].k <= 0) continue;
			sum += gisaList[i].damage;
		}
		sb.append(sum);
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}
	
	static boolean moveCheck(int num, int direct) {
		// 이동
		Node gisa = gisaList[num];
		
		int row = gisa.x;
		int col = gisa.y;

		//옮길수있는지 먼저 판별 
		for(int i= row; i < row+gisa.h; i++) {
			for(int j=col; j<col+gisa.w; j++) {
				int next_row = i + dir[direct][0];
				int next_col = j + dir[direct][1];
				
				if(next_row < 1 || next_row > L || next_col < 1 || next_col > L) {
					return false;
				}
				
				if(map[next_row][next_col] == 2) {
					return false;
				}
				
				int next_num = gisaMap[next_row][next_col];
				if(next_num > 0 && next_num != num) {
					boolean check = moveCheck(next_num, direct);
					if(!check) return false;
				}
			}
		}
		moveList.add(num);
		moveSet.add(num);
		return true;
	}
	
	static void move(int direct) {
		for(int i=0; i<moveList.size(); i++) {
			int num = moveList.get(i);
			gisaList[num].x = gisaList[num].x + dir[direct][0];
			gisaList[num].y = gisaList[num].y + dir[direct][1];
		}
	}
	
	static void setDamage(int num) {
		
		for(int i=1; i<=L; i++) {
			for(int j=1; j<=L; j++) {
				if(map[i][j] == 0) continue;
				if(map[i][j] == 2) continue;
				if(gisaMap[i][j] == 0) continue;
				if(gisaMap[i][j] == num) continue;
				
				int damageNum = gisaMap[i][j];
				if(!moveSet.contains(damageNum)) continue;
				gisaList[damageNum].k--;
				gisaList[damageNum].damage++;
			}
		}
		
	}
	
	
	
	static void initGisaMap() {
		gisaMap = new int[L+1][L+1];
		for(int n=1; n<=N; n++) {
			Node gisa = gisaList[n];
			
			if(gisa.k <= 0) continue;
			
			int row = gisa.x;
			int col = gisa.y;
			
			for(int i = row; i < row + gisa.h; i++) {
				for(int j=col; j < col + gisa.w; j++) {
					gisaMap[i][j] = n;
				}
			}
		}
	}

	static class Node {
		int x, y, h, w, k, damage;

		public Node(int x, int y, int h, int w, int k) {
			super();
			this.x = x;
			this.y = y;
			this.h = h;
			this.w = w;
			this.k = k;
		}

		@Override
		public String toString() {
			return "Node [x=" + x + ", y=" + y + ", h=" + h + ", w=" + w + ", k=" + k + ", damage=" + damage + "]";
		}
	}
}