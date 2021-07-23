#include <SoftwareSerial.h>
#include <String.h>
#include <Wire.h>
#include "MAX30105.h"
#include "heartRate.h"
#define DEBUG true
SoftwareSerial ESP11 = SoftwareSerial(8, 7); // RX, TX
//////////////////////////
MAX30105 particleSensor;
const int MPU6050_addr = 0x68;
int16_t AccX, AccY, AccZ, Temp, GyroX, GyroY, GyroZ;
const byte RATE_SIZE = 4; //Increase this for more averaging. 4 is good.
byte rates[RATE_SIZE]; //Array of heart rates
byte rateSpot = 0;
long lastBeat = 0; //Time at which the last beat occurred

float beatsPerMinute;
int beatAvg;
int BPMtest = 0;
int Time = 0;
int fallTime = 0;
int AccXfall = 0;
int AccYfall = 0;
int AccZfall = 0;
bool fall = false;
bool fallX = false;
bool fallY = false;
bool fallZ = false;
int magnitudeX = 0;
int magnitudeY = 0;
int magnitudeZ = 0;
int fallTimeOut = 0;

// WIFI SHIELD DECLARATION
String ssid = "\"iPhone\"";
String pass = "\"Aa616296\"";
String tcp = "\"TCP\"";
String remoteip = "\"leo1997.000webhostapp.com\"";
String portnum = "80";
String cmd;
String id = "1";
String getTemp;
String getHB;
void setup()
{
  Serial.begin(9600);    // the GPRS baud rate
  Serial.println("Initializing...");
  // Initialize sensor
  if (!particleSensor.begin(Wire, I2C_SPEED_FAST)) //Use default I2C port, 400kHz speed
  {
    Serial.println("MAX30105 was not found. Please check wiring/power. ");
    while (1);
  }
  Serial.println("Place your index finger on the sensor with steady pressure.");
  particleSensor.setup(); //Configure sensor with default settings
  particleSensor.setPulseAmplitudeRed(0x0A); //Turn Red LED to low to indicate sensor is running
  particleSensor.setPulseAmplitudeGreen(0); //Turn off Green LED
  Wire.begin();
  Wire.beginTransmission(MPU6050_addr);
  Wire.write(0x6B);
  Wire.write(0);
  Wire.endTransmission(true);
  ESP11.begin(115200);
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
  long irValue = particleSensor.getIR();
  if (checkForBeat(irValue) == true)
  {
    //We sensed a beat!
    long delta = millis() - lastBeat;
    lastBeat = millis();

    beatsPerMinute = 60 / (delta / 1000.0);

    if (beatsPerMinute < 255 && beatsPerMinute > 20)
    {
      rates[rateSpot++] = (byte)beatsPerMinute; //Store this reading in the array
      rateSpot %= RATE_SIZE; //Wrap variable

      //Take average of readings
      beatAvg = 0;
      for (byte x = 0 ; x < RATE_SIZE ; x++)
        beatAvg += rates[x];
      beatAvg /= RATE_SIZE;
    }
  }
  /*Serial.print("IR=");
    Serial.print(irValue);
    Serial.print(", BPM=");
    Serial.print(beatsPerMinute);
    Serial.print(", Avg BPM=");
    Serial.print(beatAvg);
    if (irValue < 50000)
    Serial.print(" No finger?");
    Serial.println();*/
  Wire.endTransmission();
  Wire.beginTransmission(MPU6050_addr);
  Wire.write(0x3B);
  Wire.endTransmission(false);
  Wire.requestFrom(MPU6050_addr, 14, true);
  AccX = Wire.read() << 8 | Wire.read();
  AccY = Wire.read() << 8 | Wire.read();
  AccZ = Wire.read() << 8 | Wire.read();
  Temp = Wire.read() << 8 | Wire.read();
  GyroX = Wire.read() << 8 | Wire.read();
  GyroY = Wire.read() << 8 | Wire.read();
  GyroZ = Wire.read() << 8 | Wire.read();
  /*
    Serial.print("AccX = "); Serial.print(AccX);
    Serial.print(" || AccY = "); Serial.print(AccY);
    Serial.print(" || AccZ = "); Serial.print(AccZ);
    Serial.print(" || Temp = "); Serial.print(Temp/340.00+36.53);
    Serial.print(" || GyroX = "); Serial.print(GyroX);
    Serial.print(" || GyroY = "); Serial.print(GyroY);
    Serial.print(" || GyroZ = "); Serial.println(GyroZ);*/
  Wire.endTransmission();
  if (GyroX < 1) {
    GyroX = GyroX / 10 * -1;
  }
  if (GyroY < 1) {
    GyroY = GyroY / 10 * -1;
  }
  if (GyroZ < 1) {
    GyroZ = GyroZ / 10 * -1;
  }
  magnitudeX = GyroX / 10;
  magnitudeY = GyroY / 10;
  magnitudeZ = GyroZ / 10;
  if (magnitudeX + magnitudeY + magnitudeZ > 2000) {
    fall = true;
  }
  if (fall) {
    if (fallTime == 20) {
      if (AccX + 100 > AccXfall || AccX - 100 < AccXfall) {
        fallX = true;
      } else {
        fallX = false;
      }
      if (AccY + 100 > AccYfall || AccY - 100 < AccYfall) {
        fallY = true;
      } else {
        fallY = false;
      }
      if (AccZ + 100 > AccZfall || AccZ - 100 < AccZfall) {
        fallZ = true;
      } else {
        fallZ = false;
      }
      fallTime = 0;
      fallTimeOut++;
    } else if (fallTime == 10) {
      AccXfall = AccX;
      AccYfall = AccY;
      AccZfall = AccZ;
    } else {
      fallTime++;
    }
  }
  if (fallTimeOut == 100) {
    fall = false;
    fallTimeOut = 0;
  }
  if (fallX && fallY && fallZ) {
    SubmitHttpRequest("Null", "fall");
  }
  if (Time == 100) {
    getTemp = String(Temp / 340.00 + 36.53);
    getHB = String(beatAvg);
    SubmitHttpRequest("Null", "Data");
    Time = 0;
  }
  Time++;
}
void SubmitHttpRequest(String Data, String C)
{
  sendData("AT+CIPSTART=" + tcp + "," + remoteip + "," + portnum + "\r\n", 3000, DEBUG); // Start connecting to localhost <link id>,<type>,<remote IP>,<Port Number>
  String getStr;
  if (C = "Data") {
    getStr = "POST /php/AddData.php?id=" + id + "&Temp=" + getTemp + "&HB=" + getHB + " HTTP/1.1\r\n";
  } else if (C = "fall") {
    getStr = "POST /php/AddGPSRecord.php?id="+id+"&isSave=N&location=0 HTTP/1.1\r\n";
  }
  getStr += "Host: leo1997.000webhostapp.com\r\n"; // add the required header
  getStr += "\r\n";
  // send data length
  cmd = "AT+CIPSEND=";
  cmd += String(getStr.length());
  sendData(cmd + "\r\n", 1000, DEBUG);
  sendData(getStr, 3000, DEBUG);
  GetData();

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
