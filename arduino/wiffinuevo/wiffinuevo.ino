
String dato = "";
String recolector;
String userdata;
String ip; 
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>
#include <SoftwareSerial.h>

//#define SERVER_IP "10.0.1.7:9080" // PC address with emulation on host
#define SERVER_IP "192.168.1.9";


#ifndef STASSID
//#define STASSID "UTEQ-DOCENTES"
//#define STAPSK  "docenciauteq"
//#define STASSID "Jorge Molina"
//#define STAPSK  "1223334444"
#define STASSID "NETLIFE-NOBOA"
#define STAPSK  "1206242610HH"
//#define STASSID "SQL SERVER"
//#define STAPSK  "Leonor_Angel&Erick1993$"
#endif

void setup() {

  Serial.begin(115200);

  Serial.println();
  Serial.println();
  Serial.println();
  
 
  WiFi.begin(STASSID, STAPSK);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected! IP address: ");
  // obtener la ip actual automaticamente
  ip = IpAddress2String(WiFi.localIP());
  Serial.println(ip);
  //Serial.println(WiFi.localIP());
}

void loop() {

  while (Serial.available())
  {
    dato = Serial.read();
    recolector = recolector + dato;
    dato = "";
   // delay(1000);
  }
 // Serial.println(recolector);

  // wait for WiFi connection
  if ((WiFi.status() == WL_CONNECTED)) {
    WiFiClient client;
    HTTPClient http;
    Serial.print("[HTTP] begin...\n");
    // configure traged server and url'
    //String webservices = "http://"+ip+":8080/bsmarthome/webresources/data/insertdata";
    
    //String webservices = "http://192.168.1.5:8080/bsmarthome/webresources/data/insertdata";

    String webservices = "http://aplicaciones.uteq.edu.ec/bsmarthome/webresources/data/insertdata";
   
    Serial.print(webservices);
    http.begin(client, webservices); //HTTP
    http.addHeader("Content-Type", "application/json");

    Serial.print("[HTTP] POST...\n");
    // start connection and send HTTP header and body
    int httpCode = http.POST(recolector);
    recolector = "";
    // httpCode will be negative on error
    if (httpCode > 0) {
      // HTTP header has been send and Server response header has been handled
      Serial.printf("[HTTP] POST... code: %d\n", httpCode);

      // file found at server
      if (httpCode == HTTP_CODE_OK) {
        const String& payload = http.getString();
        Serial.println("received payload:\n<<");
        Serial.println(payload);
        Serial.println(">>");
      }
    } else {
      Serial.printf("[HTTP] POST... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
  }
  delay(1500);
}

String IpAddress2String(const IPAddress& ipAddress)
{
    return String(ipAddress[0]) + String(".") +
           String(ipAddress[1]) + String(".") +
           String(ipAddress[2]) + String(".") +
           String(int (ipAddress[3]));
}

void apirestjava(){
    
}
