package com.example.borhanuddin.getlocation;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Contacts_activity extends AppCompatActivity {

    ListView lv;
    ArrayAdapter<String> adapter;
    CRUD crud=new CRUD();
    Dialog d;
    String number="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_activity);

        lv= (ListView) findViewById(R.id.lv);

        crud.save("01952515672");
        adapter=new ArrayAdapter<String>(Contacts_activity.this,android.R.layout.simple_list_item_1,crud.getNames());
        lv.setAdapter(adapter);




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(d != null) {
                    if(!d.isShowing())
                    {
                        displayInputDialog(i);
                    }else
                    {
                        d.dismiss();
                    }
                }
            }
        });

        final Button floatingActionButton=(Button)findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0 ; i<adapter.getCount() ; i++){

                        number += adapter.getItem(i);
                        if((i<adapter.getCount()-1)){number+=",";}

                }
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("puzzle", number);
                startActivity(i);

            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayInputDialog(-1);
            }
        });
    }
    private void displayInputDialog(final int pos)
    {
        d=new Dialog(this);
        d.setTitle("Self Security");
        d.setContentView(R.layout.input_dialog);

        final EditText nameEditTxt= (EditText) d.findViewById(R.id.nameEditText);
        Button addBtn= (Button) d.findViewById(R.id.addBtn);
        Button updateBtn= (Button) d.findViewById(R.id.updateBtn);
        Button deleteBtn= (Button) d.findViewById(R.id.deleteBtn);

        if(pos== -1)
        {
            addBtn.setEnabled(true);
            updateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }else
        {
            addBtn.setEnabled(true);
            updateBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
            nameEditTxt.setText(crud.getNames().get(pos));
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET DATA
                String name=nameEditTxt.getText().toString();

                //VALIDATE
                if(name.length()>0 && name != null)
                {

                    //save
                    crud.save(name);
                    nameEditTxt.setText("");
                    adapter=new ArrayAdapter<String>(Contacts_activity.this,android.R.layout.simple_list_item_1,crud.getNames());
                    lv.setAdapter(adapter);

                }else
                {
                    Toast.makeText(Contacts_activity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET DATA
                String newName=nameEditTxt.getText().toString();

                //VALIDATE
                if(newName.length()>0 && newName != null)
                {
                    //save
                    if(crud.update(pos,newName))
                    {
                        nameEditTxt.setText(newName);
                        adapter=new ArrayAdapter<String>(Contacts_activity.this,android.R.layout.simple_list_item_1,crud.getNames());
                        lv.setAdapter(adapter);
                    }

                }else
                {
                    Toast.makeText(Contacts_activity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DELETE
                if( crud.delete(pos))
                {
                    nameEditTxt.setText("");
                    adapter=new ArrayAdapter<String>(Contacts_activity.this,android.R.layout.simple_list_item_1,crud.getNames());
                    lv.setAdapter(adapter);
                }
            }
        });

        d.show();
    }
}
