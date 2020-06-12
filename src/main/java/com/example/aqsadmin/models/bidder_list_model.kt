package com.example.aqsadmin.models

class bidder_list_model {

    lateinit var username: String
    //lateinit var current_date:String
    lateinit var current_date_time:String
    lateinit var bid_amount:String
    lateinit var Uid: String
    constructor( username: String,current_date_time:String,bid_amount:String,Uid:String) {
        this.username= username
        this.current_date_time=current_date_time
        // this.current_time=current_time
        this.bid_amount= bid_amount
        this.Uid=Uid
    }
    constructor(){}
}