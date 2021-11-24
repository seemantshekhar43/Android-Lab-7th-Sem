package com.seemantshekhar.reminderapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.events.calendar.views.EventsCalendar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.seemantshekhar.reminderapp.Adapter.EventListAdapter;
import com.seemantshekhar.reminderapp.Model.EventModel;
import com.seemantshekhar.reminderapp.Repository.Database;
import com.seemantshekhar.reminderapp.Util.DateTimeHelper;
import com.seemantshekhar.reminderapp.Util.Globals;
import com.seemantshekhar.reminderapp.Util.Names;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    private EventsCalendar _calendar;
    private SimpleDateFormat _sdf;

    private ListView _eventListView;
    private Calendar _selectedDate;
    private static final String TAG = "MainActivity";

    private Dialog _eventDetailsDialog;
    private Dialog _aboutDialog;

    private Long _selectedEvent;
    private int _selectedEventPosition;

    private EventModel _eventModel;
    private List<EventModel> _eventModelList;

    @Override
    protected void onResume() {
        loadEvents(this._selectedDate.getTime());
        super.onResume();
    }

    @Override
    protected void initialSetup() {
        Context ctx = this.getApplicationContext();
    }

    protected void initLayouts() {
        this.activityView = R.layout.activity_main;
        this.progressBarLayout = R.id.progress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _eventModelList = new ArrayList<>();

        this._eventDetailsDialog = new Dialog(this);
        this._eventListView = findViewById(R.id.event_list);
        this._eventListView.setOnItemClickListener((parent, view, position, arg3) -> {
            this._selectedEvent = Long.valueOf(((TextView) view.findViewById(R.id.event_id)).getText().toString());
            this._selectedEventPosition = position;
        });
        this._eventListView.setOnItemLongClickListener((parent, view, position, id) -> {
            this._selectedEvent = Long.valueOf(((TextView) view.findViewById(R.id.event_id)).getText().toString());
            this._selectedEventPosition = position;
            _eventModel = getEventModel(_selectedEvent);
            showEventDetails(_eventModel);
            return true;
        });


        BottomNavigationView bottomNavigationView = this.findViewById(R.id.event_managing);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_event_add:
                    Intent createActivity = new Intent(MainActivity.this, EventActivity.class);
                    createActivity.putExtra(Names.EVENT_ACTIVITY_TITLE, getString(R.string.create));
                    createActivity.putExtra(Names.SELECTED_DATE, this._selectedDate);
                    this.startActivity(createActivity);
                    break;
                case R.id.action_event_edit:
                    if (this._selectedEvent != null) {
                        Intent editActivity = new Intent(MainActivity.this, EventActivity.class);
                        editActivity.putExtra(Names.EVENT_ACTIVITY_TITLE, getString(R.string.edit));
                        editActivity.putExtra(Names.EVENT_ID, this._selectedEvent);
                        editActivity.putExtra(Names.IS_EDITING, true);
                        this.startActivity(editActivity);
                    } else {
                        Toast.makeText(getBaseContext(), R.string.no_event_to_edit, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.action_event_remove:
                    if (this._selectedEvent != null) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                        adb.setTitle(getString(R.string.delete) + Globals.QUESTION_MARK);
                        adb.setMessage(R.string.event_delete_confirmation);
                        adb.setNegativeButton(R.string.cancel, null);
                        adb.setPositiveButton(Names.OK_CAPS, (dialog, which) -> {
                            EventModel deletedModel = deleteEventModel(this._selectedEvent);
                        });
                        adb.show();
                    } else {
                        Toast.makeText(getBaseContext(), R.string.no_event_to_delete, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        });

        this._calendar = findViewById(R.id.calendar);
        this._calendar.setWeekStartDay(Calendar.MONDAY, false);

        Calendar start = Calendar.getInstance();
        start.set(1900, 1, 1);

        Calendar end = Calendar.getInstance();
        end.set(2100, 12, 31);

        this._selectedDate = Calendar.getInstance();

        this._calendar.setMonthRange(start, end);
        this._calendar.setSelectionMode(this._calendar.getSINGLE_SELECTION());
        this._calendar.setToday(this._selectedDate);
        this._calendar.setCallback(new EventsCalendar.Callback() {
            @Override
            public void onDaySelected(@Nullable Calendar calendar) {
                if (calendar != null) {
                    _selectedEvent = null;
                    _selectedDate = calendar;
                    loadEvents(_selectedDate.getTime());
                }
            }

            @Override
            public void onDayLongPressed(@Nullable Calendar calendar) {}

            @Override
            public void onMonthChanged(@Nullable Calendar calendar) {}
        });

        loadEvents(_selectedDate.getTime());
        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    private EventModel getEventModel(Long selectedEvent) {
        for(EventModel eventModel: _eventModelList){
            if(eventModel.Id == selectedEvent){
                return eventModel;
            }
        }

        return  new EventModel();
    }

    private EventModel deleteEventModel(Long _selectedEvent){
        Database database = Database.getInstance();
        EventModel eventModel = database.deleteById(_selectedEvent);
        loadEvents(_selectedDate.getTime());

        return eventModel;
    }

    private void addEventsToCalendar(List<Calendar> calendars) {
        for (Calendar calendar : calendars) {
            this._calendar.addEvent(calendar);
        }
        this._calendar.postInvalidate();
    }
    private void showEventDetails(EventModel model) {
        this._eventDetailsDialog.setContentView(R.layout.event_details);
        TextView titleView = this._eventDetailsDialog.findViewById(R.id.event_details_title);
        titleView.setText(model.Title);
        TextView descriptionView = this._eventDetailsDialog.findViewById(R.id.event_details_description);
        descriptionView.setText(model.Description);
        TextView dateView = this._eventDetailsDialog.findViewById(R.id.event_details_date);
        dateView.setText(model.Date);
        TextView timeView = this._eventDetailsDialog.findViewById(R.id.event_details_time);
        timeView.setText(model.Time);
        this._eventDetailsDialog.findViewById(R.id.event_details_dismiss).setOnClickListener(
                v -> _eventDetailsDialog.dismiss()
        );
        Objects.requireNonNull(
                this._eventDetailsDialog.getWindow()
        ).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this._eventDetailsDialog.show();
    }

    private List<EventModel> loadEvents(Date _searchDate){
        Log.d(TAG, "loadEvents: date is" + _searchDate);
        Database database = Database.getInstance();
        List<EventModel> list = database.getByDate(_searchDate);
        List<Calendar> calendars = new ArrayList<>();
        for (EventModel event : list) {
            try {
                calendars.add(DateTimeHelper.parseDate(event.Date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        addEventsToCalendar(calendars);
        _eventModelList.clear();
        _eventModelList.addAll(list);
        Log.d(TAG, "loadEvents: " + "length is: " + _eventModelList.size());


        if (_searchDate != null) {
            EventListAdapter adapter = new EventListAdapter(
                    MainActivity.this, R.layout.event_list_item, _eventModelList
            );
            this._eventListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        if (_searchDate == null) {
            List<Calendar> calendars1 = new ArrayList<>();
            for (EventModel event : _eventModelList) {
                try {
                    calendars1.add(DateTimeHelper.parseDate(event.Date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            this.addEventsToCalendar(calendars1);
        }
        return  list;
    }
}