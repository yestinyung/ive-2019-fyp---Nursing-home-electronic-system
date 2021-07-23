#include "DHT.h"

#include <Servo.h>
#define dht_apin A4 // Analog Pin sensor is connected to
#include <Wire.h>
#include "RTClib.h"
#include <SoftwareSerial.h>
#define RX 10
#define TX 11
// WIFI SHIELD DECLARATION
#define DEBUG true
String ssid = "\"yestinyung\"";
String pass = "\"Aa616296\"";
String tcp = "\"TCP\"";
String remoteip = "\"leo1997.000webhostapp.com\"";
String portnum = "80";
String cmd;
SoftwareSerial ESP11 = SoftwareSerial(10, 11); // RX, TX

String field = "field1";
int countTrueCommand;
int countTimeCommand;
boolean found = false;
int valSensor = 1;

RTC_DS1307 RTC;
#include <LiquidCrystal.h>
//*********************************************
#include <Keypad.h>
boolean isRoomNum = false;
boolean overRoomNum = false;
char key;
long Number;
#define Password_Length 3
char Data[Password_Length];
byte data_count = 0;
const byte ROWS = 4; //four rows
const byte COLS = 4; //four columns
// Define the Keymap
char hexaKeys[ROWS][COLS] = {
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};
byte rowPins[ROWS] = {23, 25, 27, 29}; //connect to the Rows of the keypad pin 8, 7, 6, 5 respectively
byte colPins[COLS] = {31, 33, 35, 37}; //connect to the Columns of the keypad pin 4, 3, 2, 1 respectively
int savetime = 0;
int time_save = 0;
boolean Ftime = false;
boolean timeout = false;
//initialize an instance of class NewKeypad
Keypad kpd = Keypad( makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);
//*********************************************
int potPin = 3;

int trigPin0 = 8;    // Trigger
int echoPin0 = 7;

int trigPin1 = 6;    // Trigger
int echoPin1 = 5;

long duration0, cm0, inches0;
long duration1, cm1, inches1;
const int rs = 22, en = 24, d4 = 26, d5 = 28, d6 = 30, d7 = 32;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);
dht DHT;
void setup()
{
  lcd.begin(16, 2);
  lcd.print("Loading...");
  Serial.begin(9600);
  pinMode(trigPin0, OUTPUT);
  pinMode(echoPin0, INPUT);
  pinMode(trigPin1, OUTPUT);
  pinMode(echoPin1, INPUT);
  Wire.begin();
  ESP11.begin(115200);
  RTC.begin();
  // Check to see if the RTC is keeping time.  If it is, load the time from your computer.
  if (! RTC.isrunning()) {
    Serial.println("RTC is NOT running!");
    // This will reflect the time that your sketch was compiled
    RTC.adjust(DateTime(__DATE__, __TIME__));
  }
  sendData("AT+CWMODE=1\r\n", 2000, DEBUG); // configure as access point and Client
  sendData("AT+RST\r\n", 2000, DEBUG); // reset module
  sendData("AT+GMR\r\n", 2000, DEBUG); // View version Info
  sendData("AT+CWLAP\r\n", 5000, DEBUG); // List all available AP's*/
  sendData("AT+CWJAP=" + ssid + "," + pass + "\r\n", 10000, DEBUG); // Connect to AP
  sendData("AT+CIFSR\r\n", 2000, DEBUG); // get ip address
  sendData("AT+CIPMUX=0\r\n", 2000, DEBUG); //Single TCP Connections
}

