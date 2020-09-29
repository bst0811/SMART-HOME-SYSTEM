#include <SoftwareSerial.h>
#include <OneWire.h>
#include <DallasTemperature.h>

#define DEBUG true
boolean sent = false;

// Data wire is plugged into digital pin 12 on the Arduino
#define ONE_WIRE_BUS 12

// Setup a oneWire instance to communicate with any OneWire device
OneWire oneWire(ONE_WIRE_BUS);  

// Pass oneWire reference to DallasTemperature library
DallasTemperature sensors(&oneWire);

int smokeSensorPin = A0;
int lightSensorPin = A1; 
int tempPin =8;
//digital pins
int ledPin = 4;
int fanPin = 5;
int buzzerPin = 6;
int motionPin = 7;               // choose the input pin (for PIR sensor)
int pirState = LOW;             // we start, assuming no motion detected
//int temperature;
int SmokeSensorVal = 0;
int val = 0;                    // variable for reading the pin status
int  relayPin =9 ;
int solenoid = 3;//pin for lock


String temperature,motionStatus,smokeStatus,lightStatus, lightSensorStatus,fanStatus,doorStatus;
 
SoftwareSerial esp8266(2,3); // make RX Arduino line is pin 2, make TX Arduino line is pin 3.
                             // This means that you need to connect the TX line from the esp to the Arduino's pin 2
                             // and the RX line from the esp to the Arduino's pin 3

   String server=""; //variable for sending data to webpage

void setup()
{
  Serial.begin(9600);
  esp8266.begin(9600); // your esp's baud rate
  pinMode(lightSensorPin,INPUT);
  pinMode(ledPin,OUTPUT);
  pinMode(smokeSensorPin, INPUT);
  pinMode(tempPin,INPUT);
  pinMode(relayPin,OUTPUT);
  pinMode(fanPin,OUTPUT);
  analogWrite(fanPin, 0);
  pinMode(motionPin, INPUT);     // declare sensor as input
  pinMode(solenoid,OUTPUT);
  
  sensors.begin();  // Start up the library for temp sensor
     
  sendData("AT+RST\r\n",2000,DEBUG); // reset module
  sendData("AT+CWMODE=2\r\n",1000,DEBUG); // configure as access point
  sendData("AT+CIFSR\r\n",1000,DEBUG); // get ip address
  sendData("AT+CIPMUX=1\r\n",1000,DEBUG); // configure for multiple connections
  sendData("AT+CIPSERVER=1,80\r\n",1000,DEBUG); // turn on server on port 80

  /*Serial.println("Smart Home");
  Serial. println("Design by BSE20-43");
  Serial.println();*/
}
 
void loop()
{     
        server = "HTTP/1.1 200 OK\r\nConnection: Closed\r\n\r\n"+Createjson();
        //delay(5000);
    if(esp8266.available()){
          if(sent == false){
            
        sendToServer(server);
        //sent = true;
        }
       
        Receive();   
        esp8266.println("AT+CIPCLOSE=0"); // close TCP/UDP connection
        } 

}
     

 
/* 
* Name: sendData
* Description: Function used to send data to ESP8266.
* Params: command - the data/command to send; timeout - the time to wait for a response; debug - print to Serial window?(true = yes, false = no)
* Returns: The response from the esp8266 (if there is a reponse)
*/
String sendData(String command, const int timeout, boolean debug)
{
    String response = "";
    
    esp8266.print(command); // send the read character to the esp8266
    
    long int time = millis();
    
    while( (time+timeout) > millis())
    {
      while(esp8266.available())
      {
        
        // The esp has data so display its output to the serial window 
        char c = esp8266.read(); // read the next character.
        response+=c;
      }  
    }
    
    if(debug)
    {
      Serial.print(response);
    }
    
    return response;
}


void sendD(String server1)//send data to module
{
int p=0;
while(1)
{
unsigned int l=server1.length();
Serial.print("AT+CIPSEND=0,");
// setting data length to be sent
esp8266.print("AT+CIPSEND=0,");// send data of id=0,
Serial.println(l+2);
esp8266.println(l+2);//send data of id=0,length l+2
delay(100);
Serial.println(server1);
esp8266.println(server1); 
while(esp8266.available())
{

if(esp8266.find("OK"))
{
p=11;
break;
}
}
if(p==11)
break;
delay(100);
}
}

void sendToServer(String server)//send data to webpage
{
        if(esp8266.find("0,CONNECT"))
        {
        Serial.println("Starting");
        sendD(server);
        delay(1000);
        //esp8266.println("AT+CIPCLOSE=0"); //close  TCP/UDP connection
        Serial.println("Finished");
        
        delay(1000);
        Serial.write(esp8266.read());
        }
     
}  

