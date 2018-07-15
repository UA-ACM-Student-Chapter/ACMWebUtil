# ACM Web Utility

## Documentation
https://ua-acm-student-chapter.github.io/backend_docs.html

## Use it for your student organization
[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)
__Deployment Guide__ (*coming soon*)

## About
**What the heck is this for?**

* It powers our website to make stuff like joining and paying dues possible
* It carries out automated tasks like sending dues reminders

**Through what sorcery is this achieved?**

* The Spring framework makes it easy to make a REST API in Java, so we used that
* We went relational for our database and used PostgreSQL

**Can't I wreak havoc on UA ACM with knowledge of this code??**

* Hopefully not. Our API is totally public, but any important requests are protected by an authorization key, and payments have special verification to make sure you can't just add yourself as a paid member :)