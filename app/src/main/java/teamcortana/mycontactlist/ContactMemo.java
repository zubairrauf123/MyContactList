package teamcortana.mycontactlist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ContactMemo extends AppCompatActivity {

    private Contact currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_memo);

        initListButton();
        initSettingsButton();
        initMapButton();
        initTextChangedEvents();
        initSaveButton();
        initToggleButton();

        Bundle extras = getIntent().getExtras();
        if (extras != null ) {
            initMemo(extras.getInt( "contactid" ));
        }
        else {
            currentContact = new Contact();
        }
        setForEditing(false);


    }

    private void initListButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactMemo.this,
                        ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initMapButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonMap);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactMemo.this,
                        ContactMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonSettings);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactMemo.this,
                        ContactSettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setForEditing(editToggle.isChecked());
            }

        });
    }

    private void initTextChangedEvents(){
        final EditText memo = (EditText) findViewById(R.id.editMemo);
        memo.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                currentContact.setMemo(memo.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
    }

    private void setForEditing(boolean enabled) {
        EditText editMemo = (EditText) findViewById(R.id.editMemo);
        Button buttonSave = (Button) findViewById(R.id.buttonSave);

        editMemo.setEnabled(enabled);
        buttonSave.setEnabled(enabled);

        if (enabled) {
            editMemo.requestFocus();
        }
        else {
            ScrollView s = (ScrollView) findViewById(R.id.scrollView1);
            s.fullScroll(ScrollView.FOCUS_UP);
            s.clearFocus();
        }

    }

    private void initSaveButton() {
        Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
                ContactDataSource ds = new ContactDataSource(ContactMemo.this);
                ds.open();

                boolean wasSuccessful = false;
                if (currentContact.getContactID() == -1) {
                    wasSuccessful = ds.insertContact(currentContact);
                    int newId = ds.getLastContactId();
                    currentContact.setContactID(newId);
                } else {
                    wasSuccessful = ds.updateContact(currentContact);
                }
                ds.close();

                if (wasSuccessful) {
                    ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editMemo= (EditText) findViewById(R.id.editMemo);
        imm.hideSoftInputFromWindow(editMemo.getWindowToken(), 0);
    }

    private void initMemo(int id) {

        ContactDataSource ds = new ContactDataSource(ContactMemo.this );
        ds.open();
        currentContact = ds.getSpecificContact(id);
        ds.close();
        EditText editMemo = (EditText) findViewById(R.id.editMemo);
        editMemo.setText(currentContact.getMemo());

        TextView name = (TextView) findViewById(R.id.textName);
        name.setText("Name: " + currentContact.getContactName());

    }
}
