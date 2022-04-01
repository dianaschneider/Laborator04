package ro.pub.cs.systems.eim.lab04.contactsmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {
    private Button showFieldsButton;
    private EditText nameEditText;
    private EditText numberEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private Button saveButton;
    private Button cancelButton;
    private LinearLayout additionalFieldsLayout;
    private EditText jobTitleEditText;
    private EditText companyEditText;
    private EditText websiteEditText;
    private EditText imEditText;
    private final String SHOW_FIELDS = "Show Additional Fields";
    private final String HIDE_FIELDS = "Hide Additional Fields";
    private int show_fields = View.INVISIBLE;
    private ClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);
        numberEditText = (EditText) findViewById(R.id.numberEditText);
        //Get intent data
        Intent intent = getIntent();
        if (intent != null) {
            String phoneNumber = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phoneNumber != null) {
                numberEditText.setText(phoneNumber);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }

        showFieldsButton = (Button) findViewById(R.id.SHOW);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        saveButton = (Button) findViewById(R.id.SAVE);
        cancelButton = (Button) findViewById(R.id.CANCEL);
        additionalFieldsLayout = (LinearLayout) findViewById(R.id.ADDITIONAL_FIELDS_LAYOUT);
        jobTitleEditText = (EditText) findViewById(R.id.jobTitleEditText);
        companyEditText = (EditText) findViewById(R.id.companyEditText);
        websiteEditText = (EditText) findViewById(R.id.websiteEditText);
        imEditText = (EditText) findViewById(R.id.imEditText);
        clickListener = new ClickListener();

        showFieldsButton.setText(SHOW_FIELDS);
        additionalFieldsLayout.setVisibility(show_fields);

        //initialize buttons
        showFieldsButton.setOnClickListener(clickListener);
        saveButton.setOnClickListener(clickListener);
        cancelButton.setOnClickListener(clickListener);

    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.SHOW:
                    if (show_fields == View.VISIBLE) {
                        //the fields will become invisible
                        show_fields = View.INVISIBLE;
                        showFieldsButton.setText(SHOW_FIELDS);
                        additionalFieldsLayout.setVisibility(show_fields);
                    } else {
                        //the fields will become visible
                        show_fields = View.VISIBLE;
                        showFieldsButton.setText(HIDE_FIELDS);
                        additionalFieldsLayout.setVisibility(show_fields);
                    }
                    break;
                case R.id.SAVE:
                    //create the intent
                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                    //extract needed fields from edit texts
                    String name = nameEditText.getText().toString();
                    String phone = numberEditText.getText().toString();
                    String email = emailEditText.getText().toString();
                    String address = addressEditText.getText().toString();
                    String jobTitle = jobTitleEditText.getText().toString();
                    String company = companyEditText.getText().toString();
                    String website = websiteEditText.getText().toString();
                    String im = imEditText.getText().toString();

                    //add extra fields to intent
                    if (name != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                    }
                    if (phone != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                    }
                    if (email != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                    }
                    if (address != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                    }
                    if (jobTitle != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
                    }
                    if (company != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                    }
                    ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
                    if (website != null) {
                        ContentValues websiteRow = new ContentValues();
                        websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                        websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                        contactData.add(websiteRow);
                    }
                    if (im != null) {
                        ContentValues imRow = new ContentValues();
                        imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                        imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                        contactData.add(imRow);
                    }
                    intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);

                    //launch the intent
                    startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
                    break;
                case R.id.CANCEL:
                    setResult(Activity.RESULT_CANCELED, new Intent());
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case Constants.CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}