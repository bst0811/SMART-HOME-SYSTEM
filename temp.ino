#include <OneWire.h>
#include <DallasTemperature.h>

// Data wire is plugged into digital pin 2 on the Arduino
#define ONE_WIRE_BUS 2
int fan = 8;
// Setup a oneWire instance to communicate with any OneWire device
OneWire oneWire(ONE_WIRE_BUS);  

// Pass oneWire reference to DallasTemperature library
DallasTemperature sensors(&oneWire);

void setup(void)
{
  sensors.begin();  // Start up the library
  Serial.begin(9600);
  analogWrite(fan, LOW);
 pinMode(fan,OUTPUT);
}

void loop(void)
{ 
  // Send the command to get temperatures
  sensors.requestTemperatures(); 

  //print the temperature in Celsius
  int c = sensors.getTempCByIndex(0);
  Serial.print("Temperature: ");
  Serial.print(c);
  Serial.print((char)223);//shows degrees character
  Serial.print("C  |  ");
  
  //print the temperature in Fahrenheit
  Serial.print((sensors.getTempCByIndex(0) * 9.0) / 5.0 + 32.0);
  Serial.print((char)176);//shows degrees character
  Serial.println("F");

  if(c<23){
    analogWrite(fan, HIGH);
    delay(5000);
    }
  else
  {
    analogWrite(fan, LOW);
    delay(2000);
    }
  delay(1000);
}
