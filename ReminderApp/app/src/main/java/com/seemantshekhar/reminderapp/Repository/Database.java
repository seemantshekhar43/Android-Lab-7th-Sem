package com.seemantshekhar.reminderapp.Repository;

import com.seemantshekhar.reminderapp.Model.EventModel;
import com.seemantshekhar.reminderapp.Util.DateTimeHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database {
    public static  Database database;
    List<EventModel> eventModelList;

    private Database(){
        eventModelList = new ArrayList<>();
    }

    public static Database getInstance(){
        if(database == null){
            database = new Database();
        }
        return database;
    }



    public List<EventModel> getEventModelList() {
        return eventModelList;
    }

    public void setEventModelList(List<EventModel> eventModelList) {
        this.eventModelList = eventModelList;
    }

    public List<EventModel> getByDate(Date date){
        String formattedDate = DateTimeHelper.formatDate(date);
        List<EventModel> list = new ArrayList<>();
        for(EventModel eventModel: this.eventModelList){
            if(eventModel.Date.equals(formattedDate)){
                list.add(eventModel);
            }
        }
        return list;
    }

    public EventModel getEventById(long id){
        for(EventModel model: eventModelList){
            if(model.Id == id){
                return model;
            }
        }

        return new EventModel();
    }

    public EventModel deleteById(Long selectedEvent) {

        for(int i = 0; i < eventModelList.size(); i++){
            if(eventModelList.get(i).Id == selectedEvent){
                return eventModelList.remove(i);

            }
        }
        return new EventModel();
    }
}
