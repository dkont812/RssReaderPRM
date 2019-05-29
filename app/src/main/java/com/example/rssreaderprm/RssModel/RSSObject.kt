package com.example.rssreaderprm.RssModel

data class RSSObject(val status: String, val feed: Feed, val items: List<Item>)