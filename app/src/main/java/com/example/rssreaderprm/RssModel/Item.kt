package com.example.rssreaderprm.RssModel

data class Item(
    var title: String,
    val pubDate: String,
    val link: String,
    val author: String,
    val thumbnail: String,
    val description: String,
    val content:String,
    val enclosure : Object,
    val categories : List<String>)