#include <OneWire.h>
#include <DallasTemperature.h>

// Data wire is plugged into digital pin 2 on the Arduino
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
int temperature;
int SmokeSensorVal = 0;
int val = 0;                    // variable for reading the pin status
int  relayPin = 9;
void setup() {
  Serial.begin(9600);
  pinMode(lightSensorPin,INPUT);
  pinMode(ledPin,OUTPUT);
  pinMode(smokeSensorPin, INPUT);
  pinMode(tempPin,INPUT);
  pinMode(relayPin,OUTPUT);
  pinMode(fanPin,OUTPUT);
  analogWrite(fanPin, 0);
  pinMode(motionPin, INPUT);     // declare sensor as input
  sensors.begin();  // Start up the library for temp sensor
  
  Serial.println("Smart Home");
  Serial. println("Design by BSE20-43");
  Serial.println();
}

void loop() {
  //SMOKE
  SmokeSensorVal = analogRead(smokeSensorPin);
  Serial.print("SMOKE VALUE = ");
  Serial.println(SmokeSensorVal);
  
  if(SmokeSensorVal>100){
    tone(buzzerPin,1000,10000);
    Serial.print("SMOKE DETECTED");
    Serial.print("\n");
    delay(10000);
    //noTone(buzzerPin);
    }
    
    //LIGHT
    int ldr = analogRead(lightSensorPin);
   if(ldr<=900){
    Serial.print("THERE IS DARKNESS HENCE THE LIGHT IS ON");
    digitalWrite(relayPin, HIGH);
    delay(10000);
    Serial.println();
    //Serial.println(ldr);
    }else{
      Serial.print("THERE IS LIGHT HENCE THE LIGHT IS OFF");
  digitalWrite(relayPin, LOW);
  delay(2000);
  Serial.println();
  
  //Serial.println(ldr);
  
      }
      delay(1000);
      
      //temperature code
      
  // Send the command to get temperatures
  sensors.requestTemperatures(); 

  //print the temperature in Celsius
  int c = sensors.getTempCByIndex(0);
  Serial.print("Temperature: ");
  Serial.print(c);
  Serial.print("*");//shows degrees character
  Serial.print("C  |  ");
  
  //print the temperature in Fahrenheit
  Serial.print((sensors.getTempCByIndex(0) * 9.0) / 5.0 + 32.0);
  //Serial.print(();//shows degrees character
  Serial.println("F");

  if(c<28){
    analogWrite(fanPin,map(0,0,9,0,255));
  delay(5000);
    }
  else
  {
    analogWrite(fanPin,map(9,0,9,0,255));
  delay(5000);
    } 
    
    //motion
   val = digitalRead(motionPin);  // read input value
  if (val == HIGH) {            // check if the input is HIGH
    digitalWrite(ledPin, HIGH);  // turn LED ON
    //delay(10000);
    if (pirState == LOW) {
      // we have just turned on
      //tone(buzzerPin,1000,10000);
      Serial.println("Motion detected!");
      // We only want to print on the output change, not state
      
      pirState = HIGH;
      
    }
  } else {
    
    digitalWrite(ledPin, LOW); // turn LED OFF
    if (pirState == HIGH){
      // we have just turned of
      Serial.println("Motion ended!");
      // We only want to print on the output change, not state
      pirState = LOW;
    }
  }
    
}
