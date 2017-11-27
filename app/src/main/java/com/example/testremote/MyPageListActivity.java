package com.example.testremote;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MyPageListActivity extends AppCompatActivity {


    ListView listview ;
    ListViewMyClassAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_list);
        init();
    }
    public void init(){



        // Adapter 생성
        adapter = new ListViewMyClassAdapter() ;
        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.list_mypagelistclass);
        listview.setAdapter(adapter);
        showClassList();

        //리스트뷰 이벤트 처리. 눌르면 해당 정보를 데이터베이스에서 로드하기.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewMyClass item = (ListViewMyClass) parent.getItemAtPosition(position) ;

                String titleStr = item.getTitle() ;
                String descStr = item.getDesc() ;
                Drawable iconDrawable = item.getIcon();

                //마이페이지 로딩 액티비티 전환
                Toast.makeText(getApplicationContext(),titleStr,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);


            }
        }) ;



        //
        //new InsertDataTask().execute("http://sanhyup.c4hbycfv6pky.ap-northeast-2.rds.amazonaws.com:5306/pushData");
        //
        // new InsertDataTask().execute("http://13.124.85.122:52273/pushData");

    }

    public void showClassList(){

        //내 디비 정보 받아와서
        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.presentation),
                "다 함께 배우는 배드민턴", "기초 클래스로 배트민턴 수업입니다.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.presentation),
                "빠르게 배우는 바둑 교실", "기초 클래스로 바둑 수업입니다.") ;
        // 두 번째 아이템 추가.
        //adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_account_circle_black_36dp),
        //        "Circle", "Account Circle Black 36dp") ;
        // 세 번째 아이템 추가.
        //adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_assignment_ind_black_36dp),
        //        "Ind", "Assignment Ind Black 36dp") ;

        adapter.notifyDataSetChanged();

    }

    public void myclass(View view) {

        Toast.makeText(getApplicationContext(),"dd",Toast.LENGTH_LONG);
        //사용자 디비에서 수강중이나 강의중인 강의 클래스 보여주고 선택할 수 있도록 하기. 여러개인경우

        //txt_mypage.setText("내 활동");
        //txt_mypage.setTextSize(20);
        //txt_mypage.setBackgroundResource(R.color.Dcolor);
        showClassList();

        //Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
        //startActivity(intent);

    }

    public void homeBtn(View v){
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
    }

//    public class InsertDataTask extends AsyncTask<String,Integer,String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            try{
//                return downloadUrl((String) urls[0]);
//            }catch (IOException e){
//                e.printStackTrace();
//                return "다운로드 실패";
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            if(s.equals("InsertOK")){
//                Log.d("ddd","ddddddddddddddddddddddddddddddd");
//                new DownloadWebPageTask().execute("http://13.124.85.122:52273/search");
//            }
//        }
//
//        public String downloadUrl(String myurl) throws IOException{
//            HttpURLConnection conn = null;
//            String response = "InsertFailed";
//            try{
//                URL url = new URL(myurl);
//                conn = (HttpURLConnection)url.openConnection();
//                Log.d("ddd","ddddddddddddddddddddddddddddddd");
//                conn.setRequestMethod("POST");
//                Log.d("ddd","ddddddddddddddddddddddddddddddd2");
//                conn.setRequestProperty("Content-Type","application/json");
//                conn.setDoOutput(true);
//                JSONObject job = new JSONObject();
//                job.put("ID", "jackie0304");
//                job.put("PW", "1127");
//                job.put("Name", "안동현");
//                OutputStream os  = conn.getOutputStream();
//                os.write(job.toString().getBytes());
//                Log.d("ddd","ddddddddddddddddddddddddddddddd3");
//                os.flush();
//
//                int responseCode = conn.getResponseCode();
//                Log.d("ddd","ddddddddddddddddddddddddddddddd4");
//                if(responseCode == HttpURLConnection.HTTP_OK){
//                    response = "InsertOK";
//                }
//
//            }catch (JSONException e){
//                Log.d("ddd","ddddddddddddddddddddddddddddddd3");
//                e.printStackTrace();
//
//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
//            }finally {
//                conn.disconnect();
//            }
//
//            return  response;
//        }
//
//    }
//
//    public class DownloadWebPageTask extends AsyncTask < String, Integer, String>{
//
//        @Override
//        protected String doInBackground(String... urls) {
//            try{
//                return (String)downdloadUrl((String) urls[0]);
//            }catch(IOException e){
//                e.printStackTrace();
//                return "다운로드 실패";
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            jsonParsing(s);
//        }
//
//
//        public String downdloadUrl(String myurl) throws IOException{
//            HttpURLConnection conn = null;
//            try{
//                URL url = new URL(myurl);
//                conn = (HttpURLConnection) url.openConnection();
//                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
//                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf,"utf-8"));
//                String line = null;
//                String page = "";
//                while ((line = bufreader.readLine()) != null) {
//                    page += line;
//                }
//                Log.i("page : " , page);
//                return page;
//            }finally {
//                conn.disconnect();
//            }
//        }
//
//        void jsonParsing (String file) {
//
//            try {
//               JSONArray jsonArray = new JSONArray(file);
//                for ( int i = 0; i <jsonArray.length(); i++){
//                    JSONObject json = jsonArray.getJSONObject(i);
//                    String id = json.getString("ID");
//                    String name = json.getString("PW");
//                    String quantity = json.getString("Name");
//                }
//            }catch (Exception e) {
//                Log.i("err :", e.getMessage());
//            }
//        }
//
//
//    }
}
