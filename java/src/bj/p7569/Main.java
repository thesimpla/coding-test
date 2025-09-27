package bj.p7569;
//https://www.acmicpc.net/problem/7569

import java.io.*;
import java.util.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) throws IOException {

        int res =0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int m = Integer.parseInt(st.nextToken());
        int n = Integer.parseInt(st.nextToken());
        int h = Integer.parseInt(st.nextToken());

        Queue<int[]> q = new LinkedList<>();

        int check_notvalid = 0;

        // 배열(y,x)/ (0,0) (0,1) (0,2) -> 나 (x,y)/ (0,0) (1,0) (2,0)
        int [][][] map = new int[m][n][h];
        int [][][] dim = new int[m][n][h]; // 차수 확인
        boolean [][][] visited = new boolean[m][n][h]; // 방문여부 확인
        for(int k=0; k<h; k++) {
            for(int j =0; j <n; j++) { // y는 n
                // System.out.println("");
                st = new StringTokenizer(br.readLine());
                for (int i = 0; i < m; i++) { // x 는 m

                    map[i][j][k] = Integer.parseInt(st.nextToken());
                    //System.out.print(map[i][j][k]+" ");
                    if(map[i][j][k]==-1) { // 토마토가 들어있지 않음.
                        visited[i][j][k] = true; //방문할 수 없으므로, 기방문 처리
                        dim[i][j][k] = -1; // 차수 -1 넣기
                    }

                    if(map[i][j][k]==1) { // 토마토가 들어있음.
                        dim[i][j][k] = 1; // 차수 1로 고정
                        q.offer(new int[] {i,j,k});
                        visited[i][j][k] =true;
                    }

                    if(map[i][j][k]==-1||map[i][j][k]==1) {
                        check_notvalid++;
                    }
                }
            }
        }

        //System.out.println("check_notvalid("+check_notvalid+")");
        if (check_notvalid == n*m*h) { // 토마토가 모두 익어있는 상태
            res = 0;
            System.out.println(res);
        } else {

            // 입력 받기 m,n,h
            // m,n,h짜리 배열 만들기
            // 배열에 값 입력 받기
            // 입력 받을 떄, -1이 있으면 visited를 true로 설정하기
            // 값이 모두 1이거나 -1이면 결과를 0으로 출력(모두익은 상태)
            // 이것도 3차원으로 bfs
            // 3개짜리 for문 만들고 끝까지 돌리기 - 1인 값을 찾고, 그 값을 기준으로 bfs를 돌린다..
            // 인접 차수로 가야할듯. 1일차에 익는 토마토, 2일차에 익는 토마토, 3일차에 익는 토마토..
            // 그럼에도 안 익는 토마토가 있으면 -1을 출력
            // 인접 차수 별로, 모든 토마토가 익었는지 확인하려면, 마지막에 0인 토마토가 있는지 확인.

            // 조건문으로 전부 한번씩 돌고나서, 또 돌 필요 없이 해야지..

            int[][] dirs = {
                    {-1,0,0},{1,0,0},
                    {0,-1,0},{0,1,0},
                    {0,0,-1}, {0,0,1}
            };

            while (!q.isEmpty()) {
                int[] cur = q.poll();
                int x = cur[0];
                int y = cur[1];
                int z = cur[2];
                int cur_dim = dim[x][y][z];
                //System.out.println("bfs map["+x+"]["+y+"]["+z+"]="+map[x][y][z]+" dim("+dim[x][y][z]+") ");
                //상하좌우앞뒤, 확인
                // 방문여부 확인 후, 방문한 적이 없으면,
                // 차수를 입력했으면 방문 true로 바꿔야하지 않나.


                for(int[] d : dirs ) {
                    int nx = x+d[0];
                    int ny = y+d[1];
                    int nz = z+d[2];

                    if(nx <0 || nx>=m || ny < 0 || ny >=n || nz <0 || nz >=h) continue;

                    if(!visited[nx][ny][nz] && map[nx][ny][nz] ==0) { // 방문한 적이 없으면
                        // 해당 위치의 차수를 cur+1
                        dim[nx][ny][nz] = cur_dim + 1;
                        q.offer(new int[]{nx,ny,nz});
                        visited[nx][ny][nz] = true;
                    }
                }
            }

            // dim sort
            // 0이 있으면 -1을 출력
            // 없으면 최대 값 -1 출력

            int max = 0;
            for (int k = 0; k < h; k++) {
                for (int j = 0; j < n; j++) { // y는 n
                    for (int i = 0; i < m; i++) {
                        int cur = dim[i][j][k];
                        //System.out.println("dim["+i+"]["+j+"]["+k+"]="+cur);
                        if (cur == 0) {
                            System.out.println(-1);
                            return;
                        }
                        if (max < cur) {
                            max = cur;
                        }
                    }
                }
            }
            System.out.println(max-1);

            // 차수를 sort한 뒤에 가장 큰값 -1 하기
            // 처음에 익어있는 값을 1로 설정했으므로 -1 해야함.
        }
    }

}

