#include <SoftwareSerial.h>
 
#define DEBUG true
boolean sent = false;
 
SoftwareSerial esp8266(2,3); // make RX Arduino line is pin 2, make TX Arduino line is pin 3.
                             // This means that you need to connect the TX line from the esp to the Arduino's pin 2
                             // and the RX line from the esp to the Arduino's pin 3

   String server=""; //variable for sending data to webpage
void setup()
{
  pinMode(2, INPUT);
  pinMode(3, OUTPUT);
  
  Serial.begin(9600);
  esp8266.begin(9600); // your esp's baud rate
  
  pinMode(11,OUTPUT);
  digitalWrite(11,LOW);
  
  pinMode(12,OUTPUT);
  digitalWrite(12,LOW);
  
  pinMode(13,OUTPUT);
  digitalWrite(13,LOW);
   
  sendData("AT+RST\r\n",2000,DEBUG); // reset module
  sendData("AT+CWMODE=2\r\n",1000,DEBUG); // configure as access point
  sendData("AT+CIFSR\r\n",1000,DEBUG); // get ip address
  sendData("AT+CIPMUX=1\r\n",1000,DEBUG); // configure for multiple connections
  sendData("AT+CIPSERVER=1,80\r\n",1000,DEBUG); // turn on server on port 80
}
 
void loop()
{     
  server = "<h1>Welcome to Data Receiving from Arduino</h1>";
        //server = ""+23;
       
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
     
     int pinNumber = (esp8266.read()-48)*10; // get first number i.e. if the pin 13 then the 1st number is 1, then multiply to get 10
     pinNumber += (esp8266.read()-48); // get second number, i.e. if the pin number is 13 then the 2nd number is 3, then add to the first number
     
     digitalWrite(pinNumber, !digitalRead(pinNumber)); // toggle pin    
     
     // make close command
     String closeCommand = "AT+CIPCLOSE="; 
     closeCommand+=connectionId; // append connection id
     closeCommand+="\r\n";
     Serial.write(esp8266.read()); 
          
    }
    
 
}
