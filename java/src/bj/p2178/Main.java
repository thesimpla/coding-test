package bj.p2178;

// https://www.acmicpc.net/problem/2178

import java.io.*;
import java.util.*;
import java.lang.*;


public class Main {
    public static void main(String[] args) throws IOException {

        //n,m을 입력받아 map 만들기 int[][]
        //미로 map을 입력 받아, 값 채우기

        // n 차수에 갈 수 있는 곳을 차수 맵에 표시하기로 풀기
        // DFS

        // DFS로 풀기 start는 (1,1)이고 해당 지점의 dim은 1에서 시작
        // 큐가 empty일 때까지, 큐가 empty인데, m,n 이 안나왔으면, 미로가 성립하지 않음.
        // poll 값(x,y)가 m,n이면 차수 map의 m,n 을 리턴한다.
        // 아니면, dfs의 파라미터로 넣어 호출한다.
        // dfs에서는 x,y의 visited를 true로 변경한다.
        // 상하좌우 범위 체크해서 1이면 차수 map에 자기 자신값 +1 을 넣고 큐에 해당 값을 넣는다.


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        Queue<int[]> q = new LinkedList<>();

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        int[][] map = new int[m][n];
        int[][] dim = new int[m][n];
        boolean[][] visited = new boolean[m][n];


        for (int i =0; i<n; i++){
            String str = br.readLine();
            for(int j=0; j<m;j++) {
                map[j][i] = str.charAt(j)-'0';
            }
        }

        q.offer(new int[]{0,0});
        visited[0][0] =true;
        dim[0][0]=1;

        while(!q.isEmpty()) {
            bfs(m,n,map, dim,q, visited);
        }
        System.out.println(dim[m-1][n-1]);

    }

    public static void bfs(int m, int n,int[][] map, int[][] dim, Queue<int[]> q, boolean[][] visited) {


        int[] cur = q.poll();
        int x = cur[0];
        int y = cur[1];

        //System.out.println("bfs("+x+", "+y+") dim("+dim[x][y]+")");

        if(x+1<= m-1) { // x+1,y
            if (map[x + 1][y] == 1 && !visited[x + 1][y]) {
                dim[x+1][y] = dim[x][y]+1;
                q.offer(new int[]{x+1,y});
                visited[x+1][y] =true;
            }
        }

        if (x-1>=0) {//x-1,y
            if (map[x - 1][y] == 1 && !visited[x - 1][y]) {
                dim[x-1][y] = dim[x][y]+1;
                q.offer(new int[]{x-1,y});
                visited[x-1][y] =true;
            }
        }

        if(y+1 <= n-1) {//x, y+1
            if(map[x][y+1]==1 && !visited[x][y+1]) {
                dim[x][y+1] = dim[x][y]+1;
                q.offer(new int[]{x,y+1});
                visited[x][y+1] =true;
            }
        }

        if(y-1 >=0) { // x, y-1
            if(map[x][y-1]==1 && !visited[x][y-1]) {
                dim[x][y-1] = dim[x][y]+1;
                q.offer(new int[]{x,y-1});
                visited[x][y-1] =true;
            }
        }
    }

}