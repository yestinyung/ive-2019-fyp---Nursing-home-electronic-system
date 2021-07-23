#include <LiquidCrystal_I2C.h>

#include <SoftwareSerial.h>
#include <LiquidCrystal.h>
#include <Servo.h>
Servo myservo;
SoftwareSerial esp8266(6, 7); //Pin 2 & 3 of Arduino as RX and TX. Connect TX and RX of ESP8266 respectively.
#define DEBUG true
#define led_pin 8 //LED is connected to Pin 11 of Arduino
String responseIP = "";
boolean getIP = false;
LiquidCrystal_I2C lcd(0x27, 2, 1, 0, 4, 5, 6, 7, 3, POSITIVE);
void setup()
{
  lcd.begin(16, 2);
  lcd.print("Loading...");
  pinMode(led_pin, OUTPUT);
  digitalWrite(led_pin, LOW);
  Serial.begin(9600);
  esp8266.begin(115200); //Baud rate for communicating with ESP8266. Your's might be different.
  esp8266Serial("AT+RST\r\n", 5000, DEBUG); // Reset the ESP8266
  esp8266Serial("AT+CWMODE=1\r\n", 5000, DEBUG); //Set station mode Operation
  esp8266Serial("AT+CWJAP=\"yestinyung\",\"Aa616296\"\r\n", 5000, DEBUG);//Enter your WiFi network's SSID and Password.
  myservo.attach(9);
  while (!esp8266.find("OK"))
  {
  }
  getIP = true;
  esp8266Serial("AT+CIFSR\r\n", 5000, DEBUG);//You will get the IP Address of the ESP8266 from this command.
  esp8266Serial("AT+CIPMUX=1\r\n", 5000, DEBUG);
  esp8266Serial("AT+CIPSERVER=1,80\r\n", 5000, DEBUG);
}

void loop()
{
  if (esp8266.available())
  {
    if (esp8266.find("+IPD,"))
    {
      String msg;
      esp8266.find("?");
      msg = esp8266.readStringUntil(' ');
      String command1 = msg.substring(0, 3);
      String command2 = msg.substring(4);

      if (DEBUG)
      {
        //Must print "led"
        Serial.println(command2);//Must print "ON" or "OFF"
      }
      delay(100);
      if (command2 == "0") {
        digitalWrite(led_pin, HIGH);
        myservo.write(0);
      } else if (command2 == "10") {
        digitalWrite(led_pin, HIGH);
        myservo.write(40);
      } else if (command2 == "20") {
        digitalWrite(led_pin, HIGH);
        myservo.write(70);
      } else if (command2 == "30") {
        digitalWrite(led_pin, HIGH);
        myservo.write(100);
      } else if (command2 == "40") {
        digitalWrite(led_pin, HIGH);
        myservo.write(120);
      }
    } else {
      digitalWrite(led_pin, LOW);
    }
  }
}

String esp8266Serial(String command, const int timeout, boolean debug)
{
  String response = "";
  esp8266.print(command);
  long int time = millis();
  while ( (time + timeout) > millis())
  {
    while (esp8266.available())
    {
      char c = esp8266.read();
      response += c;
    }
  }
  if (debug)
  {
    Serial.print(response);
    if (getIP) {
      lcd.begin(16, 2);
      lcd.print("IP:" + response.substring(35, 40));
      Serial.println("IP: [" + response.substring(27, 40) + "]");
      getIP = false;
    }
  }
  return response;
}
