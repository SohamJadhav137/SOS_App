package com.example.sos_app;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AddContactsActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 79;
    private ListView contactsListView;
    private ArrayList<String> contactsList;
    private ArrayList<String> selectedList;
    private ExtendedFloatingActionButton fabAddContacts;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        contactsListView = findViewById(R.id.contactsList);
        fabAddContacts = findViewById(R.id.addContactsFb);
        contactsList = new ArrayList<>();
        selectedList =  new ArrayList<>();

        //check for the permissions and read the contacts if they are given
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddContactsActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        } else {
            readContacts();
        }

        fabAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to main activity
                Intent intent = new Intent(AddContactsActivity.this,MainActivity.class);
                intent.putExtra("contact_list",new Gson().toJson(selectedList));
                startActivity(intent);

            }
        });

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = contactsList.get(position);

                if(selectedList.size()==0){
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                    selectedList.add(selectedItem);
                }
                else if(!selectedList.contains(selectedItem)) {
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                    selectedList.add(selectedItem);
                }
                else {
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    selectedList.remove(selectedItem);
                }
            }
        });
    }

    private void readContacts() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Contact contact = new Contact(name, phoneNumber);
                Log.d("Contacts", contact.toString());
                contactsList.add(contact.toString());
            }
            cursor.close();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
            contactsListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}