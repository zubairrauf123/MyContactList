package teamcortana.mycontactlist;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ContactActivity extends FragmentActivity implements DatePickerDialog.SaveDateListener {

    public static Contact currentContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initListButton();
        initMapButton();
        initSettingsButton();
        initToggleButton();
        initChangeDateButton();
        initTextChangedEvents();
        initSaveButton();
        initMemoButton();
        initCallFunction();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initContact(extras.getInt("contactid"));
        } else {
            currentContact = new Contact();
        }
        setForEditing(false);

    }

    private void initListButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard();
                Intent intent = new Intent(ContactActivity.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initMapButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonMap);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ContactMapActivity.class);
                if (currentContact.getContactID() == -1) {
                    Toast.makeText(getBaseContext(), "Contact must be saved before it can be" +
                            " mapped", Toast.LENGTH_LONG).show();
                } else {
                    intent.putExtra("contactid", currentContact.getContactID());
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton list = (ImageButton) findViewById(R.id.imageButtonSettings);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ContactSettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setForEditing(editToggle.isChecked());
            }

        });
    }

    private void initSaveButton() {
        Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
                ContactDataSource ds = new ContactDataSource(ContactActivity.this);
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

    private void initMemoButton() {
        Button list = (Button) findViewById(R.id.buttonMemo);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ContactMemo.class);
                intent.putExtra("contactid", currentContact.getContactID());
                startActivity(intent);
            }
        });
    }

    private void initTextChangedEvents() {
        final EditText contactName = (EditText) findViewById(R.id.editName);
        contactName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setContactName(contactName.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                //  TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //  TODO Auto-generated method stub
            }
        });

        final EditText streetAddress = (EditText) findViewById(R.id.editAddress);
        streetAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setStreetAddress(streetAddress.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                //  TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //  TODO Auto-generated method stub
            }
        });

        final EditText city = (EditText) findViewById(R.id.editCity);
        city.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setCity(city.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                //  TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //  TODO Auto-generated method stub
            }
        });

        final EditText state = (EditText) findViewById(R.id.editState);
        state.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setState(state.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                //  TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //  TODO Auto-generated method stub
            }
        });

        final EditText zipcode = (EditText) findViewById(R.id.editZipcode);
        zipcode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setZipCode(zipcode.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                //  TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //  TODO Auto-generated method stub
            }
        });

        final EditText phoneNumber = (EditText) findViewById(R.id.editHome);
        phoneNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setPhoneNumber(phoneNumber.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }
        });

        final EditText cellNumber = (EditText) findViewById(R.id.editCell);
        cellNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setCellNumber(cellNumber.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }
        });

        final EditText eMail = (EditText) findViewById(R.id.editEMail);
        eMail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setEMail(eMail.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                //  TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //  TODO Auto-generated method stub
            }
        });

        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        cellNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private void setForEditing(boolean enabled) {
        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editAddress = (EditText) findViewById(R.id.editAddress);
        EditText editCity = (EditText) findViewById(R.id.editCity);
        EditText editState = (EditText) findViewById(R.id.editState);
        EditText editZipCode = (EditText) findViewById(R.id.editZipcode);
        EditText editPhone = (EditText) findViewById(R.id.editHome);
        EditText editCell = (EditText) findViewById(R.id.editCell);
        EditText editEmail = (EditText) findViewById(R.id.editEMail);
        Button buttonChange = (Button) findViewById(R.id.btnBirthday);
        Button buttonSave = (Button) findViewById(R.id.buttonSave);

        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZipCode.setEnabled(enabled);
        //editPhone.setEnabled(enabled);
        //editCell.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        buttonChange.setEnabled(enabled);
        buttonSave.setEnabled(enabled);

        if (enabled) {
            editName.requestFocus();
            editPhone.setInputType(InputType. TYPE_CLASS_PHONE );
            editCell.setInputType(InputType. TYPE_CLASS_PHONE );
        } else {
            ScrollView s = (ScrollView) findViewById(R.id.scrollView1);
            editPhone.setInputType(InputType. TYPE_NULL );
            editCell.setInputType(InputType. TYPE_NULL );
            s.fullScroll(ScrollView.FOCUS_UP);
            s.clearFocus();
        }

    }

    private void initChangeDateButton() {
        Button changeDate = (Button) findViewById(R.id.btnBirthday);
        changeDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editName = (EditText) findViewById(R.id.editName);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
        EditText editAddress = (EditText) findViewById(R.id.editAddress);
        imm.hideSoftInputFromWindow(editAddress.getWindowToken(), 0);
        EditText editCity = (EditText) findViewById(R.id.editCity);
        imm.hideSoftInputFromWindow(editCity.getWindowToken(), 0);
        EditText editState = (EditText) findViewById(R.id.editState);
        imm.hideSoftInputFromWindow(editState.getWindowToken(), 0);
        EditText editZipCode = (EditText) findViewById(R.id.editZipcode);
        imm.hideSoftInputFromWindow(editZipCode.getWindowToken(), 0);
        EditText editPhone = (EditText) findViewById(R.id.editHome);
        imm.hideSoftInputFromWindow(editPhone.getWindowToken(), 0);
        EditText editCell = (EditText) findViewById(R.id.editCell);
        imm.hideSoftInputFromWindow(editCell.getWindowToken(), 0);
        EditText editEmail = (EditText) findViewById(R.id.editEMail);
        imm.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);
        //EditText editMemo= (EditText) findViewById(R.id.editMemo);
        //imm.hideSoftInputFromWindow(editMemo.getWindowToken(), 0);
    }

    private void initContact(int id) {

        ContactDataSource ds = new ContactDataSource(ContactActivity.this);
        ds.open();
        currentContact = ds.getSpecificContact(id);
        ds.close();
        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editAddress = (EditText) findViewById(R.id.editAddress);
        EditText editCity = (EditText) findViewById(R.id.editCity);
        EditText editState = (EditText) findViewById(R.id.editState);
        EditText editZipCode = (EditText) findViewById(R.id.editZipcode);
        EditText editPhone = (EditText) findViewById(R.id.editHome);
        EditText editCell = (EditText) findViewById(R.id.editCell);
        EditText editEmail = (EditText) findViewById(R.id.editEMail);
        TextView birthDay = (TextView) findViewById(R.id.textBirthday);
        editName.setText(currentContact.getContactName());
        editAddress.setText(currentContact.getStreetAddress());
        editCity.setText(currentContact.getCity());
        editState.setText(currentContact.getState());
        editZipCode.setText(currentContact.getZipCode());
        editPhone.setText(currentContact.getPhoneNumber());
        editCell.setText(currentContact.getCellNumber());
        editEmail.setText(currentContact.getEMail());
        birthDay.setText(DateFormat.format("MM/dd/yyyy",
                currentContact.getBirthday().toMillis(false)).toString());
    }

    private void initCallFunction() {
        EditText editPhone = (EditText) findViewById(R.id.editHome);
        editPhone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                callContact(currentContact.getPhoneNumber());
                return false;
            }
        });
        EditText editCell = (EditText) findViewById(R.id.editCell);
        editCell.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                callContact(currentContact.getCellNumber());
                return false;
            }
        });
    }

    private void callContact(String phoneNumber) {
        Intent smsintent = new Intent(Intent.ACTION_VIEW);
        smsintent.setData(Uri.parse("send sms:" + phoneNumber));
        startActivity(smsintent);
    }



    @Override
    public void didFinishDatePickerDialog(Time selectedTime) {
        TextView birthDay = (TextView) findViewById(R.id.textBirthday);
        birthDay.setText(DateFormat.format("MM/dd/yyyy", selectedTime.toMillis(false)).toString());
    }

}