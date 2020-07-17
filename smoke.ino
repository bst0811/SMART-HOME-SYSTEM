int Input = A0;
int SensorVal = 0;
int buzzerPin = 2;

void setup() {
  Serial.begin(9600);
  pinMode(Input, INPUT);
  Serial.println("Interfacing of Smoke Sensor with Arduino");
  Serial. println("Design by www.TheEngineeringProjects.com");
  Serial.println();
}

void loop() {

  SensorVal = analogRead(Input);
  Serial.println(SensorVal);
  
  if(SensorVal>100){
    tone(buzzerPin,1000,1000);
    //delay(1000);
    noTone(buzzerPin);
    }
    
}
