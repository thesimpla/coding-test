package bj.p2667;

// https://www.acmicpc.net/problem/2667

import java.io.*;
import java.util.*;
import java.lang.*;


public class Main {

    public static void main(String[] args) throws IOException{
        // 지도의 크기 n 입력 받기
        // n사이즈의 2차원 int 배열 생성
        // 값 입력 받기

        // 0,0부터 이동하기, 총 반복 횟수는 n*n번
        // 값이 1이면 queue에 넣기 그리고 bfs 사용, 그리고 count 시작
        // queue가 empty이면, 다음 값으로 넘어간다.
        // 그 값의 상하좌우를 조회해서, 방문하지 않았다면 queue에 추가한다.
        // 지점의 상하좌우 값을 얻어온다. (상하좌우의 범위 값을 음수가 되지 않게 하기!)
        // 이미 방문한 값은 재 방문하지 않는다.

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        boolean[][] visited = new boolean[n][n];
        Queue<int[]> q = new LinkedList<>();
        int[][] map = new int[n][n];
        List<Integer> complex = new ArrayList<>();


        for (int i = 0; i < n; i++) {
            String str = br.readLine();
            for (int j = 0; j < n; j++) {
                map[i][j] = str.charAt(j) - '0'; // str은 문자열이므로 "\0" 값이 포함되어있고 그걸 지운다.
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] == 1 && !visited[i][j]) {
                    q.offer(new int[]{i,j});
                    complex.add(bfs(n, map,q,visited));
                }
            }
        }


        Collections.sort(complex);
        //System.out.println(complex);
        //오름 차순
        System.out.println(complex.size());
        for(int i =0; i<complex.size();i++) {
            System.out.println(complex.get(i));
        }

    }


    public static int bfs(int n, int[][] map, Queue<int[]> q, boolean[][] visited) {
        int count =0;
        while(!q.isEmpty()){
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            if (!visited[x][y]) {

                if (x - 1 > -1) { // x-1, y
                    if(map[x-1][y] == 1 && !visited[x-1][y]) {
                        q.offer(new int[]{x - 1, y});
                    }
                }
                if (x + 1 < n) { // x+1, y
                    if(map[x+1][y] == 1 && !visited[x+1][y]) {
                        q.offer(new int[]{x + 1, y});
                    }
                }
                if (y - 1 > -1) {// x, y-1
                    if(map[x][y-1] == 1 && !visited[x][y-1]) {
                        q.offer(new int[]{x, y - 1});
                    }
                }
                if (y + 1 < n) { // x, y+1
                    if(map[x][y+1] == 1 && !visited[x][y+1]) {
                        q.offer(new int[]{x, y + 1});
                    }
                }

                visited[x][y] = true;
                count++;
            }

        }
        return count;
    }

}