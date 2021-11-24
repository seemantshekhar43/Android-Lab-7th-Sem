package com.seemantshekhar.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.seemantshekhar.reminderapp.Model.EventModel;
import com.seemantshekhar.reminderapp.Repository.Database;
import com.seemantshekhar.reminderapp.Util.DateTimeHelper;
import com.seemantshekhar.reminderapp.Util.InputValidator;
import com.seemantshekhar.reminderapp.Util.Names;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.Calendar;

public class EventActivity extends ChildActivity {

    private Calendar _currentDate;
    private Calendar _currentTime;
    private TextView _eventTimeLabel;
    private TextView _eventDateLabel;
    private ScrollView _eventForm;
    private static final String TAG = "Cannot invoke method length() on null object";
    private EditText _titleInput;
    private EditText _descriptionInput;
    private CheckBox _repeatWeeklyInput;

    private boolean _isEditing;

    private EventModel _eventModel;

    private AsyncTask<Void, Void, String> _task;

    @Override
    protected void initLayouts() {
        this.activityView = R.layout.activity_event;
        this.progressBarLayout = R.id.event_progress;
    }

    @Override
    protected void onCreate() {
        this._eventForm = this.findViewById(R.id.event_form);
        Intent intent = this.getIntent();
        this.setTitle(this.getString(
                R.string.title_activity_event, intent.getStringExtra(Names.EVENT_ACTIVITY_TITLE)
        ));
        this._titleInput = this._eventForm.findViewById(R.id.title);
        this._descriptionInput = this._eventForm.findViewById(R.id.description);
        this._repeatWeeklyInput = this._eventForm.findViewById(R.id.repeat_weekly);
        this._isEditing = intent.getBooleanExtra(Names.IS_EDITING, false);
        if (this._isEditing) {
            loadEvent((long) intent.getSerializableExtra(Names.EVENT_ID));
        } else {
            this._eventModel = new EventModel();
            this.setDefaultDateTime();
            Calendar selectedDate = (Calendar)intent.getSerializableExtra(Names.SELECTED_DATE);
            if (!DateTimeHelper.isToday(selectedDate.getTime()) && System.currentTimeMillis() <= selectedDate.getTimeInMillis()) {
                this._currentDate = selectedDate;
            }
            this.initDateTimeDialogs();
        }
    }

    private void setDefaultDateTime() {
        Calendar calendar = Calendar.getInstance();
        this._currentDate = calendar;
        this._currentTime = calendar;
        this._currentTime.add(Calendar.MINUTE, 3);
    }

    private void initForm(EventModel model) {
        this._eventModel = model;
        this._titleInput.setText(this._eventModel.Title);
        this._descriptionInput.setText(this._eventModel.Description);
        this._repeatWeeklyInput.setChecked(this._eventModel.RepeatWeekly);
        try {
            this._currentDate = DateTimeHelper.parseDate(this._eventModel.Date);
            this._currentTime = DateTimeHelper.parseTime(this._eventModel.Time);
        } catch (ParseException e) {
            e.printStackTrace();
            this.setDefaultDateTime();
        }
        this.initDateTimeDialogs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            this.processSaveEvent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDateTimeDialogs() {
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            _currentTime.set(Calendar.HOUR, hourOfDay);
            _currentTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        };

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            _currentDate.set(Calendar.YEAR, year);
            _currentDate.set(Calendar.MONTH, monthOfYear);
            _currentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        };

        this._eventDateLabel = this._eventForm.findViewById(R.id.date);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EventActivity.this,
                dateSetListener,
                _currentDate.get(Calendar.YEAR),
                _currentDate.get(Calendar.MONTH),
                _currentDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        this._eventDateLabel.setOnClickListener(v -> datePickerDialog.show());
        this._eventTimeLabel = this._eventForm.findViewById(R.id.time);
        this._eventTimeLabel.setOnClickListener(
                v -> new TimePickerDialog(
                        EventActivity.this,
                        timeSetListener,
                        _currentTime.get(Calendar.HOUR),
                        _currentTime.get(Calendar.MINUTE), true
                ).show()
        );
        setInitialDateTime();
    }

    private void setInitialDateTime() {
        this._eventDateLabel.setText(
                DateUtils.formatDateTime(
                        this,
                        _currentDate.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                )
        );
        this._eventTimeLabel.setText(
                DateUtils.formatDateTime(
                        this,
                        _currentTime.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                )
        );
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = this.getResources().getInteger(android.R.integer.config_shortAnimTime);
        this._eventForm.setVisibility(show ? View.GONE : View.VISIBLE);
        this._eventForm.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        _eventForm.setVisibility(show ? View.GONE : View.VISIBLE);
                        if (!show) {
                            _eventForm.requestFocus();
                        }
                    }
                }
        );
        if (show) {
            this.showProgressBar();
        } else {
            this.hideProgressBar();
        }
    }

    private void processSaveEvent() {
        if (this._task != null) {
            return;
        }

        this._titleInput.setError(null);
        this._descriptionInput.setError(null);

        String title = this._titleInput.getText().toString();
        String description = this._descriptionInput.getText().toString();
        Boolean repeatWeekly = this._repeatWeeklyInput.isChecked();

        boolean cancel = false;
        View focusView = null;

        if (InputValidator.isEmpty(title)) {
            this._titleInput.setError(getString(R.string.error_field_required));
            focusView = this._titleInput;
            cancel = true;
        }

        Calendar time = (Calendar) this._currentTime.clone();
        time.set(
                this._currentDate.get(Calendar.YEAR),
                this._currentDate.get(Calendar.MONTH),
                this._currentDate.get(Calendar.DAY_OF_MONTH)
        );

        if (DateTimeHelper.isPast(time.getTime())) {
            Toast.makeText(getBaseContext(), getString(R.string.invalid_creation_time), Toast.LENGTH_LONG).show();
            cancel = true;
        }

        if (cancel) {
            if (focusView != null) {
                focusView.requestFocus();
            }
        } else {
            this.showProgress(true);
            this._eventModel.Title = title;
            this._eventModel.Time = DateTimeHelper.formatTime(this._currentTime.getTimeInMillis());
            this._eventModel.Date = DateTimeHelper.formatDate(this._currentDate.getTimeInMillis());
            this._eventModel.Description = description;
            this._eventModel.RepeatWeekly = repeatWeekly;
            saveEvent(_eventModel, _isEditing);
            this.showProgress(false);
            onBackPressed();
        }
    }



    private void saveEvent(EventModel model, boolean isEditing){
        Database database = Database.getInstance();
        if(model != null){
            if(isEditing){
                for(EventModel prev: database.getEventModelList()){
                    if(prev.Id == model.Id){
                        prev.Title = model.Title;
                        prev.Date = model.Date;
                        prev.Time = model.Time;
                        prev.Description = model.Description;
                        prev.RepeatWeekly = model.RepeatWeekly;
                        break;
                    }

                }
            }else{
                long idLong = System.currentTimeMillis();

                model.Id = idLong;
                database.getEventModelList().add(model);
                Log.d(TAG, "saveEvent: called");

            }
        }
    }

    private void loadEvent(long id){
        Database database = Database.getInstance();
        EventModel eventModel = database.getEventById(id);
        initForm(eventModel);
    }

}