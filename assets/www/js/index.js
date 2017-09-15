/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    // deviceready Event Handler
    //
    // Bind any cordova events here. Common events are:
    // 'pause', 'resume', etc.
    onDeviceReady: function() {
        this.receivedEvent('deviceready');
    },

    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

app.initialize();



document.getElementById("button_start").addEventListener('click',function(){
alert("connecting to session");

     var message_obj =   "{"
        	+'"ApiKey": "success",'
        	+'"SessionId": "asdf",'
        	+'"Token": "asdf",'
        	+'"ProfileImage": "asdf",'
        	+'"UserName": "asdf",'
        	+'"MessageData": {'
        		+'"MessageBody": "hello this is a new message",'
        		+'"MessageType": "sendTextMessage",'
        		+'"ConnectionData": {'
        			+'"connectionId": "7302F7C1-F13B-48F7-A291-FB4FD7BE4592",'
        			+'"creationTime": "1505382391231000",'
        			+'"data": "asdf"'
        		+'}'
        	+'}'
        +'}'


TokBoxPhonegapPlugin.connectToSession(message_obj,
    function(success){
        alert("success");
    },
    function(error){
        alert("error");
    });
});



document.getElementById("button_call").addEventListener('click',function(){
alert("hello");

var apiKey = '45953692';
        var sessionId = '1_MX40NTk1MzY5Mn5-MTUwNTIyODYyOTI1MX5DK055b0tyZUxQTm1uaVlxcFVIbGwxalh-fg';
        var token = 'T1==cGFydG5lcl9pZD00NTk1MzY5MiZzaWc9MmIyN2Q0NTY0YTU3ODA2ZTJhZGUwMGEwMzY5ZjBjMzgxMTEyMzM5MzpzZXNzaW9uX2lkPTFfTVg0ME5UazFNelk1TW41LU1UVXdOVEl5T0RZeU9USTFNWDVESzA1NWIwdHlaVXhRVG0xdWFWbHhjRlZJYkd3eGFsaC1mZyZjcmVhdGVfdGltZT0xNTA1MjI4NjkxJm5vbmNlPTAuOTU4NjY3MDY1NjE3NzQyMSZyb2xlPW1vZGVyYXRvciZleHBpcmVfdGltZT0xNTA3ODIwNjkwJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9';
        var obj = '{'
        +'"ApiKey":"45598312",'
        +'"SessionId":"1_MX40NTk1MzY5Mn5-MTUwNTIyODYyOTI1MX5DK055b0tyZUxQTm1uaVlxcFVIbGwxalh-fg",'
        +'"Token":"T1==cGFydG5lcl9pZD00NTk1MzY5MiZzaWc9MmIyN2Q0NTY0YTU3ODA2ZTJhZGUwMGEwMzY5ZjBjMzgxMTEyMzM5MzpzZXNzaW9uX2lkPTFfTVg0ME5UazFNelk1TW41LU1UVXdOVEl5T0RZeU9USTFNWDVESzA1NWIwdHlaVXhRVG0xdWFWbHhjRlZJYkd3eGFsaC1mZyZjcmVhdGVfdGltZT0xNTA1MjI4NjkxJm5vbmNlPTAuOTU4NjY3MDY1NjE3NzQyMSZyb2xlPW1vZGVyYXRvciZleHBpcmVfdGltZT0xNTA3ODIwNjkwJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9",'
        +'"UserType":"Pro",'
        +'"IsAbleToCall":"true",'
        +'"ProfileImage":"http://igert2012.videohall.com/images/defaults/small_default_profile.png",'
        +'"UserName":"Abella",'
        +'"CallPerMinute":"2",'
        +'"Amount":"22.30",'
        +'"isReceiverInit":"false"'
        +'}';
        var a = JSON.parse(obj);
        console.log("parseee :" + jsonObj);
        var jsonObj = JSON.stringify(eval("(" + obj + ")"));
        console.log("jkahsdjkah = " + jsonObj);


TokBoxPhonegapPlugin.initializeVideoCall(jsonObj,
    function(success){
        alert("success");
    },
    function(error){
        alert("error");
    });
});




document.getElementById("button_chat").addEventListener('click',function(){
alert("hello");

var apiKey = '45953692';
        var sessionId = '1_MX40NTk1MzY5Mn5-MTUwNTIyODYyOTI1MX5DK055b0tyZUxQTm1uaVlxcFVIbGwxalh-fg';
        var token = 'T1==cGFydG5lcl9pZD00NTk1MzY5MiZzaWc9MmIyN2Q0NTY0YTU3ODA2ZTJhZGUwMGEwMzY5ZjBjMzgxMTEyMzM5MzpzZXNzaW9uX2lkPTFfTVg0ME5UazFNelk1TW41LU1UVXdOVEl5T0RZeU9USTFNWDVESzA1NWIwdHlaVXhRVG0xdWFWbHhjRlZJYkd3eGFsaC1mZyZjcmVhdGVfdGltZT0xNTA1MjI4NjkxJm5vbmNlPTAuOTU4NjY3MDY1NjE3NzQyMSZyb2xlPW1vZGVyYXRvciZleHBpcmVfdGltZT0xNTA3ODIwNjkwJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9';
        var obj = '{'
        +'"ApiKey":"45598312",'
        +'"SessionId":"1_MX40NTk1MzY5Mn5-MTUwNTIyODYyOTI1MX5DK055b0tyZUxQTm1uaVlxcFVIbGwxalh-fg",'
        +'"Token":"T1==cGFydG5lcl9pZD00NTk1MzY5MiZzaWc9MmIyN2Q0NTY0YTU3ODA2ZTJhZGUwMGEwMzY5ZjBjMzgxMTEyMzM5MzpzZXNzaW9uX2lkPTFfTVg0ME5UazFNelk1TW41LU1UVXdOVEl5T0RZeU9USTFNWDVESzA1NWIwdHlaVXhRVG0xdWFWbHhjRlZJYkd3eGFsaC1mZyZjcmVhdGVfdGltZT0xNTA1MjI4NjkxJm5vbmNlPTAuOTU4NjY3MDY1NjE3NzQyMSZyb2xlPW1vZGVyYXRvciZleHBpcmVfdGltZT0xNTA3ODIwNjkwJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9",'
        +'"UserType":"Pro",'
        +'"IsAbleToCall":"true",'
        +'"ProfileImage":"http://igert2012.videohall.com/images/defaults/small_default_profile.png",'
        +'"UserName":"Abella",'
        +'"CallPerMinute":"2",'
        +'"Amount":"22.30",'
        +'"isReceiverInit":"false"'
        +'}';
        var a = JSON.parse(obj);
        console.log("parseee :" + a);
        var jsonObj = JSON.stringify(eval("(" + obj + ")"));
        console.log("jkahsdjkah = " + jsonObj);


     var message_obj =   "{"
        	+'"ApiKey": "success",'
        	+'"SessionId": "asdf",'
        	+'"Token": "asdf",'
        	+'"ProfileImage": "asdf",'
        	+'"UserName": "asdf",'
        	+'"MessageData": {'
        		+'"MessageBody": "hello this is a new message",'
        		+'"MessageType": "sendTextMessage",'
        		+'"ConnectionData": {'
        			+'"connectionId": "7302F7C1-F13B-48F7-A291-FB4FD7BE4592",'
        			+'"creationTime": "1505382391231000",'
        			+'"data": "asdf"'
        		+'}'
        	+'}'
        +'}'


TokBoxPhonegapPlugin.sendTextMessage(message_obj,
    function(success){
        alert("success");
    },
    function(error){
        alert("error");
    });
});



