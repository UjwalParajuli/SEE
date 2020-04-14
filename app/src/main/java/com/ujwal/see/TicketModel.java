package com.ujwal.see;

import java.io.Serializable;

public class TicketModel implements Serializable {
    int event_id, organizer_id, total_people, total_tickets, ticket_number, total_purchased_tickets;
    String user_name, purchased_date, event_name, city, venue, start_date, end_date, start_time, end_time, event_category, event_description, event_image, ticket_required, organizer_name;
    double cost_per_ticket, total_cost;

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getOrganizer_id() {
        return organizer_id;
    }

    public void setOrganizer_id(int organizer_id) {
        this.organizer_id = organizer_id;
    }

    public int getTotal_people() {
        return total_people;
    }

    public void setTotal_people(int total_people) {
        this.total_people = total_people;
    }

    public int getTotal_tickets() {
        return total_tickets;
    }

    public void setTotal_tickets(int total_tickets) {
        this.total_tickets = total_tickets;
    }

    public int getTicket_number() {
        return ticket_number;
    }

    public void setTicket_number(int ticket_number) {
        this.ticket_number = ticket_number;
    }

    public int getTotal_purchased_tickets() {
        return total_purchased_tickets;
    }

    public void setTotal_purchased_tickets(int total_purchased_tickets) {
        this.total_purchased_tickets = total_purchased_tickets;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPurchased_date() {
        return purchased_date;
    }

    public void setPurchased_date(String purchased_date) {
        this.purchased_date = purchased_date;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEvent_category() {
        return event_category;
    }

    public void setEvent_category(String event_category) {
        this.event_category = event_category;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }

    public String getTicket_required() {
        return ticket_required;
    }

    public void setTicket_required(String ticket_required) {
        this.ticket_required = ticket_required;
    }

    public String getOrganizer_name() {
        return organizer_name;
    }

    public void setOrganizer_name(String organizer_name) {
        this.organizer_name = organizer_name;
    }

    public double getCost_per_ticket() {
        return cost_per_ticket;
    }

    public void setCost_per_ticket(double cost_per_ticket) {
        this.cost_per_ticket = cost_per_ticket;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public TicketModel(int event_id, int organizer_id, int total_people, int total_tickets, int ticket_number, int total_purchased_tickets, String user_name, String purchased_date, String event_name, String city, String venue, String start_date, String end_date, String start_time, String end_time, String event_category, String event_description, String event_image, String ticket_required, String organizer_name, double cost_per_ticket, double total_cost) {
        this.event_id = event_id;
        this.organizer_id = organizer_id;
        this.total_people = total_people;
        this.total_tickets = total_tickets;
        this.ticket_number = ticket_number;
        this.total_purchased_tickets = total_purchased_tickets;
        this.user_name = user_name;
        this.purchased_date = purchased_date;
        this.event_name = event_name;
        this.city = city;
        this.venue = venue;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.event_category = event_category;
        this.event_description = event_description;
        this.event_image = event_image;
        this.ticket_required = ticket_required;
        this.organizer_name = organizer_name;
        this.cost_per_ticket = cost_per_ticket;
        this.total_cost = total_cost;
    }
}