void loop()
{
  if (isRoomNum == false) {
    lcd.setCursor(0, 0); // 設定游標位置在第一行行首
    lcd.print("room number?");
    key = kpd.getKey(); //storing pressed key value in a char
    if (key != NO_KEY) {
      lcd.clear(); //Then clean it
      if (key == '1')
      {
        if (Number == 0)
          Number = 1;
        else
          Number = (Number * 10) + 1;
      }

      if (key == '4')
      {
        if (Number == 0)
          Number = 4;
        else
          Number = (Number * 10) + 4;
      }

      if (key == '7')
      {
        if (Number == 0)
          Number = 7;
        else
          Number = (Number * 10) + 7;
      }


      if (key == '0')
      {
        if (Number == 0)
          Number = 0;
        else
          Number = (Number * 10) + 0;
      }

      if (key == '2')
      {
        if (Number == 0)
          Number = 2;
        else
          Number = (Number * 10) + 2;
      }

      if (key == '5')
      {
        if (Number == 0)
          Number = 5;
        else
          Number = (Number * 10) + 5;
      }

      if (key == '8')
      {
        if (Number == 0)
          Number = 8;
        else
          Number = (Number * 10) + 8;
      }

      if (key == '3')
      {
        if (Number == 0)
          Number = 3;
        else
          Number = (Number * 10) + 3;
      }

      if (key == '6')
      {
        if (Number == 0)
          Number = 6;
        else
          Number = (Number * 10) + 6;
      }

      if (key == '9')
      {
        if (Number == 0)
          Number = 9;
        else
          Number = (Number * 10) + 9;
      }
      if (key == 'A')
      {
        if (Number < 1) {
          lcd.setCursor(0, 0); // 設定游標位置在第一行行首
          lcd.print("Can't be 0");
          delay(1000);
        } else {
          isRoomNum = true;
          overRoomNum = true;
          lcd.setCursor(0, 0); // 設定游標位置在第一行行首
          lcd.print("room is ok");
          delay(1000);
          lcd.clear();
        }
      }
      if (key == 'C') {
        Number = 0;
      }
    }
    if (overRoomNum == false) {
      if (Number >= 1000) {
        lcd.setCursor(0, 1); // 設定游標位置在第2行行首
        lcd.print("over");
        delay(1000);
        Number = 0;
        lcd.clear();
      }
      lcd.setCursor(0, 1); // 設定游標位置在第2行行首
      lcd.print(Number);
    }
  } else {
    digitalWrite(trigPin0, LOW);
    delayMicroseconds(5);
    digitalWrite(trigPin0, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin0, LOW);

    pinMode(echoPin0, INPUT);
    duration0 = pulseIn(echoPin0, HIGH);

    cm0 = (duration0 / 2) / 29.1;   // Divide by 29.1 or multiply by 0.0343
    inches0 = (duration0 / 2) / 74; // Divide by 74 or multiply by 0.0135

    Serial.print("ONE, ");
    Serial.print(cm0);
    Serial.print("cmONE");
    Serial.println();

    digitalWrite(trigPin1, LOW);
    delayMicroseconds(5);
    digitalWrite(trigPin1, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin1, LOW);

    pinMode(echoPin1, INPUT);
    duration1 = pulseIn(echoPin1, HIGH);

    cm1 = (duration1 / 2) / 29.1;   // Divide by 29.1 or multiply by 0.0343
    inches1 = (duration1 / 2) / 74; // Divide by 74 or multiply by 0.0135

    Serial.print(inches1);
    Serial.print("TWO, ");
    Serial.print(cm1);
    Serial.print("cmTWO");
    Serial.println();

    DHT.read11(dht_apin);
    Serial.print("Current humidity = ");
    Serial.print(DHT.humidity);
    int humidity = DHT.humidity;
    Serial.print("%  ");
    Serial.print("temperature = ");
    Serial.print(DHT.temperature);
    int temperature = DHT.temperature;
    Serial.println("C  ");
    //**************************************
    DateTime now = RTC.now();
    Serial.print(now.month(), DEC);
    Serial.print('/');
    Serial.print(now.day(), DEC);
    Serial.print('/');
    Serial.print(now.year(), DEC);
    Serial.print(' ');
    Serial.print(now.hour(), DEC);
    Serial.print(':');
    Serial.print(now.minute(), DEC);
    Serial.print(':');
    Serial.print(now.second(), DEC);
    Serial.println();
    //**************************************


    //**************************************
    savetime = now.hour();
    if (!Ftime) {
      time_save = now.hour();
      Ftime = true;
    }
    String isSave;
    Serial.println();
    Serial.println("   cm0 " + String(cm0) + "    cm1 " + String(cm1));
    Serial.println();
    if ((cm0 > 26 && cm1 > 26 &&  time_save < savetime ) || ( cm0 < 2000 && cm1 < 2000 &&  time_save < savetime)) {
      isSave = "N";
      timeout = true;
    } else {
      if (timeout) {
        timeout = false;
        Ftime = false;
      }
      isSave = "Y";
    }
    SubmitHttpRequest ("?cm1=" + String(cm0) + "&cm2=" + String(cm1) + "&humidity=" + String(humidity) + "&temperature=" + String(temperature) + "&roomnum=" + String(Number) + "&isSave=" + String(isSave));
  }
}
void GetData() {
  // put your main code here, to run repeatedly:
  // For ESP8266
  if (ESP11.available()) // check if the esp is sending a message
  {
    if (ESP11.find("+IPD,"))
    {
      delay(1000); // wait for the serial buffer to fill up (read all the serial data)
      // get the connection id so that we can then disconnect
      int connectionId = ESP11.read() - 48; // subtract 48 because the read() function returns
      // the ASCII decimal value and 0 (the first decimal number) starts at 48

      ESP11.find("pin="); // advance cursor to "pin="

      int pinNumber = (ESP11.read() - 48) * 10; // get first number i.e. if the pin 13 then the 1st number is 1, then multiply to get 10
      pinNumber += (ESP11.read() - 48); // get second number, i.e. if the pin number is 13 then the 2nd number is 3, then add to the first number

      digitalWrite(pinNumber, !digitalRead(pinNumber)); // toggle pin

      // make close command
      String closeCommand = "AT+CIPCLOSE=";
      closeCommand += connectionId; // append connection id
      closeCommand += "\r\n";

      sendData(closeCommand, 1000, DEBUG); // close connection
    }
  }
}
/*
  Name: sendData
  Description: Function used to send data to ESP8266.
  Params: command - the data/command to send; timeout - the time to wait for a response; debug - print to Serial window?(true = yes, false = no)
  Returns: The response from the esp8266 (if there is a reponse)
*/
String sendData(String command, const int timeout, boolean debug)
{
  String response = ""; // ESP8266 sendData String
  ESP11.print(command); // send the read character to the esp8266
  long int time = millis();

  while ( (time + timeout) > millis())
  {
    while (ESP11.available())
    {

      // The esp has data so display its output to the serial window
      char c = ESP11.read(); // read the next character.
      response += c;
    }
  }

  if (debug)
  {
    Serial.print(response);

  }
  return response;
}

void SubmitHttpRequest(String data)
{
  sendData("AT+CIPSTART=" + tcp + "," + remoteip + "," + portnum + "\r\n", 3000, DEBUG); // Start connecting to localhost <link id>,<type>,<remote IP>,<Port Number>
  String getStr;
  getStr = "POST /php/AddRoomDetail.php" + data + " HTTP/1.1\r\n";
  getStr += "Host: leo1997.000webhostapp.com\r\n"; // add the required header
  getStr += "\r\n";
  // send data length
  cmd = "AT+CIPSEND=";
  cmd += String(getStr.length());
  sendData(cmd + "\r\n", 1000, DEBUG);
  sendData(getStr, 3000, DEBUG);
  GetData();

}
