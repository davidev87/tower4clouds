/*
 * Copyright (C) 2014 Politecnico di Milano (marco.miglierina@polimi.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//Hide the information divs
cleanBoxes();

var URL = "../v1/metrics";

//Error constants
var AJAX = 1, GET = 2;
var SEND = 1, DELETE = 2, OPEN = 3;

var observers = "";
/*
 * 
 * @param a anonymous function caller for make the lists of rules
 */
$(document).ready(function () {
    getter();
});

/*
 * makes an AJAX call in get mode, for getting the installed metrics.
 */
function getter() {
    var jqXMLHttpGet = $.get(
            URL, jsonParser, "text"
            )
            .fail(function () {
                detectError(GET);
            });
}

function jsonParser(json) {
    var obj = $.parseJSON(json);
    var length = obj.metrics.length;
    var strToPrint = "";
    for (var i = 0; i < length; i++) {
        strToPrint += "<div class='panel panel-default'>";
        strToPrint += "<div class='panel-heading' id='panelHeader'>";
        strToPrint += "<span class='glyphicon glyphicon-sort' aria-hidden='true' onclick=toggle('" + obj.metrics[i] + "')></span>";
        strToPrint += "  " + obj.metrics[i];
        strToPrint += "<input type='button' value='Add Observer' onclick=sendFetching('" + obj.metrics[i] + "') class='floatRight' />";
        strToPrint += "<input type='text' size='50' placeholder='Callback URL' class='floatRight' id='callbackUrl" + obj.metrics[i] + "' />";
        strToPrint += "<input type='text' size='50' placeholder='Format (RDF/JSON, GRAPHITE, INFLUXDB...)' class='floatRight' id='format" + obj.metrics[i] + "' />";
        strToPrint += "</div>";

        $("#metricsKeeper").append(strToPrint);

        observersGetter(obj.metrics[i]);
        strToPrint = "";
    }

}

function observersGetter(metricID) {
    var url = URL + "/" + metricID + "/observers";
    $.ajax({type: "GET",
        url: url,
        contentType: "application/json",
        async: false,
        success: function (data) {
            observersParser(data, metricID);
        }
    });

}

function deleteObserver(composedID) {
    observerID = composedID.split("§")[0];
    metricID = composedID.split("§")[1];
    var url = URL + "/" + metricID + "/observers/" + observerID;
    $.ajax({type: "DELETE",
        url: url,
        error: function (jqXHR, textStatus, errorThrown) {
            detectError(textStatus + " " + jqXHR.status  + " : " + errorThrown);
            tableReloader();
        },
        success: function () {
            showConfirmMessage(DELETE);
            tableReloader();
        }
    });
}

function observersParser(obj, metricID) {
    //var obj = $.parseJSON(jsonString);a
    var returnStr = "<div class='panel-body borderedDiv' id='toggled_" + metricID + "'>";
    var param = "";
    returnStr += "<div class='col-lg-2'><u>Observer ID</u></div>";
    returnStr += "<div class='col-lg-4'><u>Callback Address</u></div>";
    returnStr += "<div class='col-lg-2'><u>Protocol</u></div>";
    returnStr += "<div class='col-lg-2'><u>Format</u></div>";
    returnStr += "<div class='col-lg-2'><u>Delete</u></div>";
    var length = obj.observers.length;

    for (var i = 0; i < length; i++) {
        param = "";
        returnStr += "<div class='row'>";
        returnStr += "<div class='col-lg-2'>" + obj.observers[i].id + "</div>";
        returnStr += "<div class='col-lg-4'>" + obj.observers[i].callbackUrl + "</div>";
        returnStr += "<div class='col-lg-2'>" + obj.observers[i].protocol + "</div>";
        returnStr += "<div class='col-lg-2'>" + obj.observers[i].format + "</div>";
        returnStr += "<div class='col-lg-2'>";
        param = obj.observers[i].id + "§" + metricID ;
        returnStr += "<button onclick=deleteObserver('" + param + "')>";
        returnStr += "<span class = 'glyphicon glyphicon-trash floatRight' aria-hidden = 'true' /></button> </div>";
        returnStr += "</div>";
    }
    returnStr += "</div></div>";
    $("#metricsKeeper").append(returnStr);

    return;
}


function toggle(metricID) {
    $("#toggled_" + metricID).slideToggle();
}

function sendFetching(metricID) {
    var callbackTextID = "#callbackUrl" + metricID;
    var formatTextID = "#format" + metricID;
    var observerInfo = "{ \"callbackUrl\": \"" + $(callbackTextID).val() + "\", " +
    		" \"format\": \""+ $(formatTextID).val() + "\" }";
    var metricURL = "../v1/metrics/" + metricID + "/observers";
    sender(metricURL, observerInfo);

}

/*
 * decides in which of the two possibilities is sended the XML rule
 */
function sender(metricURL, observerInfo) {
    $.ajax({type: "POST",
        url: "../" + metricURL,
        data: observerInfo,
        contentType: "application/json",
        cache: false,
        error: function (jqXHR, textStatus, errorThrown) {
            detectError(textStatus + " " + jqXHR.status  + " : " + errorThrown);
            tableReloader();
        },
        success: function (xml) {
            showConfirmMessage(SEND);
            tableReloader();
        }
    });
}

function tableReloader() {
    $("#metricsKeeper").empty();
    getter();
}

/*
 * Manager of the Warning-div messages according to the error types
 */
function detectError(info) {
    // display error
    $("#error").text(info);
    $("success").hide();
    $("#error").show();
}

/*
 * Manages the information div
 */
function showConfirmMessage(type) {
    var res;
    $("#error").hide();
    switch (type) {
        case 1:
            res = "Request performed correctly!!";
            break;
        case 2:
            res = "Rule correctly deleted";
            break;
        case 3:
            res = "XML correctly found";
            break;
    }
    $("#success").text(res);
    $("#success").show();
}

function cleanBoxes() {
    $("#error").hide();
    $("#success").hide();
}
