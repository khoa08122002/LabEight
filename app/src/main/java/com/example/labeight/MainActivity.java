package com.example.labeight;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
public class MainActivity extends AppCompatActivity {
    Button bt_read,bt_write;
    EditText et_input;
    EditText et_result;
    Button bt_saveDB;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_input=(EditText)findViewById(R.id.editTextTextMultiLine);
        et_result=(EditText)findViewById(R.id.editTextTextMultiLine2);
        bt_read=(Button)findViewById(R.id.buttonRead);
        bt_write=(Button)findViewById(R.id.buttonWrite);
        bt_saveDB=(Button)findViewById(R.id.buttonSaveDB);
        bt_write.setOnClickListener(view -> {
            String noteInput=et_input.getText().toString();
            SharedPreferences sharedPrefWrite= getSharedPreferences("MyPreferences",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPrefWrite.edit();
            editor.putString("note", noteInput);
            editor.apply(); //Update data don't return anything
            //editor.commit(); //Update data return:True/False
        });
        bt_read.setOnClickListener(view -> {
            SharedPreferences sharedPref= getSharedPreferences("MyPreferences",
                    MODE_PRIVATE);
            String noteContent=sharedPref.getString("note", "Your note");
            et_result.setText(noteContent);

        });
        //When click SaveDB button, create new Database if the first time we launch app
// or don't have any content before
        bt_saveDB.setOnClickListener(view -> {
            String noteInput=et_input.getText().toString();
            LabDatabase db=new LabDatabase(this);
            if(db.countRecord()==0)
            {
                db.createNote("First note");
                db.createNote("Second note");
                db.createNote("Third note");
            }
            else{
                db.createNote(noteInput); // save your note
            }
            // After save note into DB, show them in Result
            Cursor c=db.getAllNotes();
            c.moveToFirst();
            String noteContent="";
            do
            {
                noteContent+=c.getString(0)+ " ";
                noteContent+=c.getString(1)+"\n";
            }while(c.moveToNext());
            et_result.setText(noteContent);
            db.close();
            c.close();
        });

    }
}