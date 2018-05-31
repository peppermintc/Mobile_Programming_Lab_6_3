package org.androidtown.lab_6_3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText name;
    EditText student_number;
    Button add_btn;
    Button delete_btn;
    ListView listView;

    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    String[] listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.editText1);
        student_number = findViewById(R.id.editText2);
        add_btn = findViewById(R.id.button1);
        delete_btn = findViewById(R.id.button2);
        listView = findViewById(R.id.listView1);

        helper = new MySQLiteOpenHelper(MainActivity.this, "person.db", null, 1);

        //추가버튼 리스너
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_str = name.getText().toString();
                String student_number_str = student_number.getText().toString();

                if(!(name_str.equals(""))&&!(student_number_str.equals("")))
                {
                    //DB에 추가
                    insert(name_str,student_number_str);
                    //리스트뷰 갱신
                    invalidate();
                    //리셋
                    name.getText().clear();
                    student_number.getText().clear();
                }
                else
                {
                    //미입력시 토스트 메시지
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //삭제 리스너
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_str = name.getText().toString();
                String student_number_str = name.getText().toString();

                if(!(student_number_str.equals(""))){
                    //DB에서 제거
                    delete(name_str);
                    //리스트뷰 갱신
                    invalidate();
                    //리셋
                    name.getText().clear();
                    student_number.getText().clear();
                }
                else{
                    //이름 미입력시 토스트메시지
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //DB 추가
    public void insert(String name, String student_number){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("student_number", student_number);

        db.insert("student",null, values);
    }

    //DB 제거
    public void delete (String name) {
        db = helper.getWritableDatabase();
        db.delete("student", "name=?", new String[]{name});
    }

    //Listview에 DB동기화
    public void select() {
        db = helper.getReadableDatabase();
        Cursor c = db.query("student",null,null,null,null,null,null);

        listItems = new String[c.getCount()];
        int count = 0;
        while(c.moveToNext()) {

            listItems[count] = c.getString(c.getColumnIndex("name")) + " " + c.getString(c.getColumnIndex("student_number"));
            count++;
        }
        c.close();
    }

    //리스트뷰 갱신
    private void invalidate(){
        select();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listItems);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}


