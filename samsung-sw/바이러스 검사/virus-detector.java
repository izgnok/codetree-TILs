import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        int N = Integer.parseInt(st.nextToken());

        int[] arr = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        int leader = Integer.parseInt(st.nextToken());
        int member = Integer.parseInt(st.nextToken());

        long result = 0;
        for (int num : arr) {

            num = num - leader;
            result += 1;
            if (num < 0) {
                continue;
            }

            result += num / member;
            if (num % member != 0) result++;
        }

        sb.append(result);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

}