void Receive(){

  //receiving data
  
     if(esp8266.find("+IPD,"))
    {
      Serial.println("found");
     delay(1000); // wait for the serial buffer to fill up (read all the serial data)
     // get the connection id so that we can then disconnect
     int connectionId = esp8266.read()-48; // subtract 48 because the read() function returns 
                                           // the ASCII decimal value and 0 (the first decimal number) starts at 48
          
     esp8266.find("pin="); // advance cursor to "pin="
     
    // int pinNumber = (esp8266.read()-48)*10; // get first number i.e. if the pin 13 then the 1st number is 1, then multiply to get 10
     //int pinNumber = (esp8266.read()-48); // get second number, i.e. if the pin number is 13 then the 2nd number is 3, then add to the first number
    int pinNumber = 4;
     digitalWrite(pinNumber, !digitalRead(pinNumber)); // toggle pin    
     
     // make close command
     String closeCommand = "AT+CIPCLOSE="; 
     closeCommand+=connectionId; // append connection id
     closeCommand+="\r\n";
     Serial.write(esp8266.read()); 
          
    }
     
}


String Createjson(){
  //LIGHT
   int ldr = analogRead(lightSensorPin);
   if(ldr<=900){
    //Serial.print("THERE IS DARKNESS HENCE THE LIGHT IS ON");
    digitalWrite(relayPin, HIGH);
    lightStatus = "on";
    //delay(10000);
    //Serial.println();
    //Serial.println(ldr);
    }else{
      //Serial.print("THERE IS LIGHT HENCE THE LIGHT IS OFF");
      lightStatus = "off";
  digitalWrite(relayPin, LOW);
  //delay(8000);
 // Serial.println();
    
  //Serial.println(ldr);
  
      }
      //delay(1000);

     //SMOKE  todo tone function
  SmokeSensorVal = analogRead(smokeSensorPin);
  /*Serial.print("SMOKE VALUE = ");
  Serial.println(SmokeSensorVal);*/
  if(SmokeSensorVal>100){
    tone(buzzerPin,1000,1000);
    /*Serial.print("SMOKE DETECTED");
    Serial.print("\n");*/
    smokeStatus ="detected";
    //delay(1000);
    //noTone(buzzerPin);
    }else{
      smokeStatus ="none detected";
      }

  //temperature code
  // Send the command to get temperatures
 sensors.requestTemperatures(); 

  //print the temperature in Celsius
  int c = sensors.getTempCByIndex(0);
  /*Serial.print("Temperature: ");
  Serial.print(c);
  Serial.print((char)223);//shows degrees character
  Serial.print("C  |  ");
  
  //print the temperature in Fahrenheit
  Serial.print((sensors.getTempCByIndex(0) * 9.0) / 5.0 + 32.0);
  Serial.print((char)176);//shows degrees character
  Serial.println("F");*/

  if(c<23){
    analogWrite(fanPin,map(0,0,9,0,255));
    fanStatus  = "off";
  delay(3000);
    }
  else
  {
    fanStatus  = "on";
    analogWrite(fanPin,map(9,0,9,0,255));
  delay(3000);
    } 

    //motion
   val = digitalRead(motionPin);  // read input value
  if (val == HIGH) {            // check if the input is HIGH
    digitalWrite(ledPin, HIGH);  // turn LED ON
    //delay(10000);
    if (pirState == LOW) {
      // we have just turned on
      //tone(buzzerPin,1000,10000);
      //Serial.println("Motion detected!");
       motionStatus = "detected";
      // We only want to print on the output change, not state
      
      pirState = HIGH;
      
    }
    
  } else {
    
    digitalWrite(ledPin, LOW); // turn LED OFF
    if (pirState == HIGH){
      // we have just turned of
      //Serial.println("Motion ended!");
      // We only want to print on the output change, not state
       motionStatus = "ended";
      pirState = LOW;
    }else{
      motionStatus = "none detected";
      }
  }

  //door lock
  if(SmokeSensorVal>100){
  digitalWrite(solenoid,HIGH);
  doorStatus = "open";
  //delay(2000);
  }else{
  digitalWrite(solenoid,LOW);
  doorStatus = "closed";
  //delay(2000);
  }
  
      temperature = String(c);

     server = "{";
  server += "\"temp\":\""+String(c)+"\"";
  server += ",\"smoke\":\""+smokeStatus+"\"";
  server += ",\"light\":\""+lightStatus+"\"";
  server += ",\"door\":\""+doorStatus+"\"";
  server += ",\"fan\":\""+fanStatus+"\"";
  server += ",\"motion\":\""+motionStatus+"\"";
  server += "}";

    return server;
  }
