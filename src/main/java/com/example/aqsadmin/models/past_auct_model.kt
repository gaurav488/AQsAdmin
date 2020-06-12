package com.example.aqsadmin.models

class past_auct_model {
    lateinit var auct_id:String
    lateinit var item_name: String
    lateinit var auct_end_date:String
    lateinit var auct_end_time:String
    lateinit var item_amt:String
    lateinit var estimated_end_bid:String
    lateinit var item_description:String
    lateinit var url:String
    constructor( item_name: String,item_description:String,auct_id:String,item_amt:String,url:String,auct_end_date:String,auct_end_time:String,estimated_end_bid:String) {
        this.item_name= item_name
        this.auct_end_date=auct_end_date
        this.auct_end_time=auct_end_time
        this.auct_id=auct_id
        this.item_amt=item_amt
        this.url=url
        this.estimated_end_bid=estimated_end_bid
        this.item_description=item_description
    }
    constructor(){}
}